package it.unipi.sam.app.activities.overview;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;

import java.util.Map;

import it.unipi.sam.app.R;
import it.unipi.sam.app.activities.DownloadActivity;
import it.unipi.sam.app.databinding.ActivityOverviewBinding;
import it.unipi.sam.app.util.Constants;
import it.unipi.sam.app.util.DebugUtility;
import it.unipi.sam.app.util.MyBroadcastListener;
import it.unipi.sam.app.util.OverviewActivityAlphaHandler;

public class OverviewActivity extends DownloadActivity implements AppBarLayout.OnOffsetChangedListener, MyBroadcastListener, View.OnClickListener {
    private static final String TAG = "AAAOverviewActivity";
    protected ActivityOverviewBinding binding;
    protected float currentScrollingPercentage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOverviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.overviewToolbar.inflateMenu(R.menu.menu_main);

        binding.appBarLayout.addOnOffsetChangedListener(this);
        binding.backBtn.setOnClickListener(this);

        OverviewActivityAlphaHandler.startAlphaAnimation(binding.toolbarMainTextviewTitle, 0, View.INVISIBLE);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        currentScrollingPercentage = (float) Math.abs(offset) / (float) maxScroll;
        OverviewActivityAlphaHandler.handleAlphaOnTitle(binding.mainLinearlayoutTitle, currentScrollingPercentage);
        OverviewActivityAlphaHandler.handleToolbarTitleVisibility(binding.toolbarMainTextviewTitle, currentScrollingPercentage);
    }

    @Override
    protected void handleResponseUri(long dm_resource_id, Integer type, String uriString, long lastModifiedTimestamp, boolean updateResourcePreference) {
        super.handleResponseUri(dm_resource_id, type, uriString, lastModifiedTimestamp, updateResourcePreference);
        switch(type){
            case OVERVIEW_INFO_JSON:
                onJsonInformationReceived(dm_resource_id, uriString, type, lastModifiedTimestamp, updateResourcePreference);
                break;
        }
    }

    @Override
    protected void handle404(long dm_resource_id, Integer type, String uriString) {
        super.handle404(dm_resource_id, type, uriString);
        DebugUtility.showSimpleSnackbar(binding.getRoot(), "ERROR 09. Please retry later.", 5000);
    }

    // override this in activities
    public void onJsonInformationReceived(long dm_resource_id, String uri, Integer type, long lastModifiedTimestamp, boolean updateResourcePreference) {
        DebugUtility.LogDThis(DebugUtility.SERVER_COMMUNICATION, TAG, "onJsonInformationReceived. Info ready at: "+ uri, null);
        binding.infoPlaceholderImage.setVisibility(View.GONE);
    }

    /**
     * Ottieni URL dell'immagine di copertina aggiornata.
     * @param partialPath
     * @param lastModifiedEntry
     * @return
     */
    public String getCoverImagePath(String partialPath, Map<String, Object> lastModifiedEntry){
        String basePath = Constants.restBasePath + partialPath + "/";
        String imagePath = null;
        if(lastModifiedEntry!=null){
            imagePath = basePath + lastModifiedEntry.get(Constants.coverImage);
        }
        if(imagePath == null)
            imagePath = basePath + restInfoInstance.getKeyWords().get(Constants.coverImage);
        return imagePath;
    }

    /**
     * Ottieni URL dell'immagine di profilo aggiornata.
     * @param partialPath
     * @param lastModifiedEntry
     * @return
     */
    public String getProfileImagePath(String partialPath, Map<String, Object> lastModifiedEntry){
        String basePath = Constants.restBasePath + partialPath + "/";
        String imagePath = null;
        if(lastModifiedEntry!=null){
            imagePath = basePath + lastModifiedEntry.get(Constants.profileImage);
        }
        if(imagePath == null)
            imagePath = basePath + restInfoInstance.getKeyWords().get(Constants.profileImage);
        return imagePath;
    }

    @Override
    public void onClick(View view) {
        DebugUtility.LogDThis(DebugUtility.IDENTITY_LOG, TAG, view.toString(), null);
        if(view.getId() == binding.backBtn.getId()){
            this.onBackPressed();
        }
    }
}

