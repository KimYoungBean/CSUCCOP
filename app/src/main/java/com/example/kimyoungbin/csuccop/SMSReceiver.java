package com.example.kimyoungbin.csuccop;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

/**
 * Created by Kim youngbin on 2017-08-06.
 */

public class SMSReceiver extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle=intent.getExtras();
        Object[] pdus=(Object[])bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[pdus.length];
        for(int i=0; i<pdus.length; i++){
            messages[i]=SmsMessage.createFromPdu((byte[])pdus[i]);
            try{
                String message = new String(messages[i].getMessageBody());
                String phoneNumber = messages[i].getOriginatingAddress();

                NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                builder.setSmallIcon(android.R.drawable.ic_notification_overlay);
                builder.setContentTitle("New SMS Message");
                builder.setContentText(message);
                builder.setAutoCancel(true);
                Notification noti = builder.build();
                manager.notify(111,noti);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
