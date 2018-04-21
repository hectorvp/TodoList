package com.example.patil.todolist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MyList extends AppCompatActivity {
    DatabaseHelper mydb;
    Map<Integer,String> id_label;
    Map<Integer,String>pri_label;
    Map<String,Integer>label_id;
    ListView listview;
    ArrayAdapter<String> adapter;
    ArrayList<String> al;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);




        mydb=new DatabaseHelper(this);
        id_label=new TreeMap<Integer,String>();
        pri_label=new TreeMap<Integer,String>();
        label_id=new TreeMap<String,Integer>();
        showList();
        registerForContextMenu(listview);
        onListClick();
    }
    public void getLabels()
    {
        al=new ArrayList<String>() ;

        Cursor cur=mydb.getLabels();
        if(cur.getCount()==0)
        {
            Toast.makeText(this,"Nothing entered",Toast.LENGTH_LONG).show();;

        }
        else
        {

            int count=cur.getCount();
            String s=Integer.toString(count);
            Toast.makeText(this,s,Toast.LENGTH_LONG).show();


            do {
                al.add(cur.getString(1));
                label_id.put(cur.getString(1),cur.getInt(0));
                id_label.put(cur.getInt(0),cur.getString(1));
                pri_label.put(cur.getInt(2),cur.getString(1));
            }while (cur.moveToNext());
        }



    }

    public void showList()
    {
        getLabels();
        //String label[]=getLabels();
        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,al);

        // Get ListView object from xml
        listview = (ListView) findViewById(R.id.listview);

        // Assign adapter to ListView
        listview.setAdapter(adapter);
    }

    public void onListClick()
    {
        listview.setOnItemClickListener(
                new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String item=String.valueOf(parent.getItemAtPosition(position));
                        int _id=label_id.get(item);
                        String myid=Integer.toString(_id);
                        Intent i=new Intent(MyList.this,DisplayInfo.class);
                        i.putExtra("label",item);
                        i.putExtra("id",myid);
                        startActivity(i);


                    }
                }
        );
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        menu.add(0, v.getId(), 0, "Edit");
        menu.add(0, v.getId(), 0, "Delete");
        menu.add(0,v.getId(),0,"Sort by priority");
        menu.add(0,v.getId(),0,"Sort by Id");
    }

    public boolean onContextItemSelected(MenuItem item){
        if(item.getTitle()=="Edit") {



            Toast.makeText(getApplicationContext(), "Functionality yet to be added", Toast.LENGTH_LONG).show();
        }
        if(item.getTitle()=="Delete")
        {
            AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int pos=info.position;
            String labl=(String)listview.getItemAtPosition(pos);

            int i=label_id.get(labl);
            int id=mydb.getPendingIntent(i);

            Intent in=new Intent(getApplicationContext(),MyReceiver.class);
            PendingIntent pi=PendingIntent.getBroadcast(getApplicationContext(),id,in,PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
            am.cancel(pi);
            pi.cancel();
            mydb.deleteEntry(i);


            al.remove(pos);
            //adapter.
            adapter.notifyDataSetChanged();

            Toast.makeText(getApplicationContext(), "Deleted Sucessfully", Toast.LENGTH_LONG).show();
        }
        else if(item.getTitle()=="Sort by priority")
        {
            al.clear();
            for(Map.Entry<Integer,String> it:pri_label.entrySet())
            {
                String label=it.getValue();
                al.add(label);
            }
            adapter.notifyDataSetChanged();;

        }
        else if(item.getTitle()=="Sort by Id")
        {
            al.clear();
            for(Map.Entry<Integer,String> it:id_label.entrySet())
            {
                String label=it.getValue();
                al.add(label);
            }
            adapter.notifyDataSetChanged();;
        }
        return true;
    }


}

