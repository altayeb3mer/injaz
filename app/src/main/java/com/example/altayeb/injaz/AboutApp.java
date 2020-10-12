package com.example.altayeb.injaz;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AboutApp extends AppCompatActivity {
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;
    public static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_app);
        builder = new AlertDialog.Builder(AboutApp.this);
        progressDialog = new ProgressDialog(AboutApp.this);
        progressDialog.setMessage("جاري المعالجة..");
        progressDialog.setCancelable(false);

        TextView txt_ingaz = (TextView) findViewById(R.id.txt_ingaz);
        TextView about1 = (TextView) findViewById(R.id.txt_ingaz1);
        TextView about2 = (TextView) findViewById(R.id.txt_ingaz2);
        TextView about3 = (TextView) findViewById(R.id.txt_ingaz3);
        Button get_update = (Button) findViewById(R.id.updating);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "mesaad.ttf");
        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "sultan.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "one.ttf");

        get_update.setTypeface(typeface1);
        txt_ingaz.setTypeface(typeface2);
        about1.setTypeface(typeface1);
        about2.setTypeface(typeface1);
        about3.setTypeface(typeface);

        get_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (URL.isNetworkConnected(getApplicationContext())) {
                    if (!URL.isInternetAvailable()) {
                        if (ContextCompat.checkSelfPermission(AboutApp.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(AboutApp.this, new String[]{Manifest.permission.INTERNET}, REQUEST_CALL);
                        } else {

                            showDialog();
                            final String ifupdate_available = getResources().getString(R.string.Builder_U_AV);
                            final String ifupdate_not_available = getResources().getString(R.string.Builder_U_N_AV);
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.IS_UPDATE_AVAILABLE_URL,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONArray jsonArray = new JSONArray(response);
                                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                                String code = jsonObject.getString("code");
                                                if (code.equals("2")) {
                                                    hideDialog();

                                                    displayAlert(ifupdate_available, code);

                                                } else if (code.equals("1")) {
                                                    hideDialog();
                                                    displayAlert(ifupdate_not_available, code);
                                                } else {
                                                    hideDialog();
                                                    Toast.makeText(AboutApp.this, " حدث خطأ، حاول مره أخرى", Toast.LENGTH_SHORT).show();
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

                                }
                            });
                            MySingleton.getmInstance(AboutApp.this).addToRequestque(stringRequest);
                        }
                    } else {
                        Toast.makeText(AboutApp.this, "خطأ بالانترنت", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AboutApp.this, "لايوجد اتصال بالانترنت", Toast.LENGTH_SHORT).show();
                }
            }

        });


    }


    public void displayAlert(String message, String code) {
        builder.setTitle("تحديث البرنامج");
        if (code.equals("2")) {
            builder.setMessage(message);
            builder.setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(AboutApp.this, "يجب ربط قوقل play", Toast.LENGTH_SHORT).show();
                }
            }).setNegativeButton("رجوع", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        } else {
            builder.setMessage(message);
            builder.setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
}
