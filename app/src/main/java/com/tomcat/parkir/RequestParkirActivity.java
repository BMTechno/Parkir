package com.tomcat.parkir;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.tomcat.parkir.DB.DB;
import com.tomcat.parkir.Object.Parkir;
import com.tomcat.parkir.Object.User;

import static android.R.attr.name;
import static android.R.id.input;

public class RequestParkirActivity extends AppCompatActivity implements OnMapReadyCallback{

    MapView mMapView;
    private GoogleMap mMap;
    private LatLng userLatLng;
    public GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_parkir);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_request_parkir);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();	//biar koneksi bisa dijalanin di main, karena aturannya koneksi gk boleh di Main langsung
        StrictMode.setThreadPolicy(policy);



        gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            userLatLng = new LatLng(gps.getLatitude(), gps.getLongitude());
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button btnRequestParkir = (Button) findViewById(R.id.btnSubmit);
        btnRequestParkir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText inputName = (EditText) findViewById(R.id.inputName);
                EditText inputAddress = (EditText) findViewById(R.id.inputAddress);
                EditText inputPrice = (EditText) findViewById(R.id.inputPrice);
                EditText inputCapacity = (EditText) findViewById(R.id.inputCapacity);
                new Submit(userLatLng.latitude,userLatLng.longitude,inputName.getText().toString(),inputAddress.getText().toString(),inputPrice.getText().toString(),Integer.parseInt(inputCapacity.getText().toString())).execute();
            }
        });
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
}
