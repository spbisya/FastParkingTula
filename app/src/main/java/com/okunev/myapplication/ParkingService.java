package com.okunev.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

public class ParkingService extends Service {
    private boolean notif1, notif2;
    private int time1, time2, hours;
    private int NOTIFICATION = 81237;
    private int NOTIFICATION2 = 81238;
    private int NOTIFICATION3 = 81239;
    private int NOTIFICATION4 = 81240;
    private CountDownTimer countDownTimer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
notif1 = intent.getBooleanExtra("notif1", false);
        notif2 = intent.getBooleanExtra("notif2", false);
        time1 = intent.getIntExtra("time1", 15);
        time2 = intent.getIntExtra("time2", 15);
        hours = intent.getIntExtra("hours", 1);
        long millisec = hours*60*60*1000;
       // long millisec = hours*60*1000;
//        Notification notification = new Notification.Builder(getApplicationContext())
//                .setContentTitle("Парковка оплачена")
//                .setContentText("Работаем...")
//                .setSmallIcon(R.drawable.ic_launcher)
//                .build();
//        notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
//        NotificationManager notifier = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//        notifier.notify(NOTIFICATION, notification);
        startTimer(millisec);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        try {
            countDownTimer.cancel();
        } catch (Exception l) {
        }
        super.onDestroy();
    }


    private void startTimer(final long millisec) {
            countDownTimer = new CountDownTimer(millisec, 1000) {
                public void onTick(final long millisUntilFinished) {
                    if(notif1&&(millisUntilFinished/1000/60)==time1-1)setNotif(time1);
                    if(notif2&&(millisUntilFinished/1000/60)==time2-1)setNotif2(time2);
                    long hours = millisUntilFinished / 1000 / 60 / 60;
                    long minutes = (millisUntilFinished - hours * 60 * 60 * 1000) / 1000 / 60;
                    long seconds = (millisUntilFinished - minutes * 60 * 1000 - hours * 60 * 60 * 1000) / 1000;
                    String hour = "" + hours;
                    String minute = "" + minutes;
                    String second = "" + seconds;
                    if (hour.length() < 2) hour = "0" + hour;
                    if (minute.length() < 2) minute = "0" + minute;
                    if (second.length() < 2) second = "0" + second;
                   String c3BarTitle = "" + hour.toCharArray()[0]+" "+ hour.toCharArray()[1] + " : " + minute.toCharArray()[0]+" "+
                           minute.toCharArray()[1] + " : " + second.toCharArray()[0]+" "+second.toCharArray()[1];
                    sendData(false,true);
                    setNotif1(c3BarTitle);
                }

                public void onFinish() {
                    ((NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE)).cancel(NOTIFICATION);
                    sendData(true, false);
                    NotificationManager notifier = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notifier.cancelAll();
                }
            }.start();
    }

    public void setNotif(int minutes){
        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentTitle("Парковка оплачена")
                .setContentText("До конца осталось "+minutes+" минут!")
                .setSmallIcon(R.drawable.ic_menu_white_48dp)
                .build();
        NotificationManager notifier = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notifier.notify(NOTIFICATION2, notification);
    }

    public void setNotif1(String minutes){
        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentTitle("Парковка оплачена")
                .setContentText("До конца осталось "+minutes+" минут!")
                .setSmallIcon(R.drawable.ic_menu_white_48dp)
                .build();
        notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
        NotificationManager notifier = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notifier.notify(NOTIFICATION4, notification);
    }

    public void setNotif2(int minutes){
        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentTitle("Парковка оплачена")
                .setContentText("До конца осталось "+minutes+" минут!")
                .setSmallIcon(R.drawable.ic_menu_white_48dp)
                .build();
        NotificationManager notifier = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notifier.notify(NOTIFICATION3, notification);
    }

    public void sendData(Boolean finished, Boolean working) {
        Intent intent = new Intent();
        intent.setAction("SERV-ACT");
        intent.putExtra("finished", finished);
        intent.putExtra("working",working);
        sendBroadcast(intent);
    }
}
