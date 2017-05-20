package com.tomcat.parkir.DB;

import android.content.Context;
import android.content.SharedPreferences;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/*
	copyright Albert Alfrianta @TOMCAT INC
	June 2015
	Bogor, Indonesia
*/
public class DBHelper {
    //private String url="http://192.168.1.123/scs/index.php";
    private String url="http://192.168.1.4/parkir/index.php";  //192.168.1.14

    private DBServer dbS;
    private DBLocal dbL;
    Context context;
    JSONArray jA;
    private String id;
    private String password;
    public DBHelper(Context con, String id, String password){
        context=con;
        this.id = id;
        this.password = password;
        dbS = new DBServer(context);
        //dbL= new DBLocal();
    }

    public JSONArray get(){
        return dbS.get();
    }


    public int getListParkir(){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "getListParkir"));
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("password", password));
        return send(params);
    }
    public int getListParkirSave(){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "getListParkirSave"));
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("password", password));
        return send(params);
    }
    public int getDetailParkir(String parkirId){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "getDetailParkir"));
        params.add(new BasicNameValuePair("parkir_id", parkirId));
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("password", password));
        return send(params);
    }
    public int checkParkirSave(String parkirId){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "checkParkirSave"));
        params.add(new BasicNameValuePair("parkir_id", parkirId));
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("password", password));
        return send(params);
    }
    public int saveParkir(String parkirId){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "saveParkir"));
        params.add(new BasicNameValuePair("parkir_id", parkirId));
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("password", password));
        return send(params);
    }
    public int removeSaveParkir(String parkirId){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "removeSaveParkir"));
        params.add(new BasicNameValuePair("parkir_id", parkirId));
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("password", password));
        return send(params);
    }

    public int login(String username, String password){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "login"));
        params.add(new BasicNameValuePair("id", username));
        params.add(new BasicNameValuePair("password", password));
        return send(params);
    }
    public int register(String username, String password){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "register"));
        params.add(new BasicNameValuePair("id", username));
        params.add(new BasicNameValuePair("password", password));
        return send(params);
    }
    public int auth(String id, String password){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("action", "auth"));
        return dbS.signal(url,params);
    }


    public int send(List<NameValuePair> params){
        return dbS.signal(url,params);
    }
    //TODO delete all below

/*
    public int sync(){

    }
*/



/*
    public int setData(List<NameValuePair> params){
        String user[] = getAuth();
        params.add(new BasicNameValuePair("id", user[0]));
        params.add(new BasicNameValuePair("password", user[1]));
        return dbS.signal(url,params);
    }

    public int getData(String action){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", action));
        return send(params);
    }
    public int gNew(){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "gNew"));
        return send(params);
    }

    public int setAccount(List<NameValuePair> params){
        params.add(new BasicNameValuePair("action", "updateAccount"));
        return send(params);
    }

    public int getAccount(){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "getAccount"));
        return send(params);
    }

    public int gFRequest(){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "gFRequest"));
        return send(params);
    }

    public int fRequest(String id_friend){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id_friend", id_friend));
        params.add(new BasicNameValuePair("action", "fRequest"));
        return send(params);
    }

    public int fRequestAcc(String id_friend){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id_friend", id_friend));
        params.add(new BasicNameValuePair("action", "fRequestAcc"));
        return send(params);
    }

    public int fRequestDec(String id_friend){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id_friend", id_friend));
        params.add(new BasicNameValuePair("action", "fRequestDec"));
        return send(params);
    }

    public int sFMessage(List<NameValuePair> params){
        params.add(new BasicNameValuePair("action", "iFMessage"));
        return send(params);
    }

    public int gFMessage(String date){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("date_status", date));
        params.add(new BasicNameValuePair("action", "gFMessage"));
        return send(params);
    }

    public int uFMessageStatus(List<NameValuePair> params){
        params.add(new BasicNameValuePair("action", "uFMessageStatus"));
        return send(params);
    }

    public int fList(String date){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("date", date));
        params.add(new BasicNameValuePair("action", "gFriend"));
        return send(params);
    }
    public int fDelete(String id_friend){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id_friend", id_friend));
        params.add(new BasicNameValuePair("action", "fDelete"));
        return send(params);
    }
    public int gFDelete(){      //melihat jika unfriend
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "gFDelete"));
        return send(params);
    }
    public int fProfile(String id_friend){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "fDelete"));
        return send(params);
    }

    public int gForumLast(String date, String what){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "gForumLast"));
        params.add(new BasicNameValuePair("date", date));
        params.add(new BasicNameValuePair("what", what));
        return send(params);
    }
    public int gForumOlder(String date, String what){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "gForumOlder"));
        params.add(new BasicNameValuePair("date", date));
        params.add(new BasicNameValuePair("what", what));
        return send(params);
    }

    public int gForumCommentLast(int id_forum, String date){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "gForumCommentLast"));
        params.add(new BasicNameValuePair("id_forum", Integer.toString(id_forum)));
        params.add(new BasicNameValuePair("date", date));
        return send(params);
    }
    public int gForumCommentOlder(int id_forum, String date){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "gForumCommentOlder"));
        params.add(new BasicNameValuePair("id_forum", Integer.toString(id_forum)));
        params.add(new BasicNameValuePair("date", date));
        return send(params);
    }
    public int iForum(String title, String description){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "iForum"));
        params.add(new BasicNameValuePair("title", title));
        params.add(new BasicNameValuePair("description", description));
        return send(params);
    }
    public int iForumComment(int id_forum, String comment){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "iForumComment"));
        params.add(new BasicNameValuePair("id_forum", Integer.toString(id_forum)));
        params.add(new BasicNameValuePair("comment", comment));
        return send(params);
    }
    public int gNewsLast(String date, String what){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "gNewsLast"));
        params.add(new BasicNameValuePair("date", date));
        params.add(new BasicNameValuePair("what", what));
        return send(params);
    }
    public int gNewsOlder(String date, String what){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "gNewsOlder"));
        params.add(new BasicNameValuePair("date", date));
        params.add(new BasicNameValuePair("what", what));
        return send(params);
    }
    public int gNews(String date, String id_user){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "gNews"));
        params.add(new BasicNameValuePair("date", date));
        params.add(new BasicNameValuePair("id_user", id_user));
        return send(params);
    }
    public int cNewsUser(){  //untuk mengecek apakah user boleh buat berita
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "cNewsUser"));
        return send(params);
    }
    public int gNewsGroup(){  //ambil group news yg terdaftar sesuai user
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "gNewsGroup"));
        return send(params);
    }
    public int iNews(String id_group, String title, String news){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "iNews"));
        params.add(new BasicNameValuePair("id_group", id_group));
        params.add(new BasicNameValuePair("title", title));
        params.add(new BasicNameValuePair("news", news));
        return send(params);
    }
    */
}