package com.example.altayeb.injaz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AboutUs extends AppCompatActivity {
    CircleImageView call;
    TextView gw, our_scopes, contact, aboutusphone;
    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "mesaad.ttf");
        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "sultan.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "one.ttf");


        gw = (TextView) findViewById(R.id.gw);
        our_scopes = (TextView) findViewById(R.id.our_scope);
        contact = (TextView) findViewById(R.id.contact_us);
        aboutusphone = (TextView) findViewById(R.id.aboutus_phone_no);
        gw.setTypeface(typeface);
        our_scopes.setTypeface(typeface2);
        contact.setTypeface(typeface1);
        aboutusphone.setText("0116774943");
        final String phone_no = aboutusphone.getText().toString().trim();
        aboutusphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:" + phone_no));
                ////
                if (ContextCompat.checkSelfPermission(AboutUs.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AboutUs.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                } else {
                    startActivity(i);
                }
            }
        });
        call = (CircleImageView) findViewById(R.id.call_logo_aboutus);
        call.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:" + phone_no));
                ////
                if (ContextCompat.checkSelfPermission(AboutUs.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AboutUs.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                } else {
                    startActivity(i);
                }
                ////
            }
        });


    }
}
