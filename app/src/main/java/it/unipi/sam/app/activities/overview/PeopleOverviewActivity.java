package it.unipi.sam.app.activities.overview;

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;

import java.util.Map;

import it.unipi.sam.app.R;
import it.unipi.sam.app.databinding.PersonInfoContentBinding;
import it.unipi.sam.app.ui.favorites.RetriveFavoritesRunnable;
import it.unipi.sam.app.ui.favorites.SetFavoritesRunnable;
import it.unipi.sam.app.util.Constants;
import it.unipi.sam.app.util.DMRequestWrapper;
import it.unipi.sam.app.util.DebugUtility;
import it.unipi.sam.app.util.FavoritesWrapper;
import it.unipi.sam.app.util.JacksonUtil;
import it.unipi.sam.app.util.OverviewActivityAlphaHandler;
import it.unipi.sam.app.util.Person;
import it.unipi.sam.app.util.ResourcePreferenceWrapper;
import it.unipi.sam.app.util.SharedPreferenceUtility;
import it.unipi.sam.app.util.graphics.ParamImageView;
import it.unipi.sam.app.util.room.AppDatabase;

public class PeopleOverviewActivity extends OverviewActivity implements View.OnClickListener {
    private static final String TAG = "AAAPeopleOverviewActivity";
    private String urlPersonBasePath = null;
    private String personCode;
    protected PersonInfoContentBinding personInfoContentBinding;

    private ColorMatrixColorFilter cf;
    private String thisPersonPartialPath;

    private AppDatabase db;
    private Person thisPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DebugUtility.LogDThis(DebugUtility.IDENTITY_LOG, TAG, "onCreate()", null);

        personCode = getIntent().getStringExtra(Constants.peopleCode);
        if(personCode ==null){
            DebugUtility.showSimpleSnackbar(binding.getRoot(), "No one selected, please go back.", 5000);
            return;
        }
        if(restInfoInstance == null) {
            DebugUtility.showSimpleSnackbar(binding.getRoot(), "Something went wrong, please retry later.", 5000);
            return;
        }

        thisPersonPartialPath = restInfoInstance.getPeoplePath() + personCode;
        urlPersonBasePath = Constants.restBasePath + thisPersonPartialPath + "/";
        Map<String, Object> thisLastModifiedEntry = restInfoInstance.getLastModified().get(thisPersonPartialPath);


        personInfoContentBinding = PersonInfoContentBinding.inflate(getLayoutInflater());

        // load cover image
        Glide
            .with(this)
            .load( getCoverImagePath(thisPersonPartialPath, thisLastModifiedEntry) )
            //.centerCrop()
            .placeholder(R.drawable.placeholder_126)
            .error(R.drawable.placeholder_126)
            .into(binding.toolbarLogo);

        // load avatar image
        Glide
            .with(this)
            .load( getProfileImagePath(thisPersonPartialPath, thisLastModifiedEntry) )
            //.centerCrop()
            .placeholder(R.drawable.person_placeholder)
            .error(R.drawable.person_placeholder)
            .into(binding.avatarImage);

