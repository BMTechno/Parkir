package com.tomcat.parkir.DB;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.tomcat.parkir.Object.Parkir;
import com.tomcat.parkir.Object.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.R.attr.x;

/**
 * Created by albertbrucelee on 26/04/17.
 */

public class DB {

    DBHelper dbH; //DBHelper class buat ke server
    User user;

    private Context context;
    public DB(Context context, User user){
        this.context = context;
        this.user=user;
        dbH = new DBHelper(this.context, user.getUsername(), user.getPassword());

        //new DBCreate(context);
    }

    public Parkir[] getListParkir(){
        int signal=dbH.getListParkir();       //ambil data dari server. Nanti data akan disimpan dulu di DBServer. signal buat melihat apakah query sukses atau failed
        if (signal==0){                     //jika sukses
            return listParkir();
        }
        else        //jika tidak sukses ambil data (error);
            return null;
    }
    public Parkir[] getListParkirSave(){
        int signal=dbH.getListParkirSave();       //ambil data dari server. Nanti data akan disimpan dulu di DBServer. signal buat melihat apakah query sukses atau failed
        if (signal==0){                     //jika sukses
            return listParkir();
        }
        else        //jika tidak sukses ambil data (error);
            return null;
    }
    public Parkir[] listParkir(){
        JSONArray json=dbH.get();        //ambil data di DBServer! Data akan seperti: [{"nama":"Albert"}]

        Parkir parkir[] = new Parkir[json.length()];

        try{
            for(int i=0; i<json.length(); i++){
                JSONObject jData = json.getJSONObject(i);   //ambil data object ke 0
                parkir[i] = new Parkir();
                parkir[i].setId(jData.getInt("id"));
                parkir[i].setLatitude(jData.getDouble("latitude"));
                parkir[i].setLongitude(jData.getDouble("longitude"));
                parkir[i].setName(jData.getString("name"));
            }
        }catch (JSONException e){                       //jika JSON error
            Log.e("DB JParser", "Error parsing data " + e.toString());    //pesan ke logcat
        }
        return parkir;
    }

    public Parkir getDetailParkir(String parkirId){
        int signal=dbH.getDetailParkir(parkirId);       //ambil data dari server. Nanti data akan disimpan dulu di DBServer. signal buat melihat apakah query sukses atau failed
        if (signal==0){                     //jika sukses
            JSONArray json=dbH.get();        //ambil data di DBServer! Data akan seperti: [{"nama":"Albert"}]

            Parkir parkir = new Parkir();

            try{
                    JSONObject jData = json.getJSONObject(0);   //ambil data object ke 0
                    parkir.setId(jData.getInt("id"));
                    parkir.setLatitude(jData.getDouble("latitude"));
                    parkir.setLongitude(jData.getDouble("longitude"));
                    parkir.setName(jData.getString("name"));
                    parkir.setAddress(jData.getString("address"));
                    parkir.setPrice(jData.getString("price"));
                    parkir.setCapacity(jData.getInt("capacity"));
                    parkir.setAvailable(jData.getInt("available"));
            }catch (JSONException e){                       //jika JSON error
                signal=1;
                Log.e("DB JParser", "Error parsing data " + e.toString());    //pesan ke logcat
            }
            return parkir;
        }
        else        //jika tidak sukses ambil data (error);
            return null;
    }
    public int checkParkirSave(String parkirId){
        int signal=dbH.checkParkirSave(parkirId);       //ambil data dari server. Nanti data akan disimpan dulu di DBServer. signal buat melihat apakah query sukses atau failed
        int total=0;
        if (signal==0){                     //jika sukses
            JSONArray json=dbH.get();        //ambil data di DBServer! Data akan seperti: [{"nama":"Albert"}]


            try{
                JSONObject jData = json.getJSONObject(0);   //ambil data object ke 0
                total = jData.getInt("total");
            }catch (JSONException e){                       //jika JSON error
                signal=1;
                Log.e("DB JParser", "Error parsing data " + e.toString());    //pesan ke logcat
            }
            return total;
        }
        else        //jika tidak sukses ambil data (error);
            return total;
    }
    public boolean saveParkir(String parkirId){
        int signal=dbH.saveParkir(parkirId);       //ambil data dari server. Nanti data akan disimpan dulu di DBServer. signal buat melihat apakah query sukses atau failed
        if(signal==0)
            return true;
        else
            return false;
    }
    public boolean removeSaveParkir(String parkirId){
        int signal=dbH.removeSaveParkir(parkirId);       //ambil data dari server. Nanti data akan disimpan dulu di DBServer. signal buat melihat apakah query sukses atau failed
        if(signal==0)
            return true;
        else
            return false;
    }

    public boolean login(){

        int signal=dbH.login(user.getUsername(),user.getPassword());
        if (signal==0){
            JSONArray json=dbH.get();
            try{
                JSONObject jData = json.getJSONObject(0);
                user.setPassword(jData.getString("password"));
                user.setAuth();
                new DBCreate(context);
            }catch (JSONException e){
                Log.e("Login.class JSON Parser", "Error parsing data " + e.toString());
            }
            return true;
        }
        return false;
    }
    public boolean register(){

        int signal=dbH.register(user.getUsername(),user.getPassword());
        if (signal==0){
            user.setUsername(null);
            user.setPassword(null);
            return true;
        }
        return false;
    }
    public int auth(){
        return dbH.auth(user.getUsername(),user.getPassword());
    }
}
