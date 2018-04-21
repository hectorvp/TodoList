package com.example.patil.todolist;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by patil on 22-09-2017.
 */

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String label=intent.getExtras().getString("label");
        String id=intent.getExtras().getString("id");
        Intent i=new Intent(context,DisplayInfo.class);
        i.putExtra("label",label);
        i.putExtra("id",id);
        PendingIntent pi=PendingIntent.getActivity(context,0,i,0);
        Notification notify= new Notification.Builder(context)
                            .setTicker("TickerTitle")
                            .setContentTitle(label)
                            .setSmallIcon(R.drawable.ic_stat_name)
                            .setLights(Color.RED,1,1)
                            .setVibrate(new long[] { 1000, 0})
                            .setContentText(label)

                            .setSound(alarmSound)
                            .setContentIntent(pi).getNotification();
        notify.flags=Notification.FLAG_AUTO_CANCEL;
        NotificationManager nm= (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0,notify);


        Toast.makeText(context,"Alarm is triggered",Toast.LENGTH_LONG).show();
    }
}
