package com.okunev.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 777 on 2/8/2016.
 */
public class ViewPagerActivity extends ActionBarActivity {
    SharedPreferences sPref;
    SharedPreferences.Editor ed;
    MaterialEditText caredit;
    MaterialEditText park_num, hours;
    Button button;
    private String SENT_SMS_FLAG = "SENT_SMS";
    private String DELIVER_SMS_FLAG = "DELIVER_SMS";
    Boolean finished = true;
    MyReceiver myReceiver;
    Intent intent;
    public String title = "0 0 : 0 0 : 0 0";
    Boolean working = false;
    private Drawer.Result drawerResult = null;
    TextView textView3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        List<View> pages = new ArrayList<View>();

        View page = inflater.inflate(R.layout.fragment_screen_slide_page, null);
        TextView textView2 = (TextView) page.findViewById(R.id.textView2);
        textView2.setText("Введите номер вашего автомобиля\n");
        textView2.setLines(2);
        textView2.setVisibility(View.VISIBLE);
        textView3 = (TextView) page.findViewById(R.id.textView3);
        textView3.setText("\nПри повторном использовании приложения номер автомобиля сохранится, и Вам больше не придётся вводить его заново.");
        textView3.setVisibility(View.VISIBLE);
        ImageView imageView = (ImageView) page.findViewById(R.id.imageView);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.p1);
        caredit = (MaterialEditText) page.findViewById(R.id.caredit);
        caredit.setVisibility(View.VISIBLE);
        caredit.setText(sPref.getString("number", ""));
        caredit.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences.Editor ed;
                switch (s.toString().length()) {
                    case 1:
                        caredit.setText(caredit.getText() + " ");
                        caredit.setInputType(InputType.TYPE_CLASS_NUMBER);
                        caredit.setSelection(caredit.getText().length());
                        break;
                    case 5:
                        caredit.setText(caredit.getText() + " ");
                        caredit.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                        caredit.setSelection(caredit.getText().length());
                        break;
                    case 8:
                        caredit.setText(caredit.getText() + " | ");
                        caredit.setInputType(InputType.TYPE_CLASS_NUMBER);
                        caredit.setSelection(caredit.getText().length());
                        break;
                    case 13:
                        sPref = PreferenceManager.getDefaultSharedPreferences(ViewPagerActivity.this);
                        ed = sPref.edit();
                        ed.putString("number", caredit.getText().toString());
                        ed.putString("carnumber", caredit.getText().toString().replace(" ", "").replace("|", ""));
                        ed.commit();
                        break;
                    case 14:
                        caredit.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                        View view = getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        sPref = PreferenceManager.getDefaultSharedPreferences(ViewPagerActivity.this);
                        ed = sPref.edit();
                        ed.putString("number", caredit.getText().toString());
                        ed.putString("carnumber", caredit.getText().toString().replace(" ", "").replace("|", ""));
                        ed.commit();
                        break;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        caredit.addTextChangedListener(tw);
        caredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caredit.setText("");
            }
        });
        pages.add(page);

        page = inflater.inflate(R.layout.fragment_screen_slide_page, null);
        textView2 = (TextView) page.findViewById(R.id.textView2);
        textView2.setText("Выбор парковки и оплата\n");
        textView2.setLines(2);
        textView2.setVisibility(View.VISIBLE);
        textView3 = (TextView) page.findViewById(R.id.textView3);
        textView3.setText("\nВведите номер парковки и выберите длительность сессии.");
        textView3.setVisibility(View.VISIBLE);
        imageView = (ImageView) page.findViewById(R.id.imageView);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.header);
        park_num = (MaterialEditText) page.findViewById(R.id.parking_number_edit);
        hours = (MaterialEditText) page.findViewById(R.id.parking_hours_edit);
        button = (Button) page.findViewById(R.id.button);
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

        hours.setInputType(InputType.TYPE_CLASS_NUMBER);
        TextWatcher tw2 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                switch (s.toString().length()) {
                    case 2:
                        View view = getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

                        break;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        hours.addTextChangedListener(tw2);
        hours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hours.setText("");
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  ((MainActivity) getActivity()).sendSms("p71*" + park_num.getText().toString().replace(" ", "") + "*" +
                //       caredit.getText().toString().replace(" ", "").replace("|", "") + "*" + hours.getText().toString());
                String currDate = getCurrentTimeFormat("E', 'dd'/'MM'/'yy' 'kk':'mm");
                String future = getFutCurrentTimeFormat("E', 'dd'/'MM'/'yy' 'kk':'mm", 30);
                //((MainActivity) getActivity()).makeToast(currDate + "\n" + future);
                startParkingService(Integer.parseInt(hours.getText().toString()));
                button.setText("Продлить");
            }
        });
        pages.add(page);

        SamplePagerAdapter pagerAdapter = new SamplePagerAdapter(pages);
        final ViewPager viewPager = new ViewPager(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);
        setContentView(R.layout.activity_viewpager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.material_drawer_primary_text));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        intent = new Intent(this, ParkingService.class);
        drawerResult = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_car_number).withIcon(FontAwesome.Icon.faw_home).withIdentifier(0),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_pay).withIcon(FontAwesome.Icon.faw_gamepad).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_notifications).withIcon(FontAwesome.Icon.faw_eye).withIdentifier(2),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_map).withIcon(FontAwesome.Icon.faw_bell).withIdentifier(3),//4
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_history).withIcon(FontAwesome.Icon.faw_cog).withIdentifier(4),//6
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(FontAwesome.Icon.faw_question).withIdentifier(5)//8
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        Intent intent;
                        switch ((int) id) {
                            case 0:
                               viewPager.setCurrentItem(0);
                                break;
                            case 2:
                                intent = new Intent(ViewPagerActivity.this, NotificationActivity.class);
                                startActivity(intent);
                                break;
                            case 6:
                                intent = new Intent(ViewPagerActivity.this, HistoryActivity.class);
                                startActivity(intent);
                                break;
                            case 8:
                                intent = new Intent(ViewPagerActivity.this, AboutActivity.class);
                                startActivity(intent);
                                break;
                        }
                    }
                })
                .build();
        drawerResult.getSlider().setBackgroundColor(getResources().getColor(R.color.material_drawer_background1));
        drawerResult.setSelection(1);
        //   setContentView(viewPager);
    }


    public class SamplePagerAdapter extends PagerAdapter {

        List<View> pages = null;

        public SamplePagerAdapter(List<View> pages) {
            this.pages = pages;
        }

        @Override
        public Object instantiateItem(View collection, int position) {
            View v = pages.get(position);
            ((ViewPager) collection).addView(v, 0);
            return v;
        }

        @Override
        public void destroyItem(View collection, int position, Object view) {
            ((ViewPager) collection).removeView((View) view);
        }

        @Override
        public int getCount() {
            return pages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }

    public void startParkingService(int hours) {
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("SERV-ACT");
        registerReceiver(myReceiver, intentFilter);
        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        intent.putExtra("notif1", sPref.getBoolean("notif1",false));
        intent.putExtra("notif2", sPref.getBoolean("notif2", false));
        intent.putExtra("time1", sPref.getInt("time1", 5));
        intent.putExtra("time2", sPref.getInt("time2",5));
        intent.putExtra("hours", hours);
        ed = sPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(new Item(R.drawable.not, R.drawable.clock, park_num.getText().toString().replace(" ",""), "Таганская ул., г. Москва",
                getCurrentTimeFormat("E', 'dd'/'MM'/'yy' 'kk':'mm"), getFutCurrentTimeFormat("E', 'dd'/'MM'/'yy' 'kk':'mm",hours), "Оплачено", getAssets()));
        Set<String> data =new HashSet<>();
        data.addAll(sPref.getStringSet("Objects", new HashSet<String>()));
        data.add(json);
        ed.putStringSet("Objects", data);
        ed.commit();
        Toast.makeText(this, "" + sPref.getBoolean("notif1", false) + sPref.getBoolean("notif2", true) + sPref.getInt("time1", 5), Toast.LENGTH_LONG).show();
        startService(intent);
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
                    Toast.makeText(ViewPagerActivity.this, "Сервис остановлен", Toast.LENGTH_SHORT).show();
                    park_num.setText("");
                    hours.setText("");
                }
            }
        }
    }

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
        String number = sPref.getString("number","");
        String carnumber = sPref.getString("carnumber","");
        Set<String> data =new HashSet<>();
        data = sPref.getStringSet("Objects", new HashSet<String>());
        ed.clear();
        ed.commit();
        ed.putString("number", number);
        ed.putString("carnumber", carnumber);
        ed.putStringSet("Objects",data);
        ed.commit();
        super.onDestroy();
//        unregisterReceiver(sentReceiver);
        //    unregisterReceiver(deliverReceiver);

    }

}
