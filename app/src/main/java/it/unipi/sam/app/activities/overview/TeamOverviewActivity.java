package it.unipi.sam.app.activities.overview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.android.material.appbar.AppBarLayout;

import java.util.Map;

import it.unipi.sam.app.R;
import it.unipi.sam.app.databinding.TeamInfoContentBinding;
import it.unipi.sam.app.util.DMRequestWrapper;
import it.unipi.sam.app.util.DebugUtility;
import it.unipi.sam.app.util.JacksonUtil;
import it.unipi.sam.app.util.OverviewActivityAlphaHandler;
import it.unipi.sam.app.util.ResourcePreferenceWrapper;
import it.unipi.sam.app.util.SharedPreferenceUtility;
import it.unipi.sam.app.util.Team;
import it.unipi.sam.app.util.graphics.ParamImageView;

public class TeamOverviewActivity extends OverviewActivity implements View.OnClickListener {
    private static final String TAG = "AAATeamOverviewActivity";
    private String urlTeamBasePath = null;
    private String teamCode;
    protected TeamInfoContentBinding teamInfoContentBinding;
    private String thisTeamPartialPath;
    private Map<String, Object> thisLastModifiedEntry;

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
        thisTeamPartialPath = restInfoInstance.getTeamsPath() + teamCode;
        urlTeamBasePath = getString(R.string.restBasePath) + thisTeamPartialPath + "/";
        thisLastModifiedEntry = restInfoInstance.getLastModified().get(thisTeamPartialPath);

        teamInfoContentBinding = TeamInfoContentBinding.inflate(getLayoutInflater());

        // load cover image
        Glide
            .with(this)
            .load( getCoverImagePath(thisTeamPartialPath, thisLastModifiedEntry) )
            //.centerCrop()
            .placeholder(R.drawable.placeholder_126)
            .error(R.drawable.placeholder_126)
            .into(binding.toolbarLogo);


        // load avatar image
        /*Glide
            .with(this)
            .load( getProfileImagePath(thisPartialPath, thisLastModifiedEntry) )
            //.centerCrop()
            .placeholder(R.drawable.placeholder_126)
            .error(R.drawable.placeholder_126)
            .into(binding.avatarImage);*/

        binding.avatarImage.setImageResource(R.drawable.vc);
        binding.avatarImage.setBorderWidth(0);
        binding.avatarImage.setDisableCircularTransformation(true);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(urlTeamBasePath !=null && restInfoInstance != null)
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
        Map<String, Object> riiMap = restInfoInstance.getLastModified().get(thisTeamPartialPath);
        ResourcePreferenceWrapper teamInfoJsonPreference = null;
        if(riiMap!=null) {
            teamInfoJsonPreference = SharedPreferenceUtility.getResourceUri(this, getString(R.string.teams)+teamCode+OVERVIEW_INFO_JSON,
                    (Long) riiMap.get(getString(R.string.info_file)));
        }else{
            teamInfoJsonPreference = SharedPreferenceUtility.getResourceUri(this, getString(R.string.teams)+teamCode+OVERVIEW_INFO_JSON,
                    null);
        }

