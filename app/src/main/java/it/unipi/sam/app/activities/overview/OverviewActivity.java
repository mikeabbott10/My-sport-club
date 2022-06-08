package it.unipi.sam.app.activities.overview;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;

import it.unipi.sam.app.R;
import it.unipi.sam.app.activities.DownloadActivity;
import it.unipi.sam.app.databinding.ActivityOverviewBinding;
import it.unipi.sam.app.util.MyBroadcastListener;
import it.unipi.sam.app.util.OverviewActivityAlphaHandler;

public class OverviewActivity extends DownloadActivity implements AppBarLayout.OnOffsetChangedListener, MyBroadcastListener {
    private static final String TAG = "AAAOverviewActivity";
    protected ActivityOverviewBinding binding;
    protected float currentScrollingPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOverviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.appBarLayout.addOnOffsetChangedListener(this);

        binding.overviewToolbar.inflateMenu(R.menu.menu_main);

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

    // override this in activities (call super)
    public void onJsonInformationReceived(long dm_resource_id, String uri, Integer type, long lastModifiedTimestamp, boolean updateResourcePreference) {
        //DebugUtility.LogDThis(DebugUtility.SERVER_COMMUNICATION, TAG, "onJsonInformationReceived. Info ready at: "+ uri, null);
        binding.infoPlaceholderImage.setVisibility(View.GONE);
    }
}

