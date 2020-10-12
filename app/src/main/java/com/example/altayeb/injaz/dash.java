package com.example.altayeb.injaz;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class dash extends AppCompatActivity {
    //permission var
    private static final int REQUEST_LOCATION = 1;
    String phone_no;
    public static boolean visit = false;
    private static String[] depts_array;
    SharePref sharePref;
    ProgressDialog progressDialog;
    public static final int REQUEST_CALL = 1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dash);

        progressDialog = new ProgressDialog(dash.this);
        progressDialog.setMessage("جاري المعالجة..");
        progressDialog.setCancelable(false);
        //Android V6 permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        Bundle bundle = getIntent().getExtras();
        phone_no = bundle.getString("phone_no");
        if (!phone_no.equals("guest")) {
            Toast.makeText(dash.this, "قم بتحديث موقعك لتظر للاخرين على الخريطه\nاضغط على زر الموقع في الاعلى للتحديث", Toast.LENGTH_LONG).show();
        }////////


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);


        if (phone_no.equals("guest")) {
            Toast.makeText(dash.this, "للحصول على ميزات اكثر قم بالتسجيل", Toast.LENGTH_SHORT).show();
            MenuItem M_location = menu.findItem(R.id.id_update_location);
            //  MenuItem M_sign_out = menu.findItem(R.id.id_signout);
            MenuItem M_my_profile = menu.findItem(R.id.id_profile);
            M_location.setVisible(false);
            // M_sign_out.setVisible(false);
            M_my_profile.setVisible(false);
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.id_exit) {
            buildAlertMessageNoGps();
            return true;
        }
        if (id == R.id.id_update_location) {
            Intent i = new Intent(getApplicationContext(), Maps.class);
            Bundle bundle = new Bundle();
            bundle.putString("GPS", "TRUE");
            i.putExtras(bundle);
            startActivity(i);
            return true;
        }
        if (id == R.id.id_signout) {
            buildAlertMessageNoGps2();
            return true;
        }
        if (id == R.id.id_profile) {
            Intent i = new Intent(getApplicationContext(), myProfile.class);
            Bundle bundle = new Bundle();
            bundle.putString("phone_no", phone_no);
            bundle.putString("Activity", "dash");
            i.putExtras(bundle);
            startActivity(i);
            return true;
        }
        if (id == R.id.id_about_injaz) {
            Intent i = new Intent(getApplicationContext(), AboutApp.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.id_about_us) {
            Intent i = new Intent(getApplicationContext(), AboutUs.class);
            startActivity(i);
            return true;
        }
        return true;
    }

    @Override
    protected void onResume() {
        visit = false;
        super.onResume();
    }


    //Images view event
    public void onclick(View view) {
        depts_array = getResources().getStringArray(R.array.dept);
        if (view.getTag().toString().equals("B1")) {
            Intent intent = new Intent(getApplicationContext(), Layout_tecno.class);
            startActivity(intent);
        }
        if (view.getTag().toString().equals("B2")) {
            Intent intent = new Intent(getApplicationContext(), Layout_edu_learning.class);
            startActivity(intent);
        }
        if (view.getTag().toString().equals("B3")) {
            Intent intent = new Intent(getApplicationContext(), Layout_print_translate.class);
            startActivity(intent);
        }
        if (view.getTag().toString().equals("B4")) {
            Intent intent = new Intent(getApplicationContext(), Layout_cleaning.class);
            startActivity(intent);
        }
        if (view.getTag().toString().equals("B5")) {
            Intent intent = new Intent(getApplicationContext(), Layout_smsar.class);
            startActivity(intent);
        }
        if (view.getTag().toString().equals("B16")) {
            showDialog();
            if (phone_no.equals("guest")) {
                hideDialog();
                Toast.makeText(dash.this, "عفوا انت ضيف الان وننصحك بالتسجيل", Toast.LENGTH_SHORT).show();
            } else {
                if (URL.isNetworkConnected(getApplicationContext())) {
                    if (!URL.isInternetAvailable()) {
                        if (ContextCompat.checkSelfPermission(dash.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(dash.this, new String[]{Manifest.permission.INTERNET}, REQUEST_CALL);
                        } else {
                            getgender();
                        }
                    } else {
                        hideDialog();
                        Toast.makeText(dash.this, "خطأ بالانترنت", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    hideDialog();
                    Toast.makeText(dash.this, "لايوجد اتصال بالانترنت", Toast.LENGTH_SHORT).show();
                }

            }
        }


        for (int i = 0; i < depts_array.length; i++) {
            String id = "A" + String.valueOf(i);
            if (view.getTag().toString().equals(id)) {
                Intent intent = new Intent(getApplicationContext(), WorkerList.class);
                Bundle bundle = new Bundle();
                bundle.putString("DEPT", depts_array[i]);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }

    }


    //
    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("سوف يتم اغلاق التطبيق ؟")
                .setCancelable(true)
                .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        finish();
                    }
                })
                .setNegativeButton("رجوع", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    ///
    protected void buildAlertMessageNoGps2() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("هل انت متأكد من انك تريد تسجيل الخروج؟ \nسوف يتم مسح يباناتك!")
                .setCancelable(true)
                .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        sharePref = new SharePref();
                        sharePref.logout(dash.this);
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton("رجوع", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void getgender() {
        showDialog();
        //  final String[] gender = new String[1];
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.GET_GENDER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            if (code.equals("yes")) {
                                String gender = jsonObject.getString("gender");
                                if (gender.equals("ذكر")) {
                                    Toast.makeText(dash.this, "عفوا هذا القسم خاص بالنساء فقط !", Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent intent = new Intent(getApplicationContext(), WorkerList.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("DEPT", depts_array[16]);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        hideDialog();
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone_no", SharePref.get_phone_no(dash.this));
                return params;
            }
        };
        MySingleton.getmInstance(dash.this).addToRequestque(stringRequest);
        //  return gender[0];
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
                "dash Page", // TODO: Define a title for the content shown.
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
                "dash Page", // TODO: Define a title for the content shown.
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
}
