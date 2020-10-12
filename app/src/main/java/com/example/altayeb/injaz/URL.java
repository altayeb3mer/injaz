package com.example.altayeb.injaz;

import android.content.Context;
import android.net.ConnectivityManager;

import java.net.InetAddress;

/**
 * Created by Altayeb on 2/6/2018.
 */
public class URL {
    public static String ROOT_URL = "http://ingaz-app.com/phpscript/";
    public static String ARY_URL = ROOT_URL + "fetch_multi_rows.php";
    public static String REG_URL = ROOT_URL+"register1.php";
    public static String LOGIN_URL = ROOT_URL+"login1.php";
    public static String IMG_UPLOAD_URL = ROOT_URL+"image.php";
    public static String GET_PROFILE_URL = ROOT_URL+"getprofile.php";
    public static String UPDATE_PROFILE_URL = ROOT_URL+"profileupdate.php";
    public static String GET_MY_PROFIL_URL = ROOT_URL+"getmyprofile.php";
    public static String SAVE_MY_LOCATION_URL = ROOT_URL+ "insertLocation.php";
    public static String GET_LOCATION_URL = ROOT_URL+ "getLocation.php";
    public static String GET_GENDER_URL = ROOT_URL+ "getgender.php";
    public static String IS_UPDATE_AVAILABLE_URL = ROOT_URL+ "isupdateavailable.php";


    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName(ROOT_URL);//You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }
}
