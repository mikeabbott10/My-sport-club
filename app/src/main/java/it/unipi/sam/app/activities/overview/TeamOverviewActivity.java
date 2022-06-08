package it.unipi.sam.app.activities.overview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.android.material.appbar.AppBarLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import it.unipi.sam.app.R;
import it.unipi.sam.app.databinding.ActivityOverviewBinding;
import it.unipi.sam.app.databinding.TeamInfoContentBinding;
import it.unipi.sam.app.util.AvatarImageBehavior;
import it.unipi.sam.app.util.DMRequestWrapper;
import it.unipi.sam.app.util.DebugUtility;
import it.unipi.sam.app.util.JacksonUtil;
import it.unipi.sam.app.util.OverviewActivityAlphaHandler;
import it.unipi.sam.app.util.ResourcePreferenceWrapper;
import it.unipi.sam.app.util.SharedPreferenceUtility;
import it.unipi.sam.app.util.Team;
import it.unipi.sam.app.util.VCNews;

public class TeamOverviewActivity extends OverviewActivity implements View.OnClickListener {
    private static final String TAG = "AAATeamOverviewActivity";
    private String urlBasePath = null;
    private String teamCode;
    protected TeamInfoContentBinding teamInfoContentBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        teamCode = getIntent().getStringExtra(getString(R.string.team_code));
        if(teamCode ==null){
            DebugUtility.showSimpleSnackbar(binding.getRoot(), "No team selected, please go back.", 5000);
            return;
        }
        if(restInfoInstance == null) {
            DebugUtility.showSimpleSnackbar(binding.getRoot(), "Something went wrong, please retry later.", 5000);
            return;
        }
        urlBasePath = getString(R.string.restBasePath) + restInfoInstance.getTeamsPath() + teamCode + "/";

        teamInfoContentBinding = TeamInfoContentBinding.inflate(getLayoutInflater());

        // load cover image
        Glide
            .with(this)
            .load(urlBasePath + restInfoInstance.getKeyWords().get(getString(R.string.cover_image)))
            //.centerCrop()
            .placeholder(R.drawable.placeholder_126)
            .error(R.drawable.placeholder_126)
            .into(binding.toolbarLogo);

        // load avatar image
        Glide
                .with(this)
                .load(urlBasePath + restInfoInstance.getKeyWords().get(getString(R.string.profile_image)))
                //.centerCrop()
                .placeholder(R.drawable.placeholder_126)
                .error(R.drawable.placeholder_126)
                .into(binding.avatarImage);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(urlBasePath!=null && restInfoInstance != null)
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
        OverviewActivityAlphaHandler.handleGenericViewVisibility(teamInfoContentBinding.leagueDescription, null, currentScrollingPercentage);
    }

    private void startRequestsForPopulatingActivityLayout() {
        Map<String, Long> riiMap = restInfoInstance.getLastModifiedTimestamp().get( restInfoInstance.getTeamsPath()+teamCode );
        ResourcePreferenceWrapper teamInfoJsonPreference = null;
        if(riiMap!=null) {
            teamInfoJsonPreference = SharedPreferenceUtility.getResourceUri(this, getString(R.string.teams)+teamCode+OVERVIEW_INFO_JSON,
                    riiMap.get(getString(R.string.info_file)));
        }else{
            teamInfoJsonPreference = SharedPreferenceUtility.getResourceUri(this, getString(R.string.teams)+teamCode+OVERVIEW_INFO_JSON,
                    null);
        }

        if(teamInfoJsonPreference!=null && teamInfoJsonPreference.getUri()!=null) {
            DebugUtility.LogDThis(DebugUtility.SERVER_COMMUNICATION, TAG, "getVolleyCecinaNews. From local", null);
            super.handleResponseUri(teamInfoJsonPreference.getDMResourceId(), OVERVIEW_INFO_JSON,
                    teamInfoJsonPreference.getUri(), teamInfoJsonPreference.getLastModifiedTimestamp(), false);
        }else {
            DebugUtility.LogDThis(DebugUtility.SERVER_COMMUNICATION, TAG, "getVolleyCecinaNews. From net", null);
            enqueueRequest(
                    new DMRequestWrapper(urlBasePath + restInfoInstance.getKeyWords().get(getString(R.string.info_file)),
                            "randomTitle", "randomDescription", false, false, OVERVIEW_INFO_JSON,
                            false, null, null)
            );
        }
    }

    @Override
    public void onJsonInformationReceived(long dm_resource_id, String uri, Integer type, long lastModifiedTimestamp, boolean updateResourcePreference) {
        super.onJsonInformationReceived(dm_resource_id, uri, type, lastModifiedTimestamp, updateResourcePreference);
        String content = getFileContentFromUri(uri);
        // perform jackson from file to object
        Team t;
        try {
            t = (Team) JacksonUtil.getObjectFromString(content, Team.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            DebugUtility.showSimpleSnackbar(binding.getRoot(), "ERROR 01. Please retry later.", 5000);
            return;
        }
        binding.toolbarMainTextviewTitle.setText(t.getCurrentLeague());
        binding.mainTextviewTitle.setText(t.getCurrentLeague());
        String s = getString(R.string.coach) + t.getCoach();
        binding.mainTextviewDescription.setText(s);

        // todo: aggiungi team_activity_overview_content (popolato) alla cardview binding.info_container
        teamInfoContentBinding.leagueDescription.setText(t.getLeagueDescription());
        teamInfoContentBinding.leagueDescription.setObject(t.getLeagueLink());
        teamInfoContentBinding.leagueDescription.setOnClickListener(this);

        binding.infoContainer.addView(teamInfoContentBinding.getRoot());
        // update resource preference if needed
        if(updateResourcePreference)
            SharedPreferenceUtility.setResourceUri(this, getString(R.string.teams)+teamCode+type, uri, lastModifiedTimestamp, dm_resource_id);
    }

    @Override
    public void onClick(View view) {
        if(view==teamInfoContentBinding.leagueDescription){
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse((String) teamInfoContentBinding.leagueDescription.getObj())));
        }
    }
}
