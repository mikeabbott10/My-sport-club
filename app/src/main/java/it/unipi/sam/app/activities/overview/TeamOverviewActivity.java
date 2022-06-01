package it.unipi.sam.app.activities.overview;

import android.os.Bundle;

import it.unipi.sam.app.R;
import it.unipi.sam.app.util.DMRequestWrapper;
import it.unipi.sam.app.util.DebugUtility;
import it.unipi.sam.app.util.ResourcePreferenceWrapper;
import it.unipi.sam.app.util.SharedPreferenceUtility;

public class TeamOverviewActivity extends OverviewActivity {
    private static final String TAG = "AAATeamOverviewActivity";
    private String urlBasePath = null;
    private String teamCode;

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(urlBasePath!=null || restInfoInstance == null)
            startRequestsForPopulatingActivityLayout();
    }

    private void startRequestsForPopulatingActivityLayout() {
        ResourcePreferenceWrapper coverImagePreference = SharedPreferenceUtility.getTeamResourceUri(this, teamCode, COVER_IMAGE);
        ResourcePreferenceWrapper avatarImagePreference = SharedPreferenceUtility.getTeamResourceUri(this, teamCode, AVATAR_IMAGE);
        ResourcePreferenceWrapper teamInfoJsonPreference = SharedPreferenceUtility.getTeamResourceUri(this, teamCode, INFO_JSON);

        if(coverImagePreference!=null && coverImagePreference.getUri()!=null)
            super.handleResponseUri(coverImagePreference.getDMResourceId(), COVER_IMAGE,
                    coverImagePreference.getUri(), coverImagePreference.getLastModifiedTimestamp(), false);
        else
            enqueueRequest(
                    new DMRequestWrapper(urlBasePath + restInfoInstance.getKeyWords().get(getString(R.string.cover_image)),
                            "randomTitle", "randomDescription", false, false, COVER_IMAGE,
                            false, null, null)
            );

        if(avatarImagePreference!=null && avatarImagePreference.getUri()!=null)
            super.handleResponseUri(avatarImagePreference.getDMResourceId(), AVATAR_IMAGE,
                    avatarImagePreference.getUri(), avatarImagePreference.getLastModifiedTimestamp(), false);
        else
            enqueueRequest(
                    new DMRequestWrapper(urlBasePath + restInfoInstance.getKeyWords().get(getString(R.string.profile_image)),
                            "randomTitle", "randomDescription", false, false, AVATAR_IMAGE,
                            false, null, null)
            );

        if(teamInfoJsonPreference!=null && teamInfoJsonPreference.getUri()!=null)
            super.handleResponseUri(teamInfoJsonPreference.getDMResourceId(), INFO_JSON,
                    teamInfoJsonPreference.getUri(), teamInfoJsonPreference.getLastModifiedTimestamp(), false);
        else
            enqueueRequest(
                    new DMRequestWrapper(urlBasePath + restInfoInstance.getKeyWords().get(getString(R.string.info_file)),
                            "randomTitle", "randomDescription", false, false, INFO_JSON,
                            false, null, null)
            );
    }

    @Override
    public void onCoverImageReceived(long dm_resource_id, String uri, Integer type, long lastModifiedTimestamp, boolean updateResourcePreference) {
        super.onCoverImageReceived(dm_resource_id, uri, type, lastModifiedTimestamp, updateResourcePreference);
        // update resource preference if needed
        if(updateResourcePreference)
            SharedPreferenceUtility.setTeamResourceUri(this, teamCode, type, uri, lastModifiedTimestamp, dm_resource_id);
    }

    @Override
    public void onAvatarImageReceived(long dm_resource_id, String uri, Integer type, long lastModifiedTimestamp, boolean updateResourcePreference) {
        super.onAvatarImageReceived(dm_resource_id, uri, type, lastModifiedTimestamp, updateResourcePreference);
        // update resource preference if needed
        if(updateResourcePreference)
            SharedPreferenceUtility.setTeamResourceUri(this, teamCode, type, uri, lastModifiedTimestamp, dm_resource_id);
    }

    @Override
    public void onJsonInformationReceived(long dm_resource_id, String uri, Integer type, long lastModifiedTimestamp, boolean updateResourcePreference) {
        super.onJsonInformationReceived(dm_resource_id, uri, type, lastModifiedTimestamp, updateResourcePreference);
        // todo: aggiungi team_activity_overview_content (popolato) alla cardview binding.info_container

        // update resource preference if needed
        if(updateResourcePreference)
            SharedPreferenceUtility.setTeamResourceUri(this, teamCode, type, uri, lastModifiedTimestamp, dm_resource_id);
    }
}
