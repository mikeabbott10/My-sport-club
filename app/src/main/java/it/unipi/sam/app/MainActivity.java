package it.unipi.sam.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.verify.domain.DomainVerificationManager;
import android.content.pm.verify.domain.DomainVerificationUserState;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import it.unipi.sam.app.activities.DownloadActivity;
import it.unipi.sam.app.activities.ScreenSlidePagerActivity;
import it.unipi.sam.app.databinding.ActivityMainBinding;
import it.unipi.sam.app.ui.favorites.RetriveFavoritesListener;
import it.unipi.sam.app.ui.favorites.RetriveFavoritesRunnable;
import it.unipi.sam.app.util.Constants;
import it.unipi.sam.app.util.DMRequestWrapper;
import it.unipi.sam.app.util.DebugUtility;
import it.unipi.sam.app.util.FavoritesWrapper;
import it.unipi.sam.app.util.ItemViewModel;
import it.unipi.sam.app.util.JacksonUtil;
import it.unipi.sam.app.util.ResourcePreferenceWrapper;
import it.unipi.sam.app.util.SharedPreferenceUtility;
import it.unipi.sam.app.util.VCNews;
import it.unipi.sam.app.util.room.AppDatabase;

//TODO:
// fixare preferiti (separatori notizie e squadre sminchiate)
// permettere aggiunta news ai preferiti dalle news stesse (heart next to share btn)
// v1.1. popola people activity con roba social related

