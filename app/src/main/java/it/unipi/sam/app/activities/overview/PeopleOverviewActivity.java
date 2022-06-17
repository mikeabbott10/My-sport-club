package it.unipi.sam.app.activities.overview;

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.android.material.appbar.AppBarLayout;

import java.util.Map;

import it.unipi.sam.app.R;
import it.unipi.sam.app.databinding.PersonInfoContentBinding;
import it.unipi.sam.app.util.Constants;
import it.unipi.sam.app.util.DMRequestWrapper;
import it.unipi.sam.app.util.DebugUtility;
import it.unipi.sam.app.util.JacksonUtil;
import it.unipi.sam.app.util.OverviewActivityAlphaHandler;
import it.unipi.sam.app.util.Person;
import it.unipi.sam.app.util.ResourcePreferenceWrapper;
import it.unipi.sam.app.util.SharedPreferenceUtility;
import it.unipi.sam.app.util.graphics.ParamImageView;

public class PeopleOverviewActivity extends OverviewActivity implements View.OnClickListener {
    private static final String TAG = "AAAPeopleOverviewActivity";
    private String urlPersonBasePath = null;
    private String personCode;
    protected PersonInfoContentBinding personInfoContentBinding;

    private ColorMatrixColorFilter cf;
    private String thisPersonPartialPath;
    private Map<String, Object> thisLastModifiedEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        thisLastModifiedEntry = restInfoInstance.getLastModified().get(thisPersonPartialPath);

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
        matrix.setSaturation(0);  //0 means grayscale
        cf = new ColorMatrixColorFilter(matrix);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        Person p;
        try {
            p = (Person) JacksonUtil.getObjectFromString(content, Person.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            DebugUtility.showSimpleSnackbar(binding.getRoot(), "ERROR 06. Please retry later.", 5000);
            return;
        }
        binding.toolbarMainTextviewTitle.setText(p.getName());
        binding.mainTextviewTitle.setText(p.getName());
        binding.mainTextviewDescription.setText(p.getRole());

        // social views
        for (int i = 0; i<p.getSocial().size(); ++i) {
            View c = personInfoContentBinding.socialRow.getChildAt(i);
            if(p.getSocial().get(i).equals("")){
                // social not available, make the view grey
                setLocked((ImageView) c);
            }else{
                c.setVisibility(View.VISIBLE);
                ((ParamImageView) c).setObject(p.getSocial().get(i));
                c.setOnClickListener(this);
            }
        }

        // player info
        if(p.getNumber()!=-1) {
            String num = "#" + p.getNumber() + " ";
            personInfoContentBinding.numberTv.setText(num);
            personInfoContentBinding.numberTv1.setText(num);
        }
        personInfoContentBinding.nationalityValueTv.setText(p.getNation());
        String year = ""+p.getBirthYear();
        personInfoContentBinding.yearValueTv.setText(year);
        if(p.getHeight()!=-1){
            String h = p.getHeight() + "cm";
            personInfoContentBinding.heightValueTv.setText(h);
        }

        // todo: usa p.getTeam().get("name")

        binding.infoContainer.addView(personInfoContentBinding.getRoot());

        // update resource preference if needed
        if(updateResourcePreference)
            SharedPreferenceUtility.setResourceUri(this, Constants.people_key+personCode+type, uri, lastModifiedTimestamp, dm_resource_id);
    }

    @Override
    public void onClick(View view) {
        if(view instanceof ParamImageView){
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse((String) ((ParamImageView)view).getObj())));
        }
    }

    public void setLocked(ImageView v) {
        v.setColorFilter(cf);
        v.setImageAlpha(128);   // 128 = 0.5
    }
}
