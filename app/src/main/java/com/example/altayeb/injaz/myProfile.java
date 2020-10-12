package com.example.altayeb.injaz;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class myProfile extends AppCompatActivity {
    //permission var
    private static final int REQUEST_CALL = 1;

    String[] arraydept, arraygender;
    Spinner spinner;
    Spinner gender_spinner;
    AlertDialog.Builder builder;
    private CircleImageView circleImageView;
    private Button imageupload, savedata;
    private TextView phone_no, location, myfull_name;
    private final int IMG_REQUEST = 1;
    private Bitmap bitmap, resizedbitmap;
    private EditText edtdescribe;
    String phone_nomber, new_phone_no, gender, dept, about, full_name, Activity_value;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    ProgressDialog progressDialog;
    SharePref sharePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile);
        //Android V6 permission

        Bundle bundle = getIntent().getExtras();
        phone_nomber = bundle.getString("phone_no");
        Activity_value = bundle.getString("Activity");

        circleImageView = (CircleImageView) findViewById(R.id.Mcimgview);
        edtdescribe = (EditText) findViewById(R.id.describe);
        phone_no = (TextView) findViewById(R.id.phone); //TODO must be nomber only
        myfull_name = (TextView) findViewById(R.id.full_name);
        location = (TextView) findViewById(R.id.location);

        edtdescribe.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(edtdescribe.getText().length()>5&&edtdescribe.getText().length()>100){
                    edtdescribe.setError("غير مسموح بأكثر من 100حرف");
                }
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Maps.class);
                Bundle bundle = new Bundle();
                bundle.putString("GPS", "TRUE");
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        arraygender = getResources().getStringArray(R.array.gender);
        arraydept = getResources().getStringArray(R.array.dept);

        sharePref = new SharePref();

        //P_Dialog
        progressDialog = new ProgressDialog(myProfile.this);
        progressDialog.setMessage("جاري المعالجة..");
        progressDialog.setCancelable(false);

        spinner = (Spinner) findViewById(R.id.dept);
        setSpinnerdept();
        gender_spinner = (Spinner) findViewById(R.id.spinnerGender);
        setSpinnergender();
        if (dash.visit == false) {
            if (sharePref.isMyprofileSaved(myProfile.this)) {

                showDialog();
                /////
                if (ContextCompat.checkSelfPermission(myProfile.this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(myProfile.this,new String[]{Manifest.permission.INTERNET},REQUEST_CALL);
                }else {
                    Picasso.with(getApplicationContext()).load(URL.ROOT_URL + "profileimages/" + phone_nomber + ".jpg").error(R.drawable.prologo).into(circleImageView);



                    myfull_name.setText(sharePref.get_full_name(myProfile.this));
                    edtdescribe.setText(sharePref.get_about(myProfile.this));

                    for (int i = 0; i < arraygender.length; i++) {
                        if (sharePref.get_gender(myProfile.this).equals(arraygender[i])) {
                            gender_spinner.setSelection(i);
                        }
                    }
                    for (int i = 0; i < arraydept.length; i++) {
                        if (sharePref.get_dept(myProfile.this).equals(arraydept[i])) {
                            spinner.setSelection(i);
                        }
                    }
                    hideDialog();
                }

                /////////

            } else {
                if (ContextCompat.checkSelfPermission(myProfile.this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(myProfile.this,new String[]{Manifest.permission.INTERNET},REQUEST_CALL);
                }else {
                    getmyprofiledata();
            }}


            dash.visit = true;
        }
        //location
        location = (TextView) findViewById(R.id.location);
        //

        savedata = (Button) findViewById(R.id.savedata);
        savedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(myProfile.this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(myProfile.this,new String[]{Manifest.permission.INTERNET},REQUEST_CALL);
                }else {
                updateprofile();
            }}
        });

        //user name from bundle
        phone_no.setText(phone_nomber);
        //spinner

        //radio
        //////////
        builder = new AlertDialog.Builder(myProfile.this);
        circleImageView = (CircleImageView) findViewById(R.id.Mcimgview);
        imageupload = (Button) findViewById(R.id.imageupload);
        /////
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle("تغيير صورة");
                displayAlert("هل تريد اختيار صورة من الاستديو");

            }
        });
        imageupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(myProfile.this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(myProfile.this,new String[]{Manifest.permission.INTERNET},REQUEST_CALL);
                }else {
                uploadImage();
            }}
        });
        ////
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        //Check sharePrefrences


    }


    //Select image from gellary
    private void selectImg() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (ContextCompat.checkSelfPermission(myProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(myProfile.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CALL);
        }else {
        startActivityForResult(intent, IMG_REQUEST);
    }}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                resizedbitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, false);
                circleImageView.setImageBitmap(resizedbitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                "myProfile Page", // TODO: Define a title for the content shown.
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
                "myProfile Page", // TODO: Define a title for the content shown.
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
    ////Uploading an images

    private void uploadImage() {
        ////
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.IMG_UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String Response = jsonObject.getString("response");
                            Toast.makeText(getApplicationContext(), Response, Toast.LENGTH_SHORT).show();
                            //hide save buttom
                            if (Response.equals("Image upload successfully")) {
                                imageupload.setVisibility(View.GONE);
                                hideDialog();
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
                Map<String, String> params = new HashMap<>();
                params.put("phone_no", phone_nomber);
                params.put("image", imgTostring(resizedbitmap));
                return params;
            }
        };
        MySingleton.getmInstance(getApplicationContext()).addToRequestque(stringRequest);
    }

    /////

    //Convert image to string
    private String imgTostring(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    public void displayAlert(String message) {
        builder.setMessage(message);
        builder.setPositiveButton("موافق", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ContextCompat.checkSelfPermission(myProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(myProfile.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CALL);
                }else {
                selectImg();
                imageupload.setVisibility(View.VISIBLE);}
            }
        }).setNegativeButton("رجوع", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    //Spinner handler DEPT
    public void setSpinnerdept() {
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraydept);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dept = arraydept[position].toString().trim();
                // Toast.makeText(myProfile.this,position + spinnerArray.get(position),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner.setSelection(0);
            }
        });
    }

    //Spinner handler GENDER
    public void setSpinnergender() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraygender);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_spinner.setAdapter(adapter);
        gender_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = arraygender[position].toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner.setSelection(0);
            }
        });
    }

    //updateprofile
    public void updateprofile() {
        ////
        new_phone_no = phone_no.getText().toString().trim();
        about = edtdescribe.getText().toString().trim();
        if (gender.equals(arraygender[0]) || dept.equals(arraydept[0]) || phone_no.equals("") || about.equals("")) {
            Toast.makeText(getApplicationContext(), "خطأ..الحقول فارغه!", Toast.LENGTH_SHORT).show();
        } else {
            showDialog();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.UPDATE_PROFILE_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String Response = jsonObject.getString("result");
                                if (Response.equals("yes")) {
                                    sharePref.save_profile(new_phone_no, gender, dept, about, myProfile.this);
                                    sharePref.save_full_name(myfull_name.getText().toString().trim(),myProfile.this);
                                    hideDialog();
                                    Toast.makeText(getApplicationContext(), "تم التحديث بنجاح", Toast.LENGTH_SHORT).show();
                                    if (Activity_value.equals("registration")) {
                                        Intent intent = new Intent(myProfile.this, dash.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("phone_no", new_phone_no);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                } else {
                                    hideDialog();
                                    Toast.makeText(myProfile.this, "لم يتم التحديث", Toast.LENGTH_SHORT).show();
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
                    Map<String, String> params = new HashMap<>();
                    params.put("phone_no", new_phone_no);
                    params.put("gender", gender);
                    params.put("dept", dept.trim());
                    params.put("about", about);
                    return params;
                }
            };
            MySingleton.getmInstance(getApplicationContext()).addToRequestque(stringRequest);
        }
    }

    //Get Profile
    public void getmyprofiledata() {
        if (URL.isNetworkConnected(getApplicationContext())) {
            if (!URL.isInternetAvailable()) {
                showDialog();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.GET_MY_PROFIL_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    gender = jsonObject.getString("gender");
                                    dept = jsonObject.getString("dept");
                                    about = jsonObject.getString("about");
                                    full_name = jsonObject.getString("full_name");
                                    if (dept == "null") {
                                        hideDialog();
                                        Toast.makeText(getApplicationContext(), "يجب تحديث ملفك الشخصي", Toast.LENGTH_SHORT).show();
                                        circleImageView.setImageResource(R.drawable.prologo);
                                    } else {
                                        Picasso.with(getApplicationContext()).load(URL.ROOT_URL + "profileimages/" + phone_nomber + ".jpg").error(R.drawable.prologo).into(circleImageView);

                                        if (!dept.equals("null")) {
                                            for (int i = 0; i < arraygender.length; i++) {
                                                if (gender.equals(arraygender[i])) {
                                                    gender_spinner.setSelection(i);
                                                }
                                            }
                                            for (int ii = 0; ii < arraydept.length; ii++) {
                                                if (dept.equals(arraydept[ii])) {
                                                    spinner.setSelection(ii);
                                                }
                                            }
                                            edtdescribe.setText(about);
                                        }
                                        myfull_name.setText(full_name);
                                        hideDialog();
                                    }

                                    hideDialog();
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
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("phone_no", SharePref.get_phone_no(myProfile.this));
                        return params;
                    }
                };
                MySingleton.getmInstance(myProfile.this).addToRequestque(stringRequest);
            } else {
                Toast.makeText(myProfile.this, "خطأ بالانترنت", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(myProfile.this, "لايوجد اتصال بالانترنت", Toast.LENGTH_SHORT).show();
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


    @Override
    protected void onPause() {
        arraydept = null;
        arraygender = null;
        phone_nomber = null;
        new_phone_no = null;
        super.onPause();
    }

    @Override
    protected void onResume() {
        arraydept = getResources().getStringArray(R.array.dept);
        arraygender = getResources().getStringArray(R.array.gender);
        Bundle bundle = getIntent().getExtras();
        phone_nomber = bundle.getString("phone_no");
        new_phone_no = phone_no.getText().toString().trim();
        super.onResume();
    }

}

