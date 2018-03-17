package com.vp.firebaseapp;

import android.app.ListActivity;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;
import android.content.Intent;

public class TermDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Semester");
        ListView listDrinks = (ListView)findViewById(R.id.list);
        Intent intent = getIntent();
        int position = intent.getIntExtra("hello",0);
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> ListView, View view, int position, long id) {
                Intent intent = new Intent(TermDetailActivity.this, final_activity.class);

                int sem = position+1;
                intent.putExtra("sem",sem);

                startActivity(intent);
            }
        };

        listDrinks.setOnItemClickListener(itemClickListener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}
