package com.tomcat.parkir;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tomcat.parkir.DB.DB;
import com.tomcat.parkir.Object.User;

import java.util.Calendar;

import static android.R.attr.delay;

public class SplashScreenActivity extends AppCompatActivity {


    private static int SPLASH_TIME_OUT = 1500;
    public int timeStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();	//biar koneksi bisa dijalanin di main, karena aturannya koneksi gk boleh di Main langsung
        StrictMode.setThreadPolicy(policy);

        Calendar c = Calendar.getInstance();
        timeStart = c.get(Calendar.SECOND);
        Log.d("Timeout1",""+timeStart);

        User user = new User(this);
        if(user.getPassword()!=null){
            Log.d("User_id",user.getPassword());
            new Auth(user).execute();       //autentikasi user dan password ke server
        }
    }

    class Auth extends AsyncTask<Integer, Integer, Integer> {
        User user;
        Auth(User user) {
            this.user = user;
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
            Log.d("User_id2",user.getPassword());
            DB db = new DB(getApplicationContext(),user);
            int signal=db.auth();
            return signal;
        }

        // ### After completing background task ###
        protected void onPostExecute(final Integer signal) {
            // dismiss the dialog once done
            //pDialog.dismiss();

            super.onPostExecute(signal);
            int time = timeOutRemainder();
            Log.d("Timeout",""+time);
            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
                    Intent i=null;
                    switch (signal) {
                        case 0:
                            i = new Intent(SplashScreenActivity.this, HomeActivity.class);
                            break;
                        case 1:
                            Toast.makeText(getApplicationContext(), getString(R.string.mainFailedLogin), Toast.LENGTH_SHORT).show();
                            i = new Intent(SplashScreenActivity.this, MainActivity.class);
                            break;
                        case 2:
                            Toast.makeText(getApplicationContext(), getString(R.string.signal4), Toast.LENGTH_SHORT).show();
                            i = new Intent(SplashScreenActivity.this, MainActivity.class);
                            break;
                        case 3:
                            Toast.makeText(getApplicationContext(), getString(R.string.signal2), Toast.LENGTH_SHORT).show();
                            i = new Intent(SplashScreenActivity.this, MainActivity.class);
                            break;
                        case 4:
                            Toast.makeText(getApplicationContext(), getString(R.string.signal3), Toast.LENGTH_SHORT).show();
                            break;
                    }
                    startActivity(i);
                    finish();
                }
            }, time);
        }
    }
    public int timeOutRemainder(){
        Calendar c = Calendar.getInstance();
        int time = c.get(Calendar.SECOND);
        Log.d("Timeout2",""+time);
        time -= timeStart;
        if(time<SPLASH_TIME_OUT)
            time = SPLASH_TIME_OUT - time;
        else time=0;

        return time;
    }
}
