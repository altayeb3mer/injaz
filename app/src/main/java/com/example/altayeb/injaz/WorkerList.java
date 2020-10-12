package com.example.altayeb.injaz;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class WorkerList extends AppCompatActivity {
    private static final int REQUEST_CALL =1 ;
    public static ArrayList<String> profile, phone_no;
    ListView listView;
    public ArrayAdapter arrayAdapter;
    ProgressDialog progressDialog;
    String DEPT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_list);
        profile = new ArrayList<String>();
        phone_no = new ArrayList<String>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري المعالجة..");
        progressDialog.setCancelable(false);

        Bundle bundle = getIntent().getExtras();
        DEPT = bundle.getString("DEPT");
        Toast.makeText(WorkerList.this, DEPT, Toast.LENGTH_SHORT).show();

        listView = (ListView) findViewById(R.id.listview);
        if (ContextCompat.checkSelfPermission(WorkerList.this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(WorkerList.this,new String[]{Manifest.permission.INTERNET},REQUEST_CALL);
        }else {
        jsonArray();}





        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.textView_item);
                String phone_nomber = phone_no.get(position).toString().trim();
                String full_name = textView.getText().toString().trim();
                Intent i = new Intent(getApplicationContext(), profile.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone_no", phone_nomber);
                bundle.putString("full_name", full_name);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
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

    //Array Request
    private void jsonArray() {
        if (URL.isNetworkConnected(getApplicationContext())) {
            if (!URL.isInternetAvailable()) {
                showDialog();

                final HashMap<String, String> params = new HashMap<String, String>();
                params.put("dept", DEPT);
                final JSONObject jsonObject = new JSONObject(params);

                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, URL.ARY_URL,jsonObject, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = (JSONObject) response.get(i);
                                String full_name = object.getString("full_name");
                                String phone_number = object.getString("phone_no");
                                String message = object.getString("message");
                                if (message.equals("yes")) {
                                    profile.add(i,full_name.toString().trim());
                                    phone_no.add(i,phone_number.toString().trim());
                                } else {
                                    Toast.makeText(WorkerList.this, "عفوا القسم فارغ حتى الان", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(WorkerList.this, "لاتوجد بيانات في المصفوفة", Toast.LENGTH_SHORT).show();
                                hideDialog();
                                e.printStackTrace();
                            }
                        }
                        if(profile.isEmpty()){
                            Toast.makeText(WorkerList.this, "لم يتم التسجيل بهذا القسم بعد!", Toast.LENGTH_SHORT).show();
                            hideDialog();
                        }else{
                            arrayAdapter = new ArrayAdapter(WorkerList.this, R.layout.customlistview, R.id.textView_item, profile);
                            listView.setAdapter(arrayAdapter);
                            hideDialog();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "تعذر الوصول للخادم", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                        hideDialog();
                        finish();
                    }
                });
                MySingleton.getmInstance(WorkerList.this).addToRequestque(jsonArrayRequest);
            } else {
                Toast.makeText(WorkerList.this, "خطأ بالانترنت", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(WorkerList.this, "لايوجد اتصال بالانترنت", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onStart() {
      //  jsonArray();
        super.onStart();
    }

    @Override
    protected void onResume() {
       // jsonArray();
        super.onResume();
    }

    @Override
    protected void onRestart() {
      //  jsonArray();
        super.onRestart();
    }

    ////Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_btn, menu);




        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.map_view) {
            Intent i = new Intent(getApplication(), Maps.class);
            Bundle bundle = new Bundle();
            bundle.putString("GPS", "FALSE");
            i.putExtras(bundle);
            startActivity(i);
            return true;
        }


        return true;
    }
}