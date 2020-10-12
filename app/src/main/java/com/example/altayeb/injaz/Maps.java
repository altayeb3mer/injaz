package com.example.altayeb.injaz;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Maps extends FragmentActivity implements OnMapReadyCallback {

    private static final int REQUEST_CALL =1 ;
    private GoogleMap mMap;
    ProgressDialog progressDialog;
    String GPS;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري المعالجة..");
        progressDialog.setCancelable(false);
        Bundle bundle = getIntent().getExtras();
        GPS = bundle.getString("GPS");
        if(GPS.equals("TRUE")){
            Toast.makeText(Maps.this, "لتحديث موقع اضغط على ايقونة الموقع", Toast.LENGTH_SHORT).show();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * <p/>
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (GPS.equals("FALSE")) {
            if (ContextCompat.checkSelfPermission(Maps.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(Maps.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CALL);
            }else {
                showDialog();
            location();
                hideDialog();
          }}
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(Maps.this,"لعرض ملف هذا الشخص، اضغط مطولا على اسمه", Toast.LENGTH_LONG).show();
                return false;
            }
        });
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (ContextCompat.checkSelfPermission(Maps.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(Maps.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CALL);
                }else {
                    if (!SharePref.get_phone_no(Maps.this).equals("guest")) {
                        if (!mMap.equals(null)) {
                            LatLng latLng = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
                            String latitude = String.valueOf(latLng.latitude);
                            String longitude = String.valueOf(latLng.longitude);
                            insertLocation(latitude, longitude);
                        }else {
                            Toast.makeText(Maps.this, "عفوا /n" +
                                    "الانترنت ضعيف/n" +
                                    "حاول مجددا", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                return false;
            }
        });

        mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {
                for (int i = 0; i < WorkerList.profile.size(); i++) {
                    if (marker.getTitle().toString().trim().equals(WorkerList.profile.get(i))) {
                        Intent intent = new Intent(getApplicationContext(), profile.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("phone_no", WorkerList.phone_no.get(i));
                        bundle.putString("full_name", marker.getTitle().toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void insertLocation(final String latitude, final String longitude) {

        //////
        showDialog();
        if (URL.isNetworkConnected(Maps.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.SAVE_MY_LOCATION_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String Response = jsonObject.getString("result");
                                if (Response.equals("yes")) {
                                    hideDialog();
                                    Toast.makeText(Maps.this, "تم تحديث موقعك", Toast.LENGTH_SHORT).show();
                                } else {
                                    hideDialog();
                                    Toast.makeText(Maps.this, "لم يتم تحديث موقعك حاول مجددا", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                hideDialog();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();
                    Toast.makeText(getApplicationContext(), "تعذر الوصول للخادم", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("phone_no", SharePref.get_phone_no(Maps.this));
                    params.put("locx", latitude);
                    params.put("locy", longitude);
                    return params;
                }
            };
            MySingleton.getmInstance(getApplicationContext()).addToRequestque(stringRequest);

            //////
        } else {
            Toast.makeText(Maps.this, "لايوجد اتصال بالانترنت", Toast.LENGTH_SHORT).show();
        }
    }


    //Operate Dialog
    private void showDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void hideDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.altayeb.injaz/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.altayeb.injaz/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    /////////
    private void location() {
        showDialog();
        if (URL.isNetworkConnected(getApplicationContext())) {
            if (!URL.isInternetAvailable()) {
                if (ContextCompat.checkSelfPermission(Maps.this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(Maps.this,new String[]{Manifest.permission.INTERNET},REQUEST_CALL);
                }else {
                for (int i = 0; i < WorkerList.phone_no.size(); i++) {
                    final int finalI = i;
                    final int finalI1 = i;
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.GET_LOCATION_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String message = jsonObject.getString("message");
                                        if (message.equals("yes")) {
                                            String locx = jsonObject.getString("locx");
                                            String locy = jsonObject.getString("locy");
                                            LatLng ltlg = new LatLng(Float.parseFloat(locx), Float.parseFloat(locy));
                                            Marker marker = mMap.addMarker(new MarkerOptions().position(ltlg).title(WorkerList.profile.get(finalI1)));
                                        } else {
                                            Toast.makeText(Maps.this, "Error", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideDialog();
                            Toast.makeText(getApplicationContext(), "تعذر الوصول للخادم", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                            finish();

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("phone_no", WorkerList.phone_no.get(finalI));

                            return params;
                        }
                    };
                    MySingleton.getmInstance(Maps.this).addToRequestque(stringRequest);
                }
                hideDialog();
            }} else {
                Toast.makeText(Maps.this, "خطأ بالانترنت", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(Maps.this, "لايوجد اتصال بالانترنت", Toast.LENGTH_SHORT).show();
            finish();
        }
        hideDialog();
    }
}


