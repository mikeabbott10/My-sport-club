package it.unipi.sam.app.activities.overview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.android.material.appbar.AppBarLayout;

import java.util.Map;

import it.unipi.sam.app.R;
import it.unipi.sam.app.databinding.TeamInfoContentBinding;
import it.unipi.sam.app.ui.favorites.RetriveFavoritesRunnable;
import it.unipi.sam.app.ui.favorites.SetFavoritesRunnable;
import it.unipi.sam.app.util.Constants;
import it.unipi.sam.app.util.DMRequestWrapper;
import it.unipi.sam.app.util.DebugUtility;
import it.unipi.sam.app.util.FavoritesWrapper;
import it.unipi.sam.app.util.JacksonUtil;
import it.unipi.sam.app.util.OverviewActivityAlphaHandler;
import it.unipi.sam.app.util.ResourcePreferenceWrapper;
import it.unipi.sam.app.util.SharedPreferenceUtility;
import it.unipi.sam.app.util.Team;
import it.unipi.sam.app.util.graphics.ParamImageView;
import it.unipi.sam.app.util.graphics.ParamTextView;
import it.unipi.sam.app.util.room.AppDatabase;

public class TeamOverviewActivity extends OverviewActivity implements View.OnClickListener {
    private static final String TAG = "AAATeamOverviewActivity";
    private String urlTeamBasePath = null;
    private String teamCode;
    protected TeamInfoContentBinding teamInfoContentBinding;
    private String thisTeamPartialPath;

    private AppDatabase db;
    private Team thisTeam;

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
        Map<String, Object> thisLastModifiedEntry = restInfoInstance.getLastModified().get(thisTeamPartialPath);


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

        // ROOM
        db = AppDatabase.getDatabase(getApplicationContext());

        startRequestsForPopulatingActivityLayout();
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
        try {
            thisTeam = (Team) JacksonUtil.getObjectFromString(content, Team.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            DebugUtility.showSimpleSnackbar(binding.getRoot(), "ERROR 01. Please retry later.", 5000);
            return;
        }
        binding.toolbarMainTextviewTitle.setText(thisTeam.getCurrentLeague());
        binding.mainTextviewTitle.setText(thisTeam.getCurrentLeague());
        String s = getString(R.string.coach_title) + thisTeam.getCoach().get(Constants.resource_name);
        binding.mainTextviewDescription.setText(s);

        if(teamInfoContentBinding==null)
            teamInfoContentBinding = TeamInfoContentBinding.inflate(getLayoutInflater());

        teamInfoContentBinding.leagueDescription.setText(thisTeam.getLeagueDescription());
        teamInfoContentBinding.leagueDescription.setObject(thisTeam.getLeagueLink());
        teamInfoContentBinding.leagueDescription.setOnClickListener(this);

        // players views
        for (int i = 0; i< thisTeam.getPlayers().size(); i++) {
            String currPlayerPath = thisTeam.getPlayers().get(i).get(Constants.resource_path);
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
        String coachPath = thisTeam.getCoach().get(Constants.resource_path);
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
        String secondCoachPath = thisTeam.getSecondCoach().get(Constants.resource_path);
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
        for (int i = 0; i< thisTeam.getAssistantManager().size(); i++) {
            String currManagerPath = thisTeam.getAssistantManager().get(i).get(Constants.resource_path);
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

        // set favorites icon
        new Thread(new RetriveFavoritesRunnable(favorites -> {
            if(favorites.contains(new FavoritesWrapper(thisTeam))){
                binding.overviewToolbar.getMenu().getItem(0).setIcon(R.drawable.ic_filled_red_heart);
            }else
                binding.overviewToolbar.getMenu().getItem(0).setIcon(R.drawable.ic_empty_heart);
        }, db)).start();

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

        // DebugUtility.LogDThis(DebugUtility.TOUCH_OR_CLICK_RELATED_LOG, TAG, "teamInfoContentBinding.leagueDescription.getObj():"+((ParamTextView) view).getObj(), null);
        super.onClick(view);
        if(view.getId()==teamInfoContentBinding.leagueDescription.getId() && ((ParamTextView) view).getObj()!=null){
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse((String) ((ParamTextView) view).getObj())));
        }else if(view instanceof ParamImageView){
            Intent i = new Intent(this, PeopleOverviewActivity.class);
            i.putExtra(Constants.peopleCode, (String) ((ParamImageView) view).getObj());
            i.putExtra(Constants.rest_info_instance_key, restInfoInstance);
            startActivity(i);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if(item.getItemId()==R.id.menu_fav){
            // add or remove from favorites
            if(thisTeam!=null)
                new Thread(
                        new SetFavoritesRunnable(db,
                                new FavoritesWrapper(thisTeam),
                                this, item
                        )
                ).start();
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
