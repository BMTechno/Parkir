package com.tomcat.parkir;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tomcat.parkir.DB.DB;
import com.tomcat.parkir.Object.User;

import static android.R.attr.name;

public class RequestParkirActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_parkir);


        Button btnRequestParkir = (Button) findViewById(R.id.btnSubmit);
        btnRequestParkir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText inputName = (EditText) findViewById(R.id.inputName);
                EditText inputAddress = (EditText) findViewById(R.id.inputAddress);
                EditText inputPrice = (EditText) findViewById(R.id.inputPrice);
                EditText inputInfo = (EditText) findViewById(R.id.inputInfo);
                new Submit(""+123,""+222,inputName.getText().toString(),inputAddress.getText().toString(),inputPrice.getText().toString(),inputInfo.getText().toString()).execute();
            }
        });
    }

    class Submit extends AsyncTask<Integer, Integer, Integer> {
        String lat; String lng; String name; String address; String price; String info;
        Submit(String lat, String lng, String name, String address, String price, String info) {
            this.lat = lat;
            this.lng = lng;
            this.name = name;
            this.address = address;
            this.price = price;
            this.info = info;
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
            int signal=db.requestParkir(lat, lng, name, address, price, info);
            return signal;
        }

        // ### After completing background task ###
        protected void onPostExecute(final Integer signal) {
            // dismiss the dialog once done
            //pDialog.dismiss();

            super.onPostExecute(signal);
            Intent i=null;
            switch (signal) {
                case 0:
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), getString(R.string.pleaseLogin), Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), getString(R.string.signal4), Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), getString(R.string.signal2), Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(), getString(R.string.signal3), Toast.LENGTH_SHORT).show();
                    break;
            }
            startActivity(i);
            finish();
        }
    }
}