        if(teamInfoJsonPreference!=null && teamInfoJsonPreference.getUri()!=null) {
            DebugUtility.LogDThis(DebugUtility.SERVER_COMMUNICATION, TAG, "getTeam. From local", null);
            super.handleResponseUri(teamInfoJsonPreference.getDMResourceId(), OVERVIEW_INFO_JSON,
                    teamInfoJsonPreference.getUri(), teamInfoJsonPreference.getLastModifiedTimestamp(), false);
        }else {
            DebugUtility.LogDThis(DebugUtility.SERVER_COMMUNICATION, TAG, "getTeam. From net", null);
            enqueueRequest(
                    new DMRequestWrapper(urlTeamBasePath + restInfoInstance.getKeyWords().get(getString(R.string.info_file)),
                            "randomTitle", "randomDescription", false, false, OVERVIEW_INFO_JSON,
                            false, null, null)
            );
        }
    }

    @Override
    public void onJsonInformationReceived(long dm_resource_id, String uri, Integer type, long lastModifiedTimestamp, boolean updateResourcePreference) {
        super.onJsonInformationReceived(dm_resource_id, uri, type, lastModifiedTimestamp, updateResourcePreference);
        if(teamInfoContentBinding.getRoot().getParent() != null)
            return;
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
        String s = getString(R.string.coach_title) + t.getCoach().get(getString(R.string.res_name));
        binding.mainTextviewDescription.setText(s);

        teamInfoContentBinding.leagueDescription.setText(t.getLeagueDescription());
        teamInfoContentBinding.leagueDescription.setObject(t.getLeagueLink());
        teamInfoContentBinding.leagueDescription.setOnClickListener(this);

        // players views
        for (int i=0; i<t.getPlayers().size(); i++) {
            String currPlayerPath = t.getPlayers().get(i).get(getString(R.string.res_path));
            assert currPlayerPath != null;
            if(!currPlayerPath.equals("")) {
                //String currPlayerName = t.getPlayers().get(i).get(getString(R.string.res_name));
                ParamImageView iv=new ParamImageView(this);
                iv.setPadding(10,10,10,10);
                iv.setObject(currPlayerPath);
                iv.setOnClickListener(this);
                String partialPath = restInfoInstance.getPeoplePath() + currPlayerPath;
                Glide
                        .with(this)
                        .load( getProfileImagePath(partialPath, restInfoInstance.getLastModified().get(partialPath)) )
                        .centerCrop()
                        .placeholder(R.drawable.person_placeholder)
                        .error(R.drawable.person_placeholder)
                        .into(iv);
                teamInfoContentBinding.playersGrid.addView(iv);
            }
        }

        // coach view
        String coachPath = t.getCoach().get(getString(R.string.res_path));
        assert coachPath != null;
        if(!coachPath.equals("")) {
            //String coachName = t.getCoach().get(getString(R.string.res_name));
            ParamImageView iv =new ParamImageView(this);
            iv.setPadding(10,10,10,10);
            iv.setObject(coachPath);
            iv.setOnClickListener(this);
            String partialPath = restInfoInstance.getPeoplePath() + coachPath;
            Glide
                    .with(this)
                    .load( getProfileImagePath(partialPath, restInfoInstance.getLastModified().get(partialPath)) )
                    .centerCrop()
                    .placeholder(R.drawable.person_placeholder)
                    .error(R.drawable.person_placeholder)
                    .into(iv);
            teamInfoContentBinding.coachGrid.addView(iv);
        }

        // staff views
        String secondCoachPath = t.getSecondCoach().get(getString(R.string.res_path));
        assert secondCoachPath != null;
        if(!secondCoachPath.equals("")) {
            //String secondCoachName = t.getSecondCoach().get(getString(R.string.res_name));
            ParamImageView iv =new ParamImageView(this);
            iv.setPadding(10,10,10,10);
            iv.setObject(secondCoachPath);
            iv.setOnClickListener(this);
            String partialPath = restInfoInstance.getPeoplePath() + secondCoachPath;
            Glide
                .with(this)
                .load( getProfileImagePath(partialPath, restInfoInstance.getLastModified().get(partialPath)) )
                .centerCrop()
                .placeholder(R.drawable.person_placeholder)
                .error(R.drawable.person_placeholder)
                .into(iv);
            teamInfoContentBinding.staffGrid.addView(iv);
        }

        // dirigenti views
        for (int i=0; i<t.getAssistantManager().size(); i++) {
            String currManagerPath = t.getAssistantManager().get(i).get(getString(R.string.res_path));
            assert currManagerPath != null;
            if(!currManagerPath.equals("")) {
                //String currManagerName = t.getAssistantManager().get(i).get(getString(R.string.res_name));
                ParamImageView iv=new ParamImageView(this);
                iv.setPadding(10,10,10,10);
                iv.setObject(currManagerPath);
                iv.setOnClickListener(this);
                String partialPath = restInfoInstance.getPeoplePath() + currManagerPath;
                Glide
                    .with(this)
                    .load( getProfileImagePath(partialPath, restInfoInstance.getLastModified().get(partialPath)) )
                    .centerCrop()
                    .placeholder(R.drawable.person_placeholder)
                    .error(R.drawable.person_placeholder)
                    .into(iv);
                teamInfoContentBinding.staffGrid.addView(iv);
            }

        }

        binding.infoContainer.addView(teamInfoContentBinding.getRoot());

        // update resource preference if needed
        if(updateResourcePreference)
            SharedPreferenceUtility.setResourceUri(this, getString(R.string.teams)+teamCode+type, uri, lastModifiedTimestamp, dm_resource_id);
    }

    @Override
    public void onClick(View view) {
        if(view==teamInfoContentBinding.leagueDescription && teamInfoContentBinding.leagueDescription.getObj()!=null){
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse((String) teamInfoContentBinding.leagueDescription.getObj())));
        }else if(view instanceof ParamImageView){
            Intent i = new Intent(this, PeopleOverviewActivity.class);
            i.putExtra(getString(R.string.people_code), (String) ((ParamImageView) view).getObj());
            i.putExtra(getString(R.string.rest_info_instance_key), restInfoInstance);
            startActivity(i);
        }
    }
}
