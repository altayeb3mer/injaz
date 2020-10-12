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
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity {
    Button signup, login, guest;
    TextView welcome, hint;
    EditText phone_no, password;
    String s_phone_no, s_password;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    SharePref sharePref;
    public  static  final int REQUEST_CALL=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharePref = new SharePref();
        if (sharePref.isLoggedin(MainActivity.this)) {
            if(!SharePref.get_phone_no(MainActivity.this).equals("guest")) {
                Intent intent = new Intent(MainActivity.this, dash.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone_no", sharePref.get_phone_no(MainActivity.this));
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        }
        //P_Dialog
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("جاري المعالجة..");
        progressDialog.setCancelable(false);


        builder = new AlertDialog.Builder(MainActivity.this);
        phone_no = (EditText) findViewById(R.id.phone_no);
        password = (EditText) findViewById(R.id.edtpassword);
        phone_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(phone_no.getText().length()!=0&&phone_no.getText().length()!=10){
                    phone_no.setError("رقم الهاتف يجب ان يتكون من 10 ارقام");
                }
            }
        });


        Typeface typeface = Typeface.createFromAsset(getAssets(), "one.ttf");
        welcome = (TextView) findViewById(R.id.welcome);
        hint = (TextView) findViewById(R.id.hint);
        signup = (Button) findViewById(R.id.btn_signup);
        login = (Button) findViewById(R.id.btn_login);
        guest = (Button) findViewById(R.id.btn_guest);

        welcome.setTypeface(typeface);
        hint.setTypeface(typeface);
        signup.setTypeface(typeface);
        login.setTypeface(typeface);
        guest.setTypeface(typeface);
        phone_no.setTypeface(typeface);
        password.setTypeface(typeface);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (URL.isNetworkConnected(getApplicationContext())) {
                    if (!URL.isInternetAvailable()) {
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, REQUEST_CALL);
                        } else {
                            // phone_no.addTextChangedListener(textWatcher);
                            s_phone_no = phone_no.getText().toString().trim();
                            s_password = password.getText().toString().trim();
                            if (s_phone_no.equals("") || s_password.equals("")) {
                                builder.setTitle("تنبيه !");
                                displayAlert("الرجاء عدم ترك الحقول فارغة");
                            } else {
                                showDialog();
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.LOGIN_URL,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONArray jsonArray = new JSONArray(response);
                                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                                    String code = jsonObject.getString("code");
                                                    if (code.equals("login_failed")) {
                                                        hideDialog();
                                                        builder.setTitle("خطأ");
                                                        displayAlert("اسم المستخدم او كلمة المرور غير صحيحة");
                                                    } else {
                                                        hideDialog();
                                                        Intent intent = new Intent(MainActivity.this, dash.class);
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString("phone_no", s_phone_no);
                                                        intent.putExtras(bundle);
                                                        startActivity(intent);
                                                        sharePref.saveUserInSharePref(s_phone_no, MainActivity.this);
                                                        finish();
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
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("phone_no", s_phone_no);
                                        params.put("password", s_password);
                                        return params;
                                    }
                                };
                                MySingleton.getmInstance(MainActivity.this).addToRequestque(stringRequest);
                            }
                        }}else{
                            Toast.makeText(MainActivity.this, "خطأ بالانترنت", Toast.LENGTH_SHORT).show();
                        }
                    }   else {
                    Toast.makeText(MainActivity.this, "لايوجد اتصال بالانترنت", Toast.LENGTH_SHORT).show();
                }

                //////
            }
        });
        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String guest="guest";
                Intent i = new Intent(getApplicationContext(), dash.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone_no", guest);
                SharePref.saveUserInSharePref(guest,MainActivity.this);
                i.putExtras(bundle);
                startActivity(i);
                finish();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(getApplicationContext(), registration.class);
                startActivity(ii);
            }
        });
    }

    public void displayAlert(String message) {
        builder.setMessage(message);
        builder.setPositiveButton("موافق", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                password.setText("");

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
