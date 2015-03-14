/**
 *Main Activity.java: still under development. This powers the core functionality of the application.
 * Many methods, attributes and functions will be moved to different classes in later builds. **/
package proj.wifilocator;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.io.IOException;
import java.util.List;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import android.widget.EditText;
import android.widget.Toast;
import android.R.*;
import com.google.android.gms.maps.model.Marker;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public  class MainActivity extends FragmentActivity  {
    GoogleMap googleMap;
    MarkerOptions markerOptions;
    LatLng latLng;
    static final LatLng CENTENNIAL = new LatLng(43.785042, -79.227052);
    static final LatLng STARBUCKSMARKVILLE = new LatLng(43.867096, -79.288130);
    static final LatLng HWYSTAR = new LatLng(43.867080, -79.285212);
    static final LatLng TIMSHEP = new LatLng(43.791503, -79.250054);
    static final LatLng SHEPSTAR = new LatLng(43.777878, -79.344654);
    static final LatLng TIMELLE = new LatLng(43.783860, -79.205199);
    static final LatLng MIDTIM = new LatLng(43.766015, -79.271036);
    private GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MapFragment SMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        // Getting a reference to the map
        googleMap = SMapFragment.getMap();
        googleMap.setMyLocationEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        // Getting reference to btn_find of the layout activity_main
                Button btn_find = (Button) findViewById(R.id.btn_find);
                // Defining button click event listener for the find button
                OnClickListener findClickListener = new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Getting reference to EditText to get the user input location
                        EditText etLocation = (EditText) findViewById(R.id.et_location);
                        // Getting user input location
                        String location = etLocation.getText().toString();
                        if (location != null && !location.equals(""))
                        {
                            new GeocoderTask().execute(location);
                        }
                    }
                };

        createTempMapData(btn_find, findClickListener);
        /*Temporary Map Data*/

            }
            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.menu_main, menu);
                return true;
            }

        public void createTempMapData(Button btn_find, OnClickListener findClickListener)
        {

            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)) .getMap();
            Marker centennial = map.addMarker(new MarkerOptions().position(CENTENNIAL) .title("Centennial College"));
            Marker starmark = map.addMarker(new MarkerOptions().position(STARBUCKSMARKVILLE)  .title("Markville Mall - Starbucks"));
            Marker hwystar = map.addMarker(new MarkerOptions().position(HWYSTAR)  .title("Hwy 7 & Markham - Starbucks"));
            Marker timshep = map.addMarker(new MarkerOptions().position(TIMSHEP)   .title("Sheppard Avenue-TimHortons"));
            Marker timelle = map.addMarker(new MarkerOptions().position(TIMELLE).title("Ellesmere Road-TimHortons"));
            Marker midtim = map.addMarker(new MarkerOptions().position(MIDTIM)  .title("Midland Avenue-TimHortons"));
            Marker shepstar = map.addMarker(new MarkerOptions().position(SHEPSTAR)   .title("Sheppard Avenue East-Starbucks"));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(CENTENNIAL,11));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(STARBUCKSMARKVILLE,11));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(HWYSTAR,11));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(SHEPSTAR,11));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(TIMSHEP,11));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(TIMELLE,11));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(MIDTIM,11));
            btn_find.setOnClickListener(findClickListener);
        }
            // An AsyncTask class for accessing the GeoCoding Web Service
            private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {
                @Override
                protected List<Address> doInBackground(String... locationName) {
                    // Creating an instance of Geocoder class
                    Geocoder geocoder = new Geocoder(getBaseContext());
                    List<Address> addresses = null;
                    try {
                        // Getting a maximum of 3 Address that matches the input text
                        addresses = geocoder.getFromLocationName(locationName[0], 3);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return addresses;
                }
                @Override
                protected void onPostExecute(List<Address> addresses) {
                    if (addresses == null || addresses.size() == 0) {
                        Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
                    }
                    // Clears all the existing markers on the map
                    googleMap.clear();
                    // Adding Markers on Google Map for each matching address
                    for (int i = 0; i < addresses.size(); i++) {
                        Address address = (Address) addresses.get(i);
                        // Creating an instance of GeoPoint, to display in Google Map
                        latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        String addressText = String.format("%s, %s",
                                address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                                address.getCountryName());
                        markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title(addressText);
                        googleMap.addMarker(markerOptions);
                        // Locate the first location
                        if (i == 0)
                            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    }
                }
            }
        }


