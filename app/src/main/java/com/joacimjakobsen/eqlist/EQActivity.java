package com.joacimjakobsen.eqlist;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class EQActivity extends FragmentActivity implements OnMapReadyCallback {
    public static final String LAT = "com.joacimjakobsen.eqlist.LAT";
    public static final String LONG = "com.joacimjakobsen.eqlist.LONG";
    public static final String PLACE = "com.joacimjakobsen.eqlist.PLACE";
    public static final String MAG = "com.joacimjakobsen.eqlist.MAG";
    private String latitude, longitude, magnitude, place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        latitude = intent.getStringExtra(LAT);
        longitude = intent.getStringExtra(LONG);
        magnitude = intent.getStringExtra(MAG);
        place = intent.getStringExtra(PLACE);
        setContentView(R.layout.activity_eq);
        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng location = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        googleMap.addMarker(new MarkerOptions()
                .title(magnitude)
                .snippet(place)
                .position(location));
    }
}
