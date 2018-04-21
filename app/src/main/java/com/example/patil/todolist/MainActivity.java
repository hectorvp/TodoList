package com.example.patil.todolist;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // getSupportActionBar().setElevation(0);
        Button btn = (Button) findViewById(R.id.viewTask);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTaskClick(v);
            }
        });

    }
    public void onAddClick(View v)
    {
        Intent i=new Intent(this,CreateTask.class);
        startActivity(i);
    }

    public void onTaskClick(View view)
    {
        Intent intent=new Intent(this,MyList.class);
        startActivity(intent);
    }

}
