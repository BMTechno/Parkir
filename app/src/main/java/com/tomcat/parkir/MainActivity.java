package com.tomcat.parkir;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.tomcat.parkir.DB.DB;
import com.tomcat.parkir.DB.DBHelper;
import com.tomcat.parkir.Object.User;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);     //menghilangkan heading title
        setContentView(R.layout.activity_main);

    }

    public void ClickDaftar(View V){
        Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(i);
    }

    public void ClickLogin(View V){
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
    }


}
