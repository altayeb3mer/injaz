package com.example.altayeb.injaz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Layout_print_translate extends AppCompatActivity {
    private static String[] depts_array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_print_translate);
    }

    public void onclick(View view) {
        depts_array = getResources().getStringArray(R.array.dept);
        for (int i = 0; i < depts_array.length; i++) {
            String id = "A" + String.valueOf(i);
            if (view.getTag().toString().equals(id)) {
                Intent intent =new Intent(getApplicationContext(),WorkerList.class);
                Bundle bundle=new Bundle();
                bundle.putString("DEPT",depts_array[i].trim());
                intent.putExtras(bundle);
                startActivity(intent);
                Toast.makeText(Layout_print_translate.this, depts_array[i], Toast.LENGTH_SHORT).show();
            }}
    }
}
