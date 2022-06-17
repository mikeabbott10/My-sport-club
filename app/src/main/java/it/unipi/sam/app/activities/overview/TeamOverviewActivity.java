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
import it.unipi.sam.app.util.Constants;
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
        DebugUtility.LogDThis(DebugUtility.IDENTITY_LOG, TAG, "onCreate", null);

        teamCode = getIntent().getStringExtra(Constants.teamCode);
        if(teamCode ==null){
            DebugUtility.showSimpleSnackbar(binding.getRoot(), "No team selected, please go back.", 5000);
            return;
        }
        if(restInfoInstance == null) {
            DebugUtility.showSimpleSnackbar(binding.getRoot(), "Something went wrong, please retry later.", 5000);
            return;
        }
        thisTeamPartialPath = restInfoInstance.getTeamsPath() + teamCode;
        urlTeamBasePath = Constants.restBasePath + thisTeamPartialPath + "/";
        thisLastModifiedEntry = restInfoInstance.getLastModified().get(thisTeamPartialPath);

        startRequestsForPopulatingActivityLayout();

        teamInfoContentBinding = TeamInfoContentBinding.inflate(getLayoutInflater());
        // load cover image
        Glide
            .with(this)
            .load( getCoverImagePath(thisTeamPartialPath, thisLastModifiedEntry) )
            //.centerCrop()
            .placeholder(R.drawable.placeholder_126)
            .error(R.drawable.placeholder_126)
            .into(binding.toolbarLogo);
        binding.avatarImage.setImageResource(R.drawable.vc);
        binding.avatarImage.setBorderWidth(0);
        binding.avatarImage.setDisableCircularTransformation(true);

    }

    @Override
    protected void onStart() {
        super.onStart();
        DebugUtility.LogDThis(DebugUtility.IDENTITY_LOG, TAG, "onStart", null);

    }

    @Override
    protected void onResume() {
        super.onResume();
        DebugUtility.LogDThis(DebugUtility.IDENTITY_LOG, TAG, "onResume", null);
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
            teamInfoJsonPreference = SharedPreferenceUtility.getResourceUri(this, Constants.teams_key+teamCode+OVERVIEW_INFO_JSON,
                    (Long) riiMap.get(Constants.infoFile));
        }else{
            teamInfoJsonPreference = SharedPreferenceUtility.getResourceUri(this, Constants.teams_key+teamCode+OVERVIEW_INFO_JSON,
                    null);
        }

        if(teamInfoJsonPreference!=null && teamInfoJsonPreference.getUri()!=null) {
            DebugUtility.LogDThis(DebugUtility.SERVER_COMMUNICATION, TAG, "getTeam. From local", null);
            super.handleResponseUri(teamInfoJsonPreference.getDMResourceId(), OVERVIEW_INFO_JSON,
                    teamInfoJsonPreference.getUri(), teamInfoJsonPreference.getLastModifiedTimestamp(), false);
        }else {
            DebugUtility.LogDThis(DebugUtility.SERVER_COMMUNICATION, TAG, "getTeam. From net", null);
            enqueueRequest(
                    new DMRequestWrapper(urlTeamBasePath + restInfoInstance.getKeyWords().get(Constants.infoFile),
                            "randomTitle", "randomDescription", false, false, OVERVIEW_INFO_JSON,
                            false, null, null)
            );
        }
    }

    @Override
    public void onJsonInformationReceived(long dm_resource_id, String uri, Integer type, long lastModifiedTimestamp, boolean updateResourcePreference) {
        super.onJsonInformationReceived(dm_resource_id, uri, type, lastModifiedTimestamp, updateResourcePreference);
        if(teamInfoContentBinding != null && teamInfoContentBinding.getRoot().getParent() != null)
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
        String s = getString(R.string.coach_title) + t.getCoach().get(Constants.resource_name);
        binding.mainTextviewDescription.setText(s);

        if(teamInfoContentBinding==null)
            teamInfoContentBinding = TeamInfoContentBinding.inflate(getLayoutInflater());

        teamInfoContentBinding.leagueDescription.setText(t.getLeagueDescription());
        teamInfoContentBinding.leagueDescription.setObject(t.getLeagueLink());
        teamInfoContentBinding.leagueDescription.setOnClickListener(this);

        // players views
        for (int i=0; i<t.getPlayers().size(); i++) {
            String currPlayerPath = t.getPlayers().get(i).get(Constants.resource_path);
            assert currPlayerPath != null;
            if(!currPlayerPath.equals("")) {
                //String currPlayerName = t.getPlayers().get(i).get(Constants.resource_name);
                ParamImageView iv = new ParamImageView(this);
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
        String coachPath = t.getCoach().get(Constants.resource_path);
        assert coachPath != null;
        if(!coachPath.equals("")) {
            //String coachName = t.getCoach().get(Constants.resource_name);
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
        String secondCoachPath = t.getSecondCoach().get(Constants.resource_path);
        assert secondCoachPath != null;
        if(!secondCoachPath.equals("")) {
            //String secondCoachName = t.getSecondCoach().get(Constants.resource_name);
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
            String currManagerPath = t.getAssistantManager().get(i).get(Constants.resource_path);
            assert currManagerPath != null;
            if(!currManagerPath.equals("")) {
                //String currManagerName = t.getAssistantManager().get(i).get(Constants.resource_name);
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
            SharedPreferenceUtility.setResourceUri(this, Constants.teams_key+teamCode+type, uri, lastModifiedTimestamp, dm_resource_id);
    }

    @Override
    public void onClick(View view) {
        // teamInfoContentBinding.leagueDescription.getObj() is null here on API 25 emulator
        // on same API 25 emulator view!=teamInfoContentBinding.leagueDescription
        // but view.getId()==teamInfoContentBinding.leagueDescription.getId()
        // on the contrary of other used emulators and devices

        // DebugUtility.LogDThis(DebugUtility.TOUCH_OR_CLICK_RELATED_LOG, TAG, "teamInfoContentBinding.leagueDescription.getObj():"+teamInfoContentBinding.leagueDescription.getObj(), null);

        if(view.getId()==teamInfoContentBinding.leagueDescription.getId() && teamInfoContentBinding.leagueDescription.getObj()!=null){
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse((String) teamInfoContentBinding.leagueDescription.getObj())));
        }else if(view instanceof ParamImageView){
            Intent i = new Intent(this, PeopleOverviewActivity.class);
            i.putExtra(Constants.peopleCode, (String) ((ParamImageView) view).getObj());
            i.putExtra(Constants.rest_info_instance_key, restInfoInstance);
            startActivity(i);
        }
    }
}
