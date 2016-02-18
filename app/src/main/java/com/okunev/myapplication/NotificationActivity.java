package com.okunev.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 777 on 1/17/2016.
 */
public class NotificationActivity extends ActionBarActivity {
    private Drawer.Result drawerResult = null;
    SharedPreferences sPref;
    SharedPreferences.Editor ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_notifications);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.title));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Настройка уведомлений");
        NiceSpinner niceSpinner = (NiceSpinner) findViewById(R.id.nice_spinner);
        NiceSpinner niceSpinner1 = (NiceSpinner) findViewById(R.id.nice_spinner1);
        List<String> dataset = new LinkedList<>(Arrays.asList("5 мин", "10 мин", "15 мин", "20 мин"));
        niceSpinner.attachDataSource(dataset);
        niceSpinner.setTextColor(Color.BLACK);
        niceSpinner.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sPref = PreferenceManager.getDefaultSharedPreferences(NotificationActivity.this);
                ed = sPref.edit();
                switch (position) {
                    case 0:
                        ed.putInt("time1", 5);
                        break;
                    case 1:
                        ed.putInt("time1", 10);
                        break;
                    case 2:
                        ed.putInt("time1", 15);
                        break;
                    case 3:
                        ed.putInt("time1", 20);
                        break;
                }
                ed.commit();
            }
        });
        niceSpinner1.attachDataSource(dataset);
        niceSpinner1.setTextColor(Color.BLACK);
        niceSpinner1.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sPref = PreferenceManager.getDefaultSharedPreferences(NotificationActivity.this);
                ed = sPref.edit();
                switch (position) {
                    case 0:
                        ed.putInt("time2", 5);
                        break;
                    case 1:
                        ed.putInt("time2", 10);
                        break;
                    case 2:
                        ed.putInt("time2", 15);
                        break;
                    case 3:
                        ed.putInt("time2", 20);
                        break;
                }
                ed.commit();
            }
        });
        SwitchButton push = (SwitchButton)findViewById(R.id.push);
        SwitchButton push1 = (SwitchButton)findViewById(R.id.push1);
        push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sPref = PreferenceManager.getDefaultSharedPreferences(NotificationActivity.this);
                ed = sPref.edit();
                ed.putBoolean("notif1",isChecked);
                ed.commit();
            }
        });
        push1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sPref = PreferenceManager.getDefaultSharedPreferences(NotificationActivity.this);
                ed = sPref.edit();
                ed.putBoolean("notif2",isChecked);
                ed.commit();
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
                                intent = new Intent(NotificationActivity.this, MainActivity.class);
                                intent.putExtra("need", true);
                                 startActivity(intent);
                                break;
                            case 1:
                                intent = new Intent(NotificationActivity.this, PayActivity.class);
                                 startActivity(intent);
                                break;
                            case 6:
                                intent = new Intent(NotificationActivity.this, HistoryActivity.class);
                                startActivity(intent);
                                break;
                            case 8:
                                intent = new Intent(NotificationActivity.this, AboutActivity.class);
                                startActivity(intent);
                                break;
                            case 4:
                                intent = new Intent(NotificationActivity.this,MapActivity.class);
                                startActivity(intent);
                                break;
                        }
                    }
                })
                .build();
        drawerResult.getSlider().setBackgroundColor(getResources().getColor(R.color.material_drawer_background1));
drawerResult.setSelection(2);
        Typeface face = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
        TextView textView4 = (TextView)findViewById(R.id.textView4);
        TextView textView5 = (TextView)findViewById(R.id.textView5);
        TextView textView6 = (TextView)findViewById(R.id.textView6);
        TextView textView7 = (TextView)findViewById(R.id.textView7);
        TextView textView9 = (TextView)findViewById(R.id.textView9);
        TextView textView11 = (TextView)findViewById(R.id.textView11);
        textView4.setTypeface(face);
        textView5.setTypeface(face);
        textView6.setTypeface(face);
        textView7.setTypeface(face);
        textView9.setTypeface(face);
        textView11.setTypeface(face);

    }
}