public class MainActivity extends DownloadActivity implements SwipeRefreshLayout.OnRefreshListener, Observer<String>,
        DialogInterface.OnClickListener, CompoundButton.OnCheckedChangeListener, RetriveFavoritesListener, View.OnClickListener {
    private static final String TAG = "AAAAMainActivity";

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private CollapsingToolbarLayout toolBarLayout;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private VCNews[] vcn_arr;
    public List<VCNews> vc_news = new ArrayList<>();
    private AlertDialog domainApprovationDialog;
    private ItemViewModel viewModel;

    // room
    public AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DebugUtility.LogDThis(DebugUtility.IDENTITY_LOG, TAG, "onCreate()", null);

        // set day/night mode
        boolean isNightMode = SharedPreferenceUtility.getNightMode(this);
        if(isNightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // get custom instance state or original instance state
        boolean alreadyAskedForDomainVerification = false;
        Bundle manuallyCreatedInstanceState = getIntent().getBundleExtra(Constants.manually_saved_instance_state);
        if(manuallyCreatedInstanceState != null){
            super.onCreate(manuallyCreatedInstanceState);
            restInfoInstance = manuallyCreatedInstanceState.getParcelable(Constants.rest_info_instance_key);
            alreadyAskedForDomainVerification = manuallyCreatedInstanceState.getBoolean(Constants.already_asked_for_domain_approvation_this_time);
        }else if(savedInstanceState!=null){
            super.onCreate(savedInstanceState);
            restInfoInstance = savedInstanceState.getParcelable(Constants.rest_info_instance_key);
            alreadyAskedForDomainVerification = savedInstanceState.getBoolean(Constants.already_asked_for_domain_approvation_this_time);
        }else
            super.onCreate(null);

        // set binding, view and actionbar
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);

        toolBarLayout = binding.appBarMain.toolbarLayout;
        toolBarLayout.setTitle(getString(R.string.menu_notizie));

        // set day/night mode
        SwitchCompat drawerSwitch = binding.navView.getMenu().findItem(R.id.nav_nightmode).getActionView().findViewById(R.id.nightmode_switch);
        drawerSwitch.setChecked(isNightMode);
        drawerSwitch.setOnCheckedChangeListener(this);

        // set notification enabled/disabled
        if(SharedPreferenceUtility.getNotificationEnabled(this)){
            // notifiche abilitate
            binding.appBarMain.fab.setImageResource(R.drawable.ic_no_notification);
        }else{
            binding.appBarMain.fab.setImageResource(R.drawable.ic_active_notification);
        }
        binding.appBarMain.fab.setOnClickListener(this);

        // ask for restInfo
        // rest info file is always downloaded at least once
        if(restInfoInstance==null){
            getRestInfoFile(new DMRequestWrapper(Constants.restBasePath + Constants.firstRestReqPath,
                    "notUseful", "notUseful", false, false, REST_INFO_JSON,
                    false, null, null));
            // start refreshing status
            binding.appBarMain.swiperefresh.setRefreshing(true);
        }

        // navigation view colors
        setNavigationViewColors();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_notizie, R.id.nav_favoriti, R.id.nav_femminile, R.id.nav_maschile,
                R.id.nav_contatti, R.id.nav_settings)
                .setOpenableLayout(binding.drawerLayout)
                .build();
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment_content_main);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        navController.setGraph(R.navigation.mobile_navigation);
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        viewModel.getSelectedFragmentName().observe(this, this);

        // pull-to-refresh
        binding.appBarMain.swiperefresh.setOnRefreshListener(this);

        // domain verification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alreadyAskedForDomainVerification) {
            try {
                boolean isDomainVerified = isDomainVerified();
                DebugUtility.LogDThis(DebugUtility.IDENTITY_LOG, TAG, "isdomainverified: "+isDomainVerified, null);
                DebugUtility.LogDThis(DebugUtility.IDENTITY_LOG, TAG, "don't ask for domain verification: "+SharedPreferenceUtility.getDontAskForDomainVerification(this), null);
                if(!SharedPreferenceUtility.getDontAskForDomainVerification(this) && !isDomainVerified){
                    requestDomainApprovation();
                }else if(isDomainVerified)
                    SharedPreferenceUtility.setDontAskForDomainVerification(this, true);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void setNavigationViewColors() {
        ColorStateList text_csl = new ColorStateList(
                new int[][] {
                        new int[] {-android.R.attr.state_checked}, // unchecked
                        new int[] { android.R.attr.state_checked}  // checked
                },
                new int[] {
                        ContextCompat.getColor(this, R.color.customDarkLightIconColor), // unchecked
                        ContextCompat.getColor(this, R.color.customDarkLightIconColor) // checked
                }
        );
        ColorStateList icon_csl = new ColorStateList(
                new int[][] {
                        new int[] {-android.R.attr.state_checked}, // unchecked
                        new int[] { android.R.attr.state_checked}  // checked
                },
                new int[] {
                        ContextCompat.getColor(this, R.color.middleAlphaCustomDarkLightIconColor), // unchecked
                        ContextCompat.getColor(this, R.color.customDarkLightIconColor) // checked
                }
        );
        binding.navView.setItemTextColor(text_csl);
        binding.navView.setItemIconTintList(icon_csl);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DebugUtility.LogDThis(DebugUtility.IDENTITY_LOG, TAG, "onStart()", null);

        // ROOM
        db = AppDatabase.getDatabase(getApplicationContext());
        // launch thread to retrive favorites from db
        // (always do it because of possible back navigation to this after favorites list changes)
        new Thread(new RetriveFavoritesRunnable(this, db)).start();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.rest_info_instance_key, restInfoInstance);
        outState.putBoolean(Constants.already_asked_for_domain_approvation_this_time, true);
    }

    private void getRestInfoFile(DMRequestWrapper dmRequestWrapper) {
        enqueueRequest(dmRequestWrapper);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    protected void handleResponseUri(long dm_resource_id, Integer type, String uriString, long lastModifiedTimestamp, boolean updateResourcePreference) {
        super.handleResponseUri(dm_resource_id, type, uriString, lastModifiedTimestamp, updateResourcePreference);
        switch(type){
            case NEWS_JSON:
                // DebugUtility.LogDThis(DebugUtility.SERVER_COMMUNICATION, TAG, "handleResponseUri. NEWS_JSON", null);
                // update resource preference if needed
                if(updateResourcePreference)
                    SharedPreferenceUtility.setResourceUri(this, Constants.news_key+type, uriString, lastModifiedTimestamp, dm_resource_id);

                if(!updateResourcePreference && vc_news.size()>0){
                    // se non devo salvare la Preference e vc_news è già una lista con elementi,
                    // allora è inutile aggiornare vc_news con sè stessa
                    binding.appBarMain.swiperefresh.setRefreshing(false);
                    break;
                }
                String content = getFileContentFromUri(uriString);
                // perform jackson from file to object
                try {
                    vcn_arr = (VCNews[]) JacksonUtil.getObjectFromString(content, VCNews[].class);
                    vc_news = new ArrayList<>(Arrays.asList(vcn_arr));
                    DebugUtility.LogDThis(DebugUtility.SERVER_COMMUNICATION, TAG, "handleResponseUri. vc_news:" + vc_news, null);

                    Long calledNewsId = getCalledNewsIdFromIntent(getIntent());
                    if(calledNewsId!=null) {
                        if(restInfoInstance==null){
                            Snackbar.make(binding.getRoot(), "ERROR 05. Please retry later.", 5000).show();
                            return;
                        }
                        Intent i = new Intent(this, ScreenSlidePagerActivity.class);
                        try {
                            i.putExtra(Constants.news_key, (ArrayList<VCNews>) vc_news);
                        } catch (ClassCastException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "ERROR 03. Retry later.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        i.putExtra(Constants.news_id_key, calledNewsId);
                        i.putExtra(Constants.rest_info_instance_key, restInfoInstance);
                        startActivity(i);
                    }
                    // populate news in news fragment
                    Collections.sort(vc_news); // date sort
                    viewModel.setVcNewsList(vc_news);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    DebugUtility.showSimpleSnackbar(binding.getRoot(), "ERROR 00. Please retry later.", 5000);
                }
                binding.appBarMain.swiperefresh.setRefreshing(false);
                break;
            case REST_INFO_JSON:
                if(restInfoInstance!=null) {
                    // after swipe to refresh
                    getVolleyCecinaNews();
                    if(!restInfoInstance.getFemaleTeamTags().equals(viewModel.getFemaleTeamsList().getValue()))
                        viewModel.setFemaleTeamsList( restInfoInstance.getFemaleTeamTags() );
                    if(!restInfoInstance.getMaleTeamTags().equals(viewModel.getFemaleTeamsList().getValue()))
                        viewModel.setMaleTeamsList( restInfoInstance.getMaleTeamTags() );
                }else
                    binding.appBarMain.swiperefresh.setRefreshing(false);

                break;
        }
    }

    @Override
    protected void handle404(long dm_resource_id, Integer type, String uriString) {
        super.handle404(dm_resource_id, type, uriString);
        if(type==null){
            DebugUtility.showSimpleSnackbar(binding.getRoot(), "ERROR 10. Please retry later.", 5000);
            binding.appBarMain.swiperefresh.setRefreshing(false);
            return;
        }
        switch(type) {
            case NEWS_JSON:
                DebugUtility.showSimpleSnackbar(binding.getRoot(), "ERROR 07. Please retry later.", 5000);
                break;
            case REST_INFO_JSON:
                DebugUtility.showSimpleSnackbar(binding.getRoot(), "ERROR 08. Please retry later.", 5000);
                break;
        }
        binding.appBarMain.swiperefresh.setRefreshing(false);
    }

    private void getVolleyCecinaNews() {
        Map<String, Object> riiMap = restInfoInstance.getLastModified().get( Constants.news_key );
        ResourcePreferenceWrapper newsJsonPreference = null;
        if(riiMap!=null)
            newsJsonPreference = SharedPreferenceUtility.getResourceUri(this, Constants.news_key+NEWS_JSON, (Long) riiMap.get( Constants.news_key ));

        DebugUtility.LogDThis(DebugUtility.SERVER_COMMUNICATION, TAG, "getVolleyCecinaNews. newsJsonPreference:"+newsJsonPreference, null);
        if(newsJsonPreference!=null && newsJsonPreference.getUri()!=null) {
            DebugUtility.LogDThis(DebugUtility.SERVER_COMMUNICATION, TAG, "getVolleyCecinaNews. From local", null);
            handleResponseUri(newsJsonPreference.getDMResourceId(), NEWS_JSON,
                    newsJsonPreference.getUri(), newsJsonPreference.getLastModifiedTimestamp(), false);
        }else {
            DebugUtility.LogDThis(DebugUtility.SERVER_COMMUNICATION, TAG, "getVolleyCecinaNews. From net", null);
            enqueueRequest(
                    new DMRequestWrapper(Constants.restBasePath + restInfoInstance.getNews(),
                            "randomTitle", "randomDescription", false, false, NEWS_JSON,
                            false, null, null)
            );
        }
    }

    /**
     * Swipe to refresh listener
     */
    @Override
    public void onRefresh() {
        // ask for rest info
        Log.d(TAG, "onRefresh()");
        getRestInfoFile(new DMRequestWrapper(Constants.restBasePath + Constants.firstRestReqPath,
                "notUseful", "notUseful", false, false, REST_INFO_JSON,
                false, null, null));
    }

    /**
     * Get news id from the intent.
     *
     * @param intent : the intent
     * @return String[] : an array of 3 strings: [code, randomFactor, region]
     */
    protected Long getCalledNewsIdFromIntent(@Nullable Intent intent) {
        if(intent==null) return null;
        String action = intent.getAction();
        Uri data = intent.getData();
        if (action != null && action.equals("android.intent.action.VIEW") && data != null) {
            String scheme = data.getScheme(); // "https"
            String host = data.getHost(); // "volleycecina.it"
            List<String> params = data.getPathSegments();
            if (host != null && scheme != null && params != null &&
                    scheme.equals("https") && host.equals("volleycecina.it")) {
                String paramKey = params.get(0); // "news"
                String paramValue = params.get(1); // (int) news_id
                if(paramKey!=null && paramValue!=null && !(paramValue.trim()).equals("")) {
                    DebugUtility.LogDThis(DebugUtility.IDENTITY_LOG, TAG,
                            "DeepLink key: " + paramKey + "DeepLink value:" + paramValue, this);
                    if(paramKey.equals("news")){
                        return Long.parseLong(paramValue);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Callback on String item changes
     * @param item
     */
    @Override
    public void onChanged(String item) {
        // Perform an action with the latest item data
        toolBarLayout.setTitle(item);
        binding.appBarMain.swiperefresh.setEnabled(true);
        if(item.equals(getString(R.string.menu_notizie))){
            if(SharedPreferenceUtility.getNotificationEnabled(this)){
                // notifiche abilitate
                binding.appBarMain.fab.setImageResource(R.drawable.ic_no_notification);
            }else{
                binding.appBarMain.fab.setImageResource(R.drawable.ic_active_notification);
            }
            binding.appBarMain.fab.setVisibility(View.VISIBLE);
            return;
        }

        if(item.equals(getString(R.string.menu_favoriti))) {
            binding.appBarMain.swiperefresh.setEnabled(false);
        }else if(item.equals(getString(R.string.menu_contatti))) {
            binding.appBarMain.swiperefresh.setEnabled(false);
        }else if(item.equals(getString(R.string.menu_femminile))) {
            binding.appBarMain.swiperefresh.setEnabled(false);
        }else if(item.equals(getString(R.string.menu_maschile))) {
            binding.appBarMain.swiperefresh.setEnabled(false);
        }else if(item.equals(getString(R.string.menu_settings))){
            binding.appBarMain.swiperefresh.setEnabled(false);
        }else if(item.equals(getString(R.string.menu_avanzate))) {
            binding.appBarMain.swiperefresh.setEnabled(false);
        }

        binding.appBarMain.fab.setVisibility(View.GONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void requestDomainApprovation() {
        View v = getLayoutInflater().inflate(R.layout.dialog_show_again, null);
        CheckBox cb = v.findViewById(R.id.checkBox);
        cb.setOnCheckedChangeListener(this);
        domainApprovationDialog = new AlertDialog.Builder(MainActivity.this)
                .setMessage(R.string.approvazione_dominio)
                .setTitle(getString(R.string.domain_approvation))
                .setView(v) // add the checkbox
                .setCancelable(false)
                .setPositiveButton("", this)
                .setNegativeButton("", this)
                .setPositiveButtonIcon(AppCompatResources.getDrawable(this, R.drawable.ic_ok_adaptive_foreground))
                .setNegativeButtonIcon(AppCompatResources.getDrawable(this, R.drawable.ic_close_adaptive_foreground))
                .create();
        domainApprovationDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(domainApprovationDialog!=null)
            domainApprovationDialog.dismiss();
    }

    /**
     * Dialog buttons listener
     * @param dialogInterface
     * @param which
     */
    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        if(which==AlertDialog.BUTTON_POSITIVE) {
            startActivity(new Intent(Settings.ACTION_APP_OPEN_BY_DEFAULT_SETTINGS,
                    Uri.parse("package:" + getPackageName())));
        }
        dialogInterface.cancel();
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private boolean isDomainVerified() throws PackageManager.NameNotFoundException {
        DomainVerificationManager manager =
                getSystemService(DomainVerificationManager.class);
        DomainVerificationUserState userState =
                manager.getDomainVerificationUserState(getPackageName());

        Map<String, Integer> hostToStateMap = userState.getHostToStateMap();
        for (String key : hostToStateMap.keySet()) {
            Integer stateValue = hostToStateMap.get(key);
            //DebugUtility.LogDThis(DebugUtility.IDENTITY_LOG, TAG, key + "->" + stateValue, null);
            if (stateValue!=null && stateValue == DomainVerificationUserState.DOMAIN_STATE_VERIFIED) {
                // Domain has passed Android App Links verification.
                if(key.equals("volleycecina.it") || key.equals("www.volleycecina.it")){
                    return true;
                }
            } else if (stateValue!=null && stateValue == DomainVerificationUserState.DOMAIN_STATE_SELECTED) {
                // Domain hasn't passed Android App Links verification, but the user has
                // associated it with an app.
                if(key.equals("volleycecina.it") || key.equals("www.volleycecina.it")){
                    return true;
                }
            } else {
                // All other domains.
            }
        }
        return false;
    }

    /**
     * Listen to the changes of the checkbox inside AlertDialog and the nightmode switch
     * @param compoundButton
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if(compoundButton.getId() == R.id.nightmode_switch){
            if (isChecked) {
                SharedPreferenceUtility.setNightMode( this, true);
            } else {
                SharedPreferenceUtility.setNightMode( this, false);
            }
            recreate();
        }else {
            SharedPreferenceUtility.setDontAskForDomainVerification(this, compoundButton.isChecked());
        }
    }

    // RetriveFavoritesListener implementation
    @Override
    public void onFavoritesRetrived(List<FavoritesWrapper> favorites) {
        viewModel.setFavoritesList(favorites);
        DebugUtility.LogDThis(DebugUtility.IDENTITY_LOG, TAG, "onFavoritesRetrived()", null);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fab){
            boolean wasNotificationEnabled = SharedPreferenceUtility.getNotificationEnabled(this);
            SharedPreferenceUtility.setNotificationEnabled( this, !wasNotificationEnabled);
            if(wasNotificationEnabled){
                // ora switch to no notification
                binding.appBarMain.fab.setImageResource(R.drawable.ic_active_notification);
            }else{
                // ora switch to active notification
                binding.appBarMain.fab.setImageResource(R.drawable.ic_no_notification);
            }
        }
    }
}