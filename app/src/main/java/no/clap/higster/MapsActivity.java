package no.clap.higster;

import no.clap.higster.R;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        mMap.setMyLocationEnabled(true);  // Shows current position

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        LatLng hig = new LatLng(60.789237,10.681779);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hig,17));

        Marker abuilding = mMap.addMarker(new MarkerOptions().position(new LatLng(60.790107,10.683389)).title("A-building"));
        Marker kbuilding = mMap.addMarker(new MarkerOptions().position(new LatLng(60.790332,10.682455)).title("K-building"));
        Marker gbuilding = mMap.addMarker(new MarkerOptions().position(new LatLng(60.78973, 10.682412)).title("G-building"));
        Marker bbuilding = mMap.addMarker(new MarkerOptions().position(new LatLng(60.789515,10.680749)).title("B-building"));
        Marker hbuilding = mMap.addMarker(new MarkerOptions().position(new LatLng(60.788447,10.680749)).title("H-building"));

        abuilding.showInfoWindow();
        kbuilding.showInfoWindow();
        gbuilding.showInfoWindow();
        bbuilding.showInfoWindow();
        hbuilding.showInfoWindow();
    }
}
