package it.unipi.sam.app;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.verify.domain.DomainVerificationManager;
import android.content.pm.verify.domain.DomainVerificationUserState;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.ImageViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import it.unipi.sam.app.activities.DownloadActivity;
import it.unipi.sam.app.activities.ScreenSlidePagerActivity;
import it.unipi.sam.app.activities.overview.TeamOverviewActivity;
import it.unipi.sam.app.databinding.ActivityMainBinding;
import it.unipi.sam.app.ui.news.NewsViewModel;
import it.unipi.sam.app.util.DMRequestWrapper;
import it.unipi.sam.app.util.DebugUtility;
import it.unipi.sam.app.util.ItemViewModel;
import it.unipi.sam.app.util.JacksonUtil;
import it.unipi.sam.app.util.ResourcePreferenceWrapper;
import it.unipi.sam.app.util.SharedPreferenceUtility;
import it.unipi.sam.app.util.VCNews;

//TODO:
// 1. completare pagina teams
// 2. fare pagina contatti
// 3. inserire in settore maschile/femminile le squadre
// 4. implementare preferiti

public class MainActivity extends DownloadActivity implements NavigationView.OnNavigationItemSelectedListener,
                                                View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, Observer<ClipData.Item>,
                                                DialogInterface.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "AAAAMainActivity";

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private CollapsingToolbarLayout toolBarLayout;

    private ItemViewModel viewModel;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private VCNews[] vcn_arr;
    private List<VCNews> vc_news = null;
    private NewsViewModel newsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ask for rest info

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);

        toolBarLayout = binding.appBarMain.toolbarLayout;
        toolBarLayout.setTitle(getString(R.string.menu_notizie));

        getRestInfoFile(new DMRequestWrapper(getString(R.string.restBasePath) + getString(R.string.first_rest_req_path),
                "notUseful", "notUseful", false, false, REST_INFO_JSON,
                false, null, null));

        binding.navView.setNavigationItemSelectedListener(this);
        binding.appBarMain.fab.setOnClickListener(this);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_notizie, R.id.nav_femminile, R.id.nav_maschile, R.id.nav_contatti, R.id.nav_settings)
                .setOpenableLayout(drawer)
                .build();
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment_content_main);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);

        viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        viewModel.getSelectedItem().observe(this, this);

        // pull-to-refresh
        binding.appBarMain.swiperefresh.setOnRefreshListener(this);

        // domain verification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
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

    private void getRestInfoFile(DMRequestWrapper dmRequestWrapper) {
        // rest info file is always downloaded
        enqueueRequest(dmRequestWrapper);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // TODO: doesn't work
        if(item.getItemId()==R.id.emailTextView) {
            // intent to mail apps
            DebugUtility.LogDThis(DebugUtility.TOUCH_OR_CLICK_RELATED_LOG, TAG, "email selected", null);
        }else if(item.getItemId()==R.id.indirizzoTextView){
            // intent to map apps
        }else if(item.getItemId()==R.id.logoView){
            // animate logo
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fab){
            // TODO: togliere start activity
            Intent i = new Intent(this, TeamOverviewActivity.class);
            i.putExtra(getString(R.string.team_code), restInfoInstance.getTeamCodes()[1]);
            i.putExtra(getString(R.string.rest_info_instance_key), restInfoInstance);
            startActivity(i);
        }
    }

    @Override
    protected void handleResponseUri(long dm_resource_id, Integer type, String uriString, long lastModifiedTimestamp, boolean updateResourcePreference) {
        super.handleResponseUri(dm_resource_id, type, uriString, lastModifiedTimestamp, updateResourcePreference);
        switch(type){
            case NEWS_JSON:
                // DebugUtility.LogDThis(DebugUtility.SERVER_COMMUNICATION, TAG, "handleResponseUri. NEWS_JSON", null);
                // update resource preference if needed
                if(updateResourcePreference)
                    SharedPreferenceUtility.setResourceUri(this, getString(R.string.news)+type, uriString, lastModifiedTimestamp, dm_resource_id);

                if(!updateResourcePreference && vc_news!=null){
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
                        Intent i = new Intent(this, ScreenSlidePagerActivity.class);
                        try {
                            i.putExtra(getString(R.string.news), (ArrayList<VCNews>) vc_news);
                        } catch (ClassCastException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "ERROR 03. Retry later.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        i.putExtra(getString(R.string.news_id), calledNewsId);
                        i.putExtra(getString(R.string.rest_info_instance_key), restInfoInstance);
                        startActivity(i);
                    }
                    // populate news in news fragment
                    Collections.sort(vc_news); // date sort
                    newsViewModel.setVcNewsList(vc_news);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    DebugUtility.showSimpleSnackbar(binding.getRoot(), "ERROR 00. Please retry later.", 5000);
                }
                binding.appBarMain.swiperefresh.setRefreshing(false);
                break;
            case REST_INFO_JSON:
                if(restInfoInstance!=null) {
                    getVolleyCecinaNews();
                }else
                    binding.appBarMain.swiperefresh.setRefreshing(false);
                break;
        }
    }

    private void getVolleyCecinaNews() {
        Map<String, Long> riiMap = restInfoInstance.getLastModifiedTimestamp().get( getString(R.string.news) );
        ResourcePreferenceWrapper newsJsonPreference = null;
        if(riiMap!=null)
            newsJsonPreference = SharedPreferenceUtility.getResourceUri(this, getString(R.string.news)+NEWS_JSON, riiMap.get( getString(R.string.news) ));

        if(newsJsonPreference!=null && newsJsonPreference.getUri()!=null) {
            DebugUtility.LogDThis(DebugUtility.SERVER_COMMUNICATION, TAG, "getVolleyCecinaNews. From local", null);
            handleResponseUri(newsJsonPreference.getDMResourceId(), NEWS_JSON,
                    newsJsonPreference.getUri(), newsJsonPreference.getLastModifiedTimestamp(), false);
        }else {
            DebugUtility.LogDThis(DebugUtility.SERVER_COMMUNICATION, TAG, "getVolleyCecinaNews. From net", null);
            enqueueRequest(
                    new DMRequestWrapper(getString(R.string.restBasePath) + restInfoInstance.getNews(),
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
        getRestInfoFile(new DMRequestWrapper(getString(R.string.restBasePath) + getString(R.string.first_rest_req_path),
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
     * Callback on ClipData.Item item changes
     * @param item
     */
    @Override
    public void onChanged(ClipData.Item item) {
        // Perform an action with the latest item data
        toolBarLayout.setTitle(item.getText());

        if(item.getText().equals(getString(R.string.menu_contatti))){
            // show fab
            //binding.appBarMain.fab.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            ImageViewCompat.setImageTintList(
                    binding.appBarMain.fab,
                    ColorStateList.valueOf(Color.BLACK)
            );
            binding.appBarMain.fab.setImageResource(android.R.drawable.ic_dialog_email);
            binding.appBarMain.fab.setVisibility(View.VISIBLE);
        }else if(item.getText().equals(getString(R.string.menu_notizie))){
            //binding.appBarMain.fab.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.main_color));
            ImageViewCompat.setImageTintList(
                    binding.appBarMain.fab,
                    ColorStateList.valueOf(Color.RED)
            );
            binding.appBarMain.fab.setImageResource(R.drawable.ic_filled_red_heart);
            binding.appBarMain.fab.setVisibility(View.VISIBLE);
        }else{
            binding.appBarMain.fab.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void requestDomainApprovation() {
        @SuppressLint("InflateParams")
        View v = getLayoutInflater().inflate(R.layout.dialog_show_again, null);
        CheckBox cb = v.findViewById(R.id.checkBox);
        cb.setOnCheckedChangeListener(this);
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(R.string.approvazione_dominio)
                .setTitle("Approvazione del dominio")
                .setView(v) // add the
                .setCancelable(false)
                .setPositiveButton("", this)
                .setNegativeButton("", this)
                .setPositiveButtonIcon(AppCompatResources.getDrawable(this, R.drawable.ic_ok_adaptive_foreground))
                .setNegativeButtonIcon(AppCompatResources.getDrawable(this, R.drawable.ic_close_adaptive_foreground))
                .create()
                .show();
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
     * Listen to the changes of the checkbox inside AlertDialog
     * @param compoundButton
     * @param b
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        SharedPreferenceUtility.setDontAskForDomainVerification(this, compoundButton.isChecked());
    }
}