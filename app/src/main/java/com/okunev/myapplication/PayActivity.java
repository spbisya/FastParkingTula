package com.okunev.myapplication;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by 777 on 2/2/2016.
 */
public class PayActivity extends ActionBarActivity {
    private String SENT_SMS_FLAG = "SENT_SMS";
    private String DELIVER_SMS_FLAG = "DELIVER_SMS";
    Boolean finished = true;
    MyReceiver myReceiver;
    Intent intent;
    public String title = "0 0 : 0 0 : 0 0";
    private Drawer.Result drawerResult = null;
    MaterialEditText park_num;
    NiceSpinner hours;
    Button button;
    TextView tv2, tv3;
    SharedPreferences sPref;
    SharedPreferences.Editor ed;
    Boolean working = false;
    Integer time1 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.material_drawer_primary_text));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        intent = new Intent(this, ParkingService.class);

        //   ImageView imageView = (ImageView) findViewById(R.id.imageView);
//        imageView.setImageResource(R.drawable.p1);
        park_num = (MaterialEditText) findViewById(R.id.parking_number_edit);
        hours = (NiceSpinner) findViewById(R.id.parking_hours_edit);
        button = (Button) findViewById(R.id.button);
        tv2 = (TextView) findViewById(R.id.textView2);
        tv3 = (TextView) findViewById(R.id.textView3);

        park_num.setInputType(InputType.TYPE_CLASS_NUMBER);
        TextWatcher tw1 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                switch (s.toString().length()) {
                    case 1:
                        park_num.setText(park_num.getText() + "   ");
                        park_num.setSelection(park_num.getText().length());
                        break;
                    case 5:
                        park_num.setText(park_num.getText() + "   ");
                        park_num.setSelection(park_num.getText().length());
                        break;
                    case 9:
                        park_num.setText(park_num.getText() + "   ");
                        park_num.setSelection(park_num.getText().length());
                        break;
                    case 13:
                        hours.requestFocus();
                        View view = getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        break;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        park_num.addTextChangedListener(tw1);
        park_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                park_num.setText("");
            }
        });

        List<String> dataset = new LinkedList<>();
        for (int i = 1; i < 100; i++) {
            String it = "" + i;
            if (it.length() > 1) it = it.toCharArray()[0] + " " + it.toCharArray()[1];
            dataset.add("" + it + " ч");
        }
        hours.attachDataSource(dataset);
        hours.setTextColor(Color.BLACK);
        hours.setBackgroundColor(getResources().getColor(R.color.material_drawer_primary_text));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  ((MainActivity) getActivity()).sendSms("p71*" + park_num.getText().toString().replace(" ", "") + "*" +
                //       caredit.getText().toString().replace(" ", "").replace("|", "") + "*" + hours.getText().toString());
                String currDate = getCurrentTimeFormat("E', 'dd'/'MM'/'yy' 'kk':'mm");
                String future = getFutCurrentTimeFormat("E', 'dd'/'MM'/'yy' 'kk':'mm", 30);
                //((MainActivity) getActivity()).makeToast(currDate + "\n" + future);
                startParkingService(hours.getSelectedIndex() + 1);
                button.setText("Продлить");
            }
        });


        drawerResult = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_car_number).withIcon(getResources().getDrawable(R.drawable.drawer1)).withIdentifier(0),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_pay).withIcon(getResources().getDrawable(R.drawable.drawer2)).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_notifications).withIcon(getResources().getDrawable(R.drawable.drawer3)).withIdentifier(2),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_map).withIcon(getResources().getDrawable(R.drawable.drawer4)).withIdentifier(3),//4
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_history).withIcon(getResources().getDrawable(R.drawable.drawer5)).withIdentifier(4),//6
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(getResources().getDrawable(R.drawable.drawer6)).withIdentifier(5)//8
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        Intent intent;
                        switch ((int) id) {
                            case 0:
                                intent = new Intent(PayActivity.this, MainActivity.class);
                                intent.putExtra("need", true);
                                startActivity(intent);
                                break;
                            case 4:
                                intent = new Intent(PayActivity.this, MapActivity.class);
                                startActivity(intent);
                                break;
                            case 2:
                                intent = new Intent(PayActivity.this, NotificationActivity.class);
                                startActivity(intent);
                                break;
                            case 6:
                                intent = new Intent(PayActivity.this, HistoryActivity.class);
                                startActivity(intent);
                                break;
                            case 8:
                                intent = new Intent(PayActivity.this, AboutActivity.class);
                                startActivity(intent);
                                break;
                        }
                    }
                })
                .build();
        drawerResult.getSlider().setBackgroundColor(getResources().getColor(R.color.material_drawer_background1));
        drawerResult.setSelection(1);

    }

    public void startParkingService(int hours) {
        time1 = hours;
        String text = "p71*" + park_num.getText().toString().replace(" ", "") + "*" + sPref.getString("carnumber", "") + "*" + hours;
        sendSms(text);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PayActivity.this);
        alertDialog.setTitle(" ");
        alertDialog.setMessage("Запрос на оплату парковки отправлен");

        alertDialog.setIcon(R.drawable.ios_thumb);

        alertDialog.setNegativeButton("ОКЕЙ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
        button.setEnabled(false);
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            title = arg1.getStringExtra("title");
            working = arg1.getBooleanExtra("working", true);
            finished = arg1.getBooleanExtra("finished", false);
            if (finished) {
                Boolean r = stopService(intent);
                if (r) {
                    Toast.makeText(PayActivity.this, "Сервис остановлен", Toast.LENGTH_SHORT).show();
                    park_num.setText("");
                    tv3.setText("\nВведите номер парковки и выберите длительность сессии");
                }
            }
        }
    }

    public void onPause() {
        super.onPause();
        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        ed = sPref.edit();
        ed.putString("park_num", park_num.getText().toString());
        ed.commit();

    }

    public void onResume() {
        super.onResume();
        park_num.setText(sPref.getString("park_num", ""));
    }

    public void sendSms(String text) {
        registerReceiver(sentReceiver, new IntentFilter(SENT_SMS_FLAG));
        registerReceiver(deliverReceiver, new IntentFilter(DELIVER_SMS_FLAG));
        Intent sentIn = new Intent(SENT_SMS_FLAG);
        final PendingIntent sentPIn = PendingIntent.getBroadcast(this, 0,
                sentIn, 0);
        Intent deliverIn = new Intent(DELIVER_SMS_FLAG);
        final PendingIntent deliverPIn = PendingIntent.getBroadcast(this, 0,
                deliverIn, 0);
        SmsManager smsManager = SmsManager.getDefault();
        // отправляем сообщение
        smsManager.sendTextMessage("9878", null, text, sentPIn, deliverPIn);

    }

    public void sendSms1(String text) {
        SmsManager smsManager = SmsManager.getDefault();
        // отправляем сообщение
        smsManager.sendTextMessage("9878", null, text, null, null);
    }

    public void sendSms2(String text) {
        SmsManager smsManager = SmsManager.getDefault();
        // отправляем сообщение
        smsManager.sendTextMessage("8464", null, text, null, null);
    }

    BroadcastReceiver sentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent in) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    // sent SMS message successfully;
                 //   Toast.makeText(PayActivity.this, "SENT!", Toast.LENGTH_LONG).show();
                    break;
                default:
                    // sent SMS message failed
                //    Toast.makeText(PayActivity.this, "not SENT!", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    BroadcastReceiver deliverReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent in) {
            // SMS delivered actions
            Gson gson;
            String json;
            Set<String> data;
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    // sent SMS message successfully;
                   // Toast.makeText(PayActivity.this, "Оплачено, ЕБАТЬ МЕНЯ В РОТ!!!", Toast.LENGTH_LONG).show();
                    myReceiver = new MyReceiver();
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction("SERV-ACT");
                    registerReceiver(myReceiver, intentFilter);
                    sPref = PreferenceManager.getDefaultSharedPreferences(PayActivity.this);
                    intent.putExtra("notif1", sPref.getBoolean("notif1", false));
                    intent.putExtra("notif2", sPref.getBoolean("notif2", false));
                    intent.putExtra("time1", sPref.getInt("time1", 5));
                    intent.putExtra("time2", sPref.getInt("time2", 5));
                    intent.putExtra("hours", time1);
                  //  Toast.makeText(PayActivity.this, "DELIVERED!", Toast.LENGTH_LONG).show();
                    String currDate = getCurrentTimeFormat("E', 'dd'/'MM'/'yy' 'kk':'mm");
                    String future = getFutCurrentTimeFormat("E', 'dd'/'MM'/'yy' 'kk':'mm", 30);
                    ed = sPref.edit();
                    gson = new Gson();
                    json = gson.toJson(new Item(R.drawable.not, R.drawable.clock, park_num.getText().toString().replace(" ", ""), "г. Тула",
                            getCurrentTimeFormat("E', 'dd'/'MM'/'yy' 'kk':'mm"), getFutCurrentTimeFormat("E', 'dd'/'MM'/'yy' 'kk':'mm", time1), "Оплачено", getAssets()));
                    data = new HashSet<>();
                    data.addAll(sPref.getStringSet("Objects", new HashSet<String>()));
                    data.add(json);
                    ed.putStringSet("Objects", data);
                    ed.commit();
                    startService(intent);
                    CountDownTimer  countDownTimer = new CountDownTimer(10000, 1000) {
                        public void onTick(final long millisUntilFinished) {
                        }

                        public void onFinish() {
                            sendSms2("1");
                          //  Toast.makeText(PayActivity.this, "Отправил я твоё сообщение!", Toast.LENGTH_LONG).show();
                        }
                    }.start();
                    button.setEnabled(true);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendSms1("p71*c");
                            Boolean r = stopService(intent);
                            if (r) {
                                startParkingService(PayActivity.this.hours.getSelectedIndex() + 1);
                            } else
                                Toast.makeText(PayActivity.this, "Сервис всё ещё останавливается, подождите 3 секунды", Toast.LENGTH_LONG).show();
                        }
                    });
                    unregisterReceiver(sentReceiver);
                    unregisterReceiver(deliverReceiver);
                    //    Calendar cal1 = Calendar.getInstance().getTimeInMillis()+hours;
                    //  history.add(new Item(R.drawable.not, R.drawable.clock,park_num,address,,,"Оплачено",getAssets()));
                    break;
                default:
                    // sent SMS message failed
                    Toast.makeText(PayActivity.this, "Не оплачено!!", Toast.LENGTH_LONG).show();
                    ed = sPref.edit();
                    gson = new Gson();
                    json = gson.toJson(new Item(R.drawable.not, R.drawable.clock, park_num.getText().toString().replace(" ", ""), "г. Тула",
                            getCurrentTimeFormat("E', 'dd'/'MM'/'yy' 'kk':'mm"), getFutCurrentTimeFormat("E', 'dd'/'MM'/'yy' 'kk':'mm", time1), "Не оплачено", getAssets()));
                    data = new HashSet<>();
                    data.addAll(sPref.getStringSet("Objects", new HashSet<String>()));
                    data.add(json);
                    ed.putStringSet("Objects", data);
                    ed.commit();
                    button.setEnabled(true);
                    unregisterReceiver(sentReceiver);
                    unregisterReceiver(deliverReceiver);
                    break;
            }
        }
    };

    public String getCurrentTimeFormat(String timeFormat) {
        String time = "";
        SimpleDateFormat df = new SimpleDateFormat(timeFormat);
        Calendar c = Calendar.getInstance();
        time = df.format(c.getTime());
        return time;
    }

    public String getFutCurrentTimeFormat(String timeFormat, int hours) {
        String time = "";
        SimpleDateFormat df = new SimpleDateFormat(timeFormat);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, hours);
        time = df.format(c.getTime());
        return time;
    }

    @Override
    public void onBackPressed() {
        if (drawerResult.isDrawerOpen()) {
            drawerResult.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public void onDestroy() {
        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        ed = sPref.edit();
        String number = sPref.getString("number", "");
        String carnumber = sPref.getString("carnumber", "");
        Set<String> data = new HashSet<>();
        data = sPref.getStringSet("Objects", new HashSet<String>());
        ed.clear();
        ed.commit();
        ed = sPref.edit();
        ed.putString("number", number);
        ed.putString("carnumber", carnumber);
        ed.putStringSet("Objects", data);
        ed.commit();
        super.onDestroy();
//        unregisterReceiver(sentReceiver);
        //    unregisterReceiver(deliverReceiver);

    }
}
