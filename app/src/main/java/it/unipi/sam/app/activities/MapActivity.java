package it.unipi.sam.app.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import it.unipi.sam.app.R;
import it.unipi.sam.app.databinding.ActivityMapBinding;
import it.unipi.sam.app.util.Constants;
import it.unipi.sam.app.util.SharedPreferenceUtility;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {
    private static final int NO_PERMISSIONS = 0;
    private static final int PERMISSIONS_NO_LOCATION = 1;

    ActivityMapBinding binding;
    private LatLng ll;

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private GoogleMap globalGoogleMapInstance = null;
    private int locationEnabled = NO_PERMISSIONS;
    private final Object mapLock = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        // Set the layout file as the content view.
        setContentView(binding.getRoot());

        ll = getIntent().getParcelableExtra(Constants.lat_lon_marker_key);

        // Get a handle to the fragment and register the callback.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        requestLocationPermission();
    }

    // Get a handle to the GoogleMap object and display marker.
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // marker
        googleMap.addMarker(new MarkerOptions()
                .position(ll)
                .title("A.S.D. Volley Cecina"));

        // Set the map type to Hybrid.
        //googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            if(SharedPreferenceUtility.getNightMode(this))
                googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                this, R.raw.map_night_style));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        // map settings
        UiSettings mapSettings = googleMap.getUiSettings();
        mapSettings.setZoomControlsEnabled(true);
        mapSettings.setAllGesturesEnabled(true);
        mapSettings.setCompassEnabled(true);

        // Move the camera to the map coordinates and zoom in closer.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll,15));

        synchronized (mapLock) {
            if (locationEnabled == PERMISSIONS_NO_LOCATION) {
                globalGoogleMapInstance.setMyLocationEnabled(true);
                globalGoogleMapInstance.setOnMyLocationButtonClickListener(this);
                globalGoogleMapInstance.setOnMyLocationClickListener(this);
            } else // still no permission
                globalGoogleMapInstance = googleMap;
        }
    }

    // Location
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(LOCATION_PERMISSION_REQUEST_CODE)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if(EasyPermissions.hasPermissions(this, perms)) {
            //Snackbar.make(binding.getRoot(), "Permission granted", 2000).show();
            synchronized (mapLock) {
                if (globalGoogleMapInstance != null) {
                    globalGoogleMapInstance.setMyLocationEnabled(true);
                    globalGoogleMapInstance.setOnMyLocationButtonClickListener(this);
                    globalGoogleMapInstance.setOnMyLocationClickListener(this);
                } else {
                    locationEnabled = PERMISSIONS_NO_LOCATION;
                }
            }
        }
        else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", LOCATION_PERMISSION_REQUEST_CODE, perms);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Location temp = new Location(LocationManager.GPS_PROVIDER);
        temp.setLatitude(ll.latitude);
        temp.setLongitude(ll.longitude);
        int distance = (int) location.distanceTo(temp);
        Snackbar.make(binding.getRoot(), "Distanza dalla sede in linea d'aria: " + distance + "m", 2000).show();
    }
}