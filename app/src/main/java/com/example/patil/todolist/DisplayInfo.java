package com.example.patil.todolist;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class DisplayInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_info);
        show();
    }
    public void show()
    {
        String label=getIntent().getExtras().getString("label");
        String id=getIntent().getExtras().getString("id");
        int myid=Integer.parseInt(id);
        TextView l=(TextView)findViewById(R.id.label);
        TextView _id=(TextView)findViewById(R.id.myid);
        TextView task=(TextView)findViewById(R.id.task);
        TextView Datee=(TextView)findViewById(R.id.Datee);
        TextView priority=(TextView)findViewById(R.id.priority);
        TextView time=(TextView)findViewById(R.id.time);

        DatabaseHelper mydb=new DatabaseHelper(this);
        Cursor cur=mydb.getInfo(myid);

        l.setText(label);
        _id.setText(id);

        task.setText(cur.getString(2));
        Datee.setText(cur.getString(3));
        priority.setText(cur.getInt(4)+"");
        time.setText(cur.getString(5));

       /* l.setEnabled(false);
        _id.setEnabled(false);
        task.setEnabled(false);
        time.setEnabled(false);
        Datee.setEnabled(false);
        priority.setEnabled(false);
    */


    }
}
