package it.unipi.sam.app;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;

import it.unipi.sam.app.activities.DownloadActivity;
import it.unipi.sam.app.activities.overview.TeamOverviewActivity;
import it.unipi.sam.app.databinding.ActivityMainBinding;
import it.unipi.sam.app.util.DMRequestWrapper;
import it.unipi.sam.app.util.DebugUtility;
import it.unipi.sam.app.util.ItemViewModel;
import it.unipi.sam.app.util.ResourcePreferenceWrapper;
import it.unipi.sam.app.util.SharedPreferenceUtility;

//TODO:
// 2. Se serve, usare preferenceFragment per impostazioni varie (salvataggio come shared preferences)

public class MainActivity extends DownloadActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{
    private static final String TAG = "AAAAMainActivity";

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private CollapsingToolbarLayout toolBarLayout;

    private ItemViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);

        toolBarLayout = binding.appBarMain.toolbarLayout;
        toolBarLayout.setTitle(getString(R.string.menu_notizie));

        // ask for rest info
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
                R.id.nav_notizie, R.id.nav_femminile, R.id.nav_maschile, R.id.nav_contatti)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        viewModel.getSelectedItem().observe(this, item -> {
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
        });
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
        // todo: doesn't work
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
            i.putExtra(getString(R.string.team_code), restInfoInstance.getTeamCodes()[2]);
            i.putExtra(getString(R.string.rest_info_instance_key), restInfoInstance);
            startActivity(i);
        }
    }

    @Override
    protected void handleResponseUri(long dm_resource_id, Integer type, String uriString, long lastModifiedTimestamp, boolean updateResourcePreference) {
        super.handleResponseUri(dm_resource_id, type,uriString, lastModifiedTimestamp, updateResourcePreference);
        switch(type){
            case NEWS_JSON:
                // todo: populate news in news fragment

                // update resource preference if needed
                if(updateResourcePreference)
                    SharedPreferenceUtility.setResourceUri(this, type, uriString, lastModifiedTimestamp, dm_resource_id);
                break;
        }
    }
}