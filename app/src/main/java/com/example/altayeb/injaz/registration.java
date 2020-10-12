package com.example.altayeb.injaz;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class registration extends AppCompatActivity {
    private static final int REQUEST_CALL =1 ;
    EditText phone_no, full_name, password, repassword;
    String s_phone_no, s_full_name, s_password, s_repassword;
    Button signup;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    SharePref sharePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        //P_Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري المعالجة..");
        progressDialog.setCancelable(false);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "prp.TTF");
        phone_no = (EditText) findViewById(R.id.phone_no);
        full_name = (EditText) findViewById(R.id.full_name);
        password = (EditText) findViewById(R.id.edt_password);
        repassword = (EditText) findViewById(R.id.edt_confirmPassword);
        signup = (Button) findViewById(R.id.btn_signup);
        signup.setTypeface(typeface);
        builder = new AlertDialog.Builder(registration.this);
        builder.setCancelable(false);
        phone_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(phone_no.getText().length()!=0&&phone_no.getText().length()!=10){
                    phone_no.setError("رقم الهاتف يجب ان يتكون من 10 ارقام");
                }
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(password.getText().length()<6||password.getText().length()>10){
                    password.setError("مسموح للمدى من 8 الى 10");
                }
            }
        });

        full_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(full_name.getText().length()<15||full_name.getText().length()>25){
                    full_name.setError("مسموح للمدى من 15 الي 25 حرف");
                }
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (URL.isNetworkConnected(getApplicationContext())) {
                    if (!URL.isInternetAvailable()) {
                s_phone_no = phone_no.getText().toString().trim();
                s_full_name = full_name.getText().toString().trim();
                s_password = password.getText().toString().trim();
                s_repassword = repassword.getText().toString().trim();
                if (s_phone_no.equals("") || s_full_name.equals("") || s_password.equals("") || s_repassword.equals("")) {
                    builder.setTitle("تنبيه !");
                    displayAlert("الرجاء عدم ترك الحقول فارغة");
                    displayAlert("input_error");
                } else {

                    if (!(s_password.equals(s_repassword))) {
                        builder.setTitle("تنبيه !");
                        builder.setMessage("كلمة المرور غير متطابقة");
                        displayAlert("input_error");
                    } else {
                        showDialog();
                        if (ContextCompat.checkSelfPermission(registration.this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(registration.this,new String[]{Manifest.permission.INTERNET},REQUEST_CALL);
                        }else {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.REG_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONArray jsonArray = new JSONArray(response);
                                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                                            String code = jsonObject.getString("code");
                                            if(code.equals("reg_success")) {
                                                builder.setTitle("نجاح");
                                                builder.setMessage(getResources().getString(R.string.thanena));
                                                displayAlert(code);
                                            }
                                            else
                                            {
                                                builder.setTitle("خطأ");
                                                builder.setMessage("هذا المستخم موجود بالفعل");
                                                displayAlert("reg_failed");
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
                                Toast.makeText(registration.this, "تعذر الوصول للخادم", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("phone_no", s_phone_no);
                                params.put("full_name", s_full_name);
                                params.put("password", s_password);
                                return params;
                            }
                        };
                        MySingleton.getmInstance(registration.this).addToRequestque(stringRequest);
                        ////
                    }}


                    ////////////////
                }
                    } else {
                        Toast.makeText(registration.this, "خطأ بالانترنت", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(registration.this, "لايوجد اتصال بالانترنت", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void displayAlert(final String code) {
        builder.setPositiveButton("موافق", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (code.equals("input_error")) {
                    password.setText("");
                    repassword.setText("");
                } else if (code.equals("reg_success")) {
                    sharePref = new SharePref();
                    sharePref.saveUserInSharePref(s_phone_no,registration.this);
                    sharePref.save_full_name(s_full_name,registration.this);
                    Intent i =new Intent(getApplicationContext(),myProfile.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("phone_no",s_phone_no);
                    bundle.putString("Activity","registration");
                    i.putExtras(bundle);
                    startActivity(i);
                    finish();

                } else if (code.equals("reg_failed")) {
                    phone_no.setText("");
                    full_name.setText("");
                    password.setText("");
                    repassword.setText("");
                }
            }
        });
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
