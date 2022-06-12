package it.unipi.sam.app.activities.overview;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.android.material.appbar.AppBarLayout;

import java.util.Map;

import it.unipi.sam.app.R;
import it.unipi.sam.app.util.DMRequestWrapper;
import it.unipi.sam.app.util.DebugUtility;
import it.unipi.sam.app.util.JacksonUtil;
import it.unipi.sam.app.util.ResourcePreferenceWrapper;
import it.unipi.sam.app.util.SharedPreferenceUtility;

public class PeopleOverviewActivity extends OverviewActivity implements View.OnClickListener {
    private static final String TAG = "AAATeamOverviewActivity";
    private String urlPersonBasePath = null;
    private String personCode;
    protected it.unipi.sam.app.databinding.PersonInfoContentBinding personInfoContentBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        personCode = getIntent().getStringExtra(getString(R.string.people_code));
        if(personCode ==null){
            DebugUtility.showSimpleSnackbar(binding.getRoot(), "No one selected, please go back.", 5000);
            return;
        }
        if(restInfoInstance == null) {
            DebugUtility.showSimpleSnackbar(binding.getRoot(), "Something went wrong, please retry later.", 5000);
            return;
        }
        urlPersonBasePath = getString(R.string.restBasePath) + restInfoInstance.getPeoplePath() + personCode + "/";

        personInfoContentBinding = it.unipi.sam.app.databinding.PersonInfoContentBinding.inflate(getLayoutInflater());

        // load cover image
        Glide
                .with(this)
                .load(urlPersonBasePath + restInfoInstance.getKeyWords().get(getString(R.string.cover_image)))
                //.centerCrop()
                .placeholder(R.drawable.placeholder_126)
                .error(R.drawable.placeholder_126)
                .into(binding.toolbarLogo);


        // load avatar image
        Glide
            .with(this)
            .load(urlPersonBasePath + restInfoInstance.getKeyWords().get(getString(R.string.profile_image)))
            //.centerCrop()
            .placeholder(R.drawable.person_placeholder)
            .error(R.drawable.person_placeholder)
            .into(binding.avatarImage);

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
        //OverviewActivityAlphaHandler.handleGenericViewVisibility(personInfoContentBinding.leagueDescription, null, currentScrollingPercentage);
    }

    private void startRequestsForPopulatingActivityLayout() {
        Map<String, Long> riiMap = restInfoInstance.getLastModifiedTimestamp().get( restInfoInstance.getPeoplePath()+ personCode);
        ResourcePreferenceWrapper personInfoJsonPreference = null;
        if(riiMap!=null) {
            personInfoJsonPreference = SharedPreferenceUtility.getResourceUri(this, getString(R.string.people)+ personCode +OVERVIEW_INFO_JSON,
                    riiMap.get(getString(R.string.info_file)));
        }else{
            personInfoJsonPreference = SharedPreferenceUtility.getResourceUri(this, getString(R.string.people)+ personCode +OVERVIEW_INFO_JSON,
                    null);
        }

        if(personInfoJsonPreference!=null && personInfoJsonPreference.getUri()!=null) {
            DebugUtility.LogDThis(DebugUtility.SERVER_COMMUNICATION, TAG, "getVolleyCecinaNews. From local", null);
            super.handleResponseUri(personInfoJsonPreference.getDMResourceId(), OVERVIEW_INFO_JSON,
                    personInfoJsonPreference.getUri(), personInfoJsonPreference.getLastModifiedTimestamp(), false);
        }else {
            DebugUtility.LogDThis(DebugUtility.SERVER_COMMUNICATION, TAG, "getVolleyCecinaNews. From net", null);
            enqueueRequest(
                    new DMRequestWrapper(urlPersonBasePath + restInfoInstance.getKeyWords().get(getString(R.string.info_file)),
                            "randomTitle", "randomDescription", false, false, OVERVIEW_INFO_JSON,
                            false, null, null)
            );
        }
    }

    @Override
    public void onJsonInformationReceived(long dm_resource_id, String uri, Integer type, long lastModifiedTimestamp, boolean updateResourcePreference) {
        super.onJsonInformationReceived(dm_resource_id, uri, type, lastModifiedTimestamp, updateResourcePreference);
        if(personInfoContentBinding.getRoot().getParent() != null)
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
        if(!p.getRole().equals("")){
            String s = getString(R.string.role_title) + p.getRole();
            binding.mainTextviewDescription.setText(s);
        }


        // update resource preference if needed
        if(updateResourcePreference)
            SharedPreferenceUtility.setResourceUri(this, getString(R.string.teams)+ personCode +type, uri, lastModifiedTimestamp, dm_resource_id);
    }

    @Override
    public void onClick(View view) {
        /*if(view==personInfoContentBinding.leagueDescription && personInfoContentBinding.leagueDescription.getObj()!=null){
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse((String) personInfoContentBinding.leagueDescription.getObj())));
        }else if(view instanceof ParamImageView){
            // TODO: scrivere PeopleOverviewActivity e lanciare quella
            Intent i = new Intent(this, PeopleOverviewActivity.class);
            i.putExtra(getString(R.string.people_code), (String) ((ParamImageView) view).getObj());
            i.putExtra(getString(R.string.rest_info_instance_key), restInfoInstance);
            startActivity(i);
        }*/
    }
}
