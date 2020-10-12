package com.example.altayeb.injaz;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class profile extends AppCompatActivity {
    private static final int REQUEST_CALL =1 ;
    CircleImageView circleImageView, call, whatsapp;
    TextView txtusername, txtgender, txtdept, txtphone, txtabout;
    String phone_no, full_name, gender, dept, about;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        //P_Dialog
        progressDialog = new ProgressDialog(profile.this);
        progressDialog.setMessage("جاري المعالجة..");
        progressDialog.setCancelable(false);


        //bundle handle
        Bundle bundle = getIntent().getExtras();
        phone_no = bundle.getString("phone_no");
        full_name = bundle.getString("full_name");
        ///
        txtusername = (TextView) findViewById(R.id.Pro_username);
        txtgender = (TextView) findViewById(R.id.Pgender);
        txtdept = (TextView) findViewById(R.id.Pdept);
        txtphone = (TextView) findViewById(R.id.Pphone);
        txtabout = (TextView) findViewById(R.id.aboutuser);
        txtusername.setText(full_name);
        //calling function
        if (ContextCompat.checkSelfPermission(profile.this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(profile.this,new String[]{Manifest.permission.INTERNET},REQUEST_CALL);
        }else {
        getdata();}


        txtphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:" + phone_no));
                ////
                if (ContextCompat.checkSelfPermission(profile.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(profile.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
                }else {
                    startActivity(i);
                }
            }
        });

        circleImageView = (CircleImageView) findViewById(R.id.cimgview);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                if (ContextCompat.checkSelfPermission(profile.this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(profile.this,new String[]{Manifest.permission.INTERNET},REQUEST_CALL);
                }else {
                    Picasso.with(getApplicationContext()).load(URL.ROOT_URL + "profileimages/" + phone_no + ".jpg").error(R.drawable.prologo).into(circleImageView);
                } hideDialog();
            }
        });
        call = (CircleImageView) findViewById(R.id.call_logo);
        whatsapp = (CircleImageView) findViewById(R.id.whatsapp_logo);



        call.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:" + phone_no));
                ////
                if (ContextCompat.checkSelfPermission(profile.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(profile.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
                }else {
                    startActivity(i);
                }
                ////
            }
        });
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.setData(Uri.parse("smsto:" + "00249116774943"));
                String whatsappPackage = "com.whatsapp";
                i.setPackage(whatsappPackage);
                if (isPackageExisted(whatsappPackage)) {
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "تأكد من وجود برامج Whatsapp", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //
    public void getdata() {
        if (URL.isNetworkConnected(getApplicationContext())) {
            if (!URL.isInternetAvailable()) {
                showDialog();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.GET_PROFILE_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    gender = jsonObject.getString("gender");
                                    dept = jsonObject.getString("dept");
                                    about = jsonObject.getString("about");
                                    if (gender == "null") {
                                        hideDialog();
                                        finish();
                                        Toast.makeText(getApplicationContext(), "حدث خطأ", Toast.LENGTH_SHORT).show();
                                        circleImageView.setImageResource(R.drawable.prologo);
                                    } else {
                                        Picasso.with(getApplicationContext()).load(URL.ROOT_URL + "profileimages/" + phone_no + ".jpg").error(R.drawable.prologo).into(circleImageView);
                                        txtgender.setText(gender);
                                        txtdept.setText(dept);
                                        txtphone.setText(phone_no);
                                        txtabout.setText(about);
                                        txtusername.setText(full_name);
                                        hideDialog();
                                    }

                                    hideDialog();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        Toast.makeText(getApplicationContext(), "تعذر الوصول للخادم", Toast.LENGTH_SHORT).show();
                        finish();
                        error.printStackTrace();

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("phone_no", phone_no);
                        return params;
                    }
                };
                MySingleton.getmInstance(profile.this).addToRequestque(stringRequest);
            } else {
                Toast.makeText(profile.this, "خطأ بالانترنت", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(profile.this, "لايوجد اتصال بالانترنت", Toast.LENGTH_SHORT).show();
            finish();
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

    //Check if whatsapp installing
    public boolean isPackageExisted(String targetPackage) {
        List<ApplicationInfo> packages;
        PackageManager pm;

        pm = getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.equals(targetPackage))
                return true;
        }
        return false;
    }
}

