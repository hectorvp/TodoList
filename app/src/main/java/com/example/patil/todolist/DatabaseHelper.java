package com.example.patil.todolist;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.Calendar;

import static android.R.attr.datePickerDialogTheme;
import static android.R.attr.version;

/**
 * Created by patil on 20-09-2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="todo.db";
    public static final String TABLE_NAME="todo_table";
    public static final String COL_1="id";
    public static final String COL_2="label";
    public static final String COL_3="task";
    public static final String COL_4="Datee";
    public static final String COL_5="priority";
    public static final String COL_6="time";
    public static final String COL_7="pIntentId";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null,1);


    }

    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("Create table todo_table (id INTEGER primary key ,label TEXT,task TEXT,Datee TEXT,priority INTEGER,time TEXT,pIntentId INTEGER)");
    }
    public void onUpgrade(SQLiteDatabase db,int oldversion,int newversion)
    {
        db.execSQL("drop if TABLE exists todo_table");
        onCreate(db);
    }
    public boolean insertData(int id,String label,String task,String Date,int priority,String time,int pIntentId)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        Calendar cal=Calendar.getInstance();
       //int id=(int) cal.getTimeInMillis();
        cv.put(COL_1,id);
        cv.put(COL_2,label);
        cv.put(COL_3,task);
        cv.put(COL_4,Date);
        cv.put(COL_5,priority);
        cv.put(COL_6,time);
        cv.put(COL_7,pIntentId);
       long status= db.insert(TABLE_NAME,null,cv);
        if(status==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
public Cursor getLabels()
{
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cur=db.rawQuery("Select id,label,priority from "+TABLE_NAME,null);
    cur.moveToFirst();
    return cur;
}


    public Cursor getInfo(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur=db.rawQuery("Select * from "+TABLE_NAME+" where id="+id,null);
        cur.moveToFirst();
        return cur;
    }
    public int getPendingIntent(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c=db.rawQuery("Select "+COL_7+" from "+TABLE_NAME+" where id="+id,null);
        c.moveToFirst();
        int pi_id=c.getInt(0);
        return pi_id;

    }
    public void deleteEntry(int id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
         db.delete(TABLE_NAME,"id ="+id,null) ;
    }




}
