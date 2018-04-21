package com.example.patil.todolist;

import android.app.AlarmManager;
import android.app.DatePickerDialog;

//import android.icu.util.Calendar;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CreateTask extends AppCompatActivity {
    EditText task, medit, tedit, priority, label;
    int yr, mnth, day, hr, min;
    Switch sw;

    DatabaseHelper mydb;
    int flag;
    Calendar mainCal;
    String monthname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        task = (EditText) findViewById(R.id.task);
        medit = (EditText) findViewById(R.id.editText);
        tedit = (EditText) findViewById(R.id.time);
        sw = (Switch) findViewById(R.id.switch3);
        priority = (EditText) findViewById(R.id.priority);
        label = (EditText) findViewById(R.id.label);

        // save.setEnabled(false);
        medit.setEnabled(false);
        tedit.setEnabled(false);

        Calendar cal = Calendar.getInstance();
        yr = cal.get(Calendar.YEAR);
        mnth = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        hr = cal.get(Calendar.HOUR_OF_DAY);
        min = cal.get(Calendar.MINUTE);
        mydb = new DatabaseHelper(this);


        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    medit.setEnabled(true);
                    tedit.setEnabled(true);
                    flag = 1;
                } else {
                    medit.setEnabled(false);
                    tedit.setEnabled(false);
                }
            }
        });


    }


    DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yr = year;
            mnth = month;
            day = dayOfMonth;

            dispDate();
        }
    };

    public void onDueDateClick(View view) {

        new DatePickerDialog(this, dpd, yr, mnth, day).show();


    }

    public void dispDate() {
        Calendar mycal = Calendar.getInstance();
        mycal.set(Calendar.MONTH, mnth);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM");
        monthname = simpleDateFormat.format(mycal.getTime());
        medit.setText(monthname + " " + day + ", " + yr);


    }

    TimePickerDialog.OnTimeSetListener ots = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
            hr = selectedHour;
            min = selectedMinute;
            dispTime();
        }
    };

    public void onDueTimeClick(View v) {
        new TimePickerDialog(this, ots, hr, min, true).show();

    }


    public void dispTime() {

        tedit.setText(hr + " : " + min);

    }


    public void onSaveClick(View view) {
        boolean status = false;
        String mytask = task.getText().toString();
        String mylabel = label.getText().toString();
        String p = priority.getText().toString();

        if (mytask.isEmpty() || mylabel.isEmpty() || p.isEmpty()) {
            Toast.makeText(this, "Data in some fields are missing", Toast.LENGTH_LONG).show();
            return;
        }
        int pri = Integer.parseInt(p);
        //////////////////////Authentication of input//////////////////////////////////////////////
        if (pri < 0 || pri > 10) {
            priority.setText("");
            Toast.makeText(this, "Priority should be between 0-10", Toast.LENGTH_LONG).show();
            return;
        }
        Calendar get_id = Calendar.getInstance();
        int id = (int) get_id.getTimeInMillis();

        if (flag == 1) {

            ////get diff id for each alarm///////////////////


            boolean b = notAuthentic();
            if (b) {
                medit.setText("");
                tedit.setText("");
                Toast.makeText(this, "Date is already passed", Toast.LENGTH_LONG).show();
                return;
            }

            //////////////////////Insert Data into database//////////////////////////////////////
            String mydate = monthname + " " + day + ", " + yr;
            String time = hr + ":" + min;
            status = mydb.insertData(id, mylabel, mytask, mydate, pri, time, id);


            ////////////////////////////////Set a broadcast service////////////////////////////////////////////
            String _id = Integer.toString(id);
            Intent receiverintent = new Intent(getApplicationContext(), MyReceiver.class);
            receiverintent.putExtra("label", mylabel);
            receiverintent.putExtra("id", _id);
            Toast.makeText(this, _id, Toast.LENGTH_LONG).show();
            PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), id, receiverintent, PendingIntent.FLAG_UPDATE_CURRENT);

            mainCal = Calendar.getInstance();
            mainCal.set(yr, mnth, day, hr, min);


            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, mainCal.getTimeInMillis(), pi);
        } else {
            status = mydb.insertData(id, mylabel, mytask, "NOT SET", pri, "NOT SET", 0);
        }
        if (status == true) {
            Toast.makeText(this, "Successfully Done!!!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Unsucessfull!!!", Toast.LENGTH_LONG).show();
        }


        //////////////////////Go to next activity///////////////////////////////////////
        Intent list = new Intent(this, MyList.class);
        startActivity(list);

    }

    public boolean notAuthentic() {
        boolean b;
        int yrr, mth, dayy, hour, minute;
        Calendar c = Calendar.getInstance();
        yrr = c.get(Calendar.YEAR);
        mth = c.get(Calendar.MONTH);
        dayy = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        if (yrr > yr || ((yrr == yr) && (mth > mnth)) || ((yrr == yr) && mth == mnth && (dayy > day)) || ((yrr == yr) && mth == mnth && (dayy == day) && (hr < hour))) {
            b = true;
        } else if ((yrr == yr) && mth == mnth && (dayy == day) && (hr == hour) && (minute > min)) {
            b = true;
        } else

        {
            b = false;
        }

        return b;
    }

}