        // init grey filter for disabling social buttons
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);  // 0 means grayscale
        cf = new ColorMatrixColorFilter(matrix);

        // ROOM
        db = AppDatabase.getDatabase(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        DebugUtility.LogDThis(DebugUtility.IDENTITY_LOG, TAG, "onStart()", null);
        if(urlPersonBasePath !=null && restInfoInstance != null)
            startRequestsForPopulatingActivityLayout();
    }

    /**
     * Hide league description if scrolled up
     * Because info_container hides itself under the toolbar
     * @param appBarLayout
     * @param offset
     */
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        super.onOffsetChanged(appBarLayout, offset);
        OverviewActivityAlphaHandler.handleGenericViewVisibility(personInfoContentBinding.socialRow, null, currentScrollingPercentage);
    }

    private void startRequestsForPopulatingActivityLayout() {
        Map<String, Object> riiMap = restInfoInstance.getLastModified().get( thisPersonPartialPath );
        ResourcePreferenceWrapper personInfoJsonPreference = null;
        if(riiMap!=null) {
            personInfoJsonPreference = SharedPreferenceUtility.getResourceUri(this, Constants.people_key+personCode+OVERVIEW_INFO_JSON,
                    (Long) riiMap.get(Constants.infoFile));
        }else{
            personInfoJsonPreference = SharedPreferenceUtility.getResourceUri(this, Constants.people_key+personCode+OVERVIEW_INFO_JSON,
                    null);
        }

        if(personInfoJsonPreference!=null && personInfoJsonPreference.getUri()!=null) {
            DebugUtility.LogDThis(DebugUtility.SERVER_COMMUNICATION, TAG, "getPerson. From local", null);
            super.handleResponseUri(personInfoJsonPreference.getDMResourceId(), OVERVIEW_INFO_JSON,
                    personInfoJsonPreference.getUri(), personInfoJsonPreference.getLastModifiedTimestamp(), false);
        }else {
            DebugUtility.LogDThis(DebugUtility.SERVER_COMMUNICATION, TAG, "getPerson. From net", null);
            enqueueRequest(
                    new DMRequestWrapper(urlPersonBasePath + restInfoInstance.getKeyWords().get(Constants.infoFile),
                            "randomTitle", "randomDescription", false, false, OVERVIEW_INFO_JSON,
                            false, null, null)
            );
        }
    }

    @Override
    public void onJsonInformationReceived(long dm_resource_id, String uri, Integer type, long lastModifiedTimestamp, boolean updateResourcePreference) {
        super.onJsonInformationReceived(dm_resource_id, uri, type, lastModifiedTimestamp, updateResourcePreference);
        if(personInfoContentBinding.getRoot().getParent() != null)
            // view già aggiunta a binding.infoContainer
            return;
        String content = getFileContentFromUri(uri);
        // perform jackson from file to object
        try {
            thisPerson = (Person) JacksonUtil.getObjectFromString(content, Person.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            DebugUtility.showSimpleSnackbar(binding.getRoot(), "ERROR 06. Please retry later.", 5000);
            return;
        }
        binding.toolbarMainTextviewTitle.setText(thisPerson.getName());
        binding.mainTextviewTitle.setText(thisPerson.getName());
        binding.mainTextviewDescription.setText(thisPerson.getRole());

        // social views
        for (int i = 0; i< thisPerson.getSocial().size(); ++i) {
            View c = personInfoContentBinding.socialRow.getChildAt(i);
            if(thisPerson.getSocial().get(i).equals("")){
                // social not available, make the view grey
                setLocked((ImageView) c);
            }else{
                c.setVisibility(View.VISIBLE);
                ((ParamImageView) c).setObject(thisPerson.getSocial().get(i));
                c.setOnClickListener(this);
            }
        }

        // player info
        if(thisPerson.getNumber()!=-1) {
            String num = "#" + thisPerson.getNumber() + " ";
            personInfoContentBinding.numberTv.setText(num);
            personInfoContentBinding.numberTv1.setText(num);
        }
        personInfoContentBinding.nationalityValueTv.setText(thisPerson.getNation());
        String year = ""+ thisPerson.getBirthYear();
        personInfoContentBinding.yearValueTv.setText(year);
        if(thisPerson.getHeight()!=-1){
            String h = thisPerson.getHeight() + "cm";
            personInfoContentBinding.heightValueTv.setText(h);
        }

        // todo: mostra team con: p.getTeam().get("name")

        binding.infoContainer.addView(personInfoContentBinding.getRoot());

        // set favorites icon
        new Thread(new RetriveFavoritesRunnable(favorites -> {
            if(favorites.contains(new FavoritesWrapper(thisPerson))){
                binding.overviewToolbar.getMenu().getItem(0).setIcon(R.drawable.ic_filled_red_heart);
            }else
                binding.overviewToolbar.getMenu().getItem(0).setIcon(R.drawable.ic_empty_heart);
        }, db)).start();

        // update resource preference if needed
        if(updateResourcePreference)
            SharedPreferenceUtility.setResourceUri(this, Constants.people_key+personCode+type, uri, lastModifiedTimestamp, dm_resource_id);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view instanceof ParamImageView){
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse((String) ((ParamImageView)view).getObj())));
        }
    }

    public void setLocked(ImageView v) {
        v.setColorFilter(cf);
        v.setImageAlpha(128);   // 128 is 0.5
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if(item.getItemId()==R.id.menu_fav){
            // add or remove from favorites
            if(thisPerson!=null) {
                try {
                    SetFavoritesRunnable r =
                            new SetFavoritesRunnable(db,
                                    new FavoritesWrapper(thisPerson),
                                    this, item
                            );
                    new Thread(r).start();
                }catch (IllegalArgumentException e){
                    Snackbar.make(binding.getRoot(), "ERROR 11: please retry later", 2000).show();
                }
            }
        }
        return false;
    }

    @Override
    public void onFavoritesSetted(Object obj, int operation) {
        if(operation==SetFavoritesRunnable.DELETED){
            ((MenuItem)obj).setIcon(R.drawable.ic_empty_heart);
        }else if(operation==SetFavoritesRunnable.INSERTED){
            ((MenuItem)obj).setIcon(R.drawable.ic_filled_red_heart);
        }
    }
}
