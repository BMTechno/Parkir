package com.tomcat.parkir;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tomcat.parkir.DB.DB;
import com.tomcat.parkir.Object.Parkir;
import com.tomcat.parkir.Object.User;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.Console;

import static com.tomcat.parkir.R.id.inputCapacity;
import static com.tomcat.parkir.R.id.inputPrice;
import static com.tomcat.parkir.R.id.map;


public class RequestParkirActivity extends AppCompatActivity implements OnMapReadyCallback{

    MapView mMapView;
    private GoogleMap mMap;
    private Marker marker;
    private LatLng userLatLng;
    public GPSTracker gps;
    int PLACE_PICKER_REQUEST = 1;
    private EditText textInputName;
    private EditText textInputAddress;
    private EditText textInputPrice;
    private EditText textInputCapacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_parkir);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_request_parkir);

        gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            userLatLng = new LatLng(gps.getLatitude(), gps.getLongitude());
        }


        textInputName = (EditText) findViewById(R.id.inputName);
        textInputAddress = (EditText) findViewById(R.id.inputAddress);
        textInputPrice = (EditText) findViewById(inputPrice);
        textInputCapacity = (EditText) findViewById(inputCapacity);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);


        Button btnPick = (Button)findViewById(R.id.btnPick);
        btnPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(RequestParkirActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
//        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//        try {
//            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
//        } catch (GooglePlayServicesRepairableException e) {
//            e.printStackTrace();
//        } catch (GooglePlayServicesNotAvailableException e) {
//            e.printStackTrace();
//        }

        Button btnRequestParkir = (Button) findViewById(R.id.btnSubmit);
        btnRequestParkir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Submit(userLatLng.latitude,userLatLng.longitude,textInputName.getText().toString(),textInputAddress.getText().toString(),textInputPrice.getText().toString(),Integer.parseInt(textInputCapacity.getText().toString())).execute();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    class Submit extends AsyncTask<Integer, Integer, Integer> {
        double lat; double lng; String name; String address; String price; int capacity;
        Submit(double lat, double lng, String name, String address, String price, int capacity) {
            this.lat = lat;
            this.lng = lng;
            this.name = name;
            this.address = address;
            this.price = price;
            this.capacity = capacity;
        }
        // ### Before starting background thread Show Progress Dialog ###
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*
            pDialog = new ProgressDialog(SplashScreenActivity.this);
            pDialog.setMessage("Login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            */
        }

        // ### Login Auth ###
        protected Integer doInBackground(Integer... args) {
            DB db = new DB(getApplicationContext(),new User(getApplicationContext()));
            int signal=db.requestParkir(lat, lng, name, address, price, capacity);
            return signal;
        }

        // ### After completing background task ###
        protected void onPostExecute(final Integer signal) {
            // dismiss the dialog once done
            //pDialog.dismiss();

            super.onPostExecute(signal);
            switch (signal) {
                case 0:
                    Toast.makeText(getApplicationContext(), getString(R.string.textSuccess), Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), getString(R.string.textRequestFailed), Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), getString(R.string.signal2), Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), getString(R.string.signal3), Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(), getString(R.string.signal4), Toast.LENGTH_SHORT).show();
                    break;
            }
            finish();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();
        setUpMarker();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Log.i("YYYY ","1");
                Place place = PlacePicker.getPlace(data, this);
                //Log.i("Place ",""+place.getName()+ " "+place.getId()+ " " + place.getAddress()+ " "+place.getAttributions());
                userLatLng = place.getLatLng();
                textInputAddress.setText(place.getAddress());
                marker.setPosition(userLatLng);
            }
        }
    }

    public void setUpMap(){
        double currentLatitude=0;
        double currentLongitude=0;
        mMap.setMyLocationEnabled(true);
        //set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (gps.canGetLocation()) {
            currentLatitude = gps.getLatitude();
            currentLongitude = gps.getLongitude();
        }
        Log.d("currentlatitude",""+currentLatitude);
        // Create a LatLng object for the current location
        LatLng currentCoordinate = new LatLng(currentLatitude, currentLongitude);
        // Show the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentCoordinate));
        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));

    }
    public void setUpMarker(){
        marker = mMap.addMarker(new MarkerOptions().position(userLatLng));
    }
}
