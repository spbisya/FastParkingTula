package com.okunev.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by 777 on 1/16/2016.
 */
public class HistoryActivity extends ActionBarActivity {
    private Drawer.Result drawerResult = null;
    private ArrayList<Item> data = new ArrayList<>();
    private DynamicListView listView;
    ParkingAdapter adapter = new ParkingAdapter(data, this);
    SharedPreferences sPref;
    SharedPreferences.Editor ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        listView = (DynamicListView) findViewById(R.id.historyView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.title));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("История парковки");
//        data.add(new Item(R.drawable.not, R.drawable.clock, "2040", "Таганская ул., г. Москва",
//                "Понедельник, 14/01/16 10:50", "Понедельник, 14/01/16 11:50", "Оплачено", getAssets()));
//        data.add(new Item(R.drawable.not, R.drawable.clock, "2040", "Таганская ул., г. Москва",
//                "Понедельник, 14/01/16 10:50", "Понедельник, 14/01/16 10:50", "Отменено", getAssets()));
//        data.add(new Item(R.drawable.not, R.drawable.clock, "2040", "Таганская ул., г. Москва",
//                "Понедельник, 14/01/16 10:50", "Понедельник, 14/01/16 10:50", "Оплачено", getAssets()));
//        data.add(new Item(R.drawable.not, R.drawable.clock, "2040", "Таганская ул., г. Москва",
//                "Понедельник, 14/01/16 10:50", "Понедельник, 14/01/16 11:50", "Отменено", getAssets()));
        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        for(String s : sPref.getStringSet("Objects",new HashSet<String>())){
            Gson gson = new Gson();
            String json = s;
            Item mItemObject = gson.fromJson(json, Item.class);
            data.add(mItemObject);
        }
     //   sPref.getString("Object1", "");
        adapter = new ParkingAdapter(data, this);
        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(adapter);
        animationAdapter.setAbsListView(listView);
        listView.setAdapter(animationAdapter);


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
                            case 2:
                                intent = new Intent(HistoryActivity.this, NotificationActivity.class);
                                startActivity(intent);
                                break;
                            case 1:
                                intent = new Intent(HistoryActivity.this, PayActivity.class);
                                startActivity(intent);
                                break;
                            case 0:
                                intent = new Intent(HistoryActivity.this, MainActivity.class);
                                intent.putExtra("need", true);
                                startActivity(intent);
                                break;
                            case 8:
                                intent = new Intent(HistoryActivity.this, AboutActivity.class);
                                startActivity(intent);
                                break;
                            case 4:
                                intent = new Intent(HistoryActivity.this,MapActivity.class);
                                startActivity(intent);
                                break;
                        }
                    }
                })
                .build();
        drawerResult.getSlider().setBackgroundColor(getResources().getColor(R.color.material_drawer_background1));
        drawerResult.setSelection(6);

    }

    @Override
    public void onBackPressed() {
        if (drawerResult.isDrawerOpen()) {
            drawerResult.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            ed = sPref.edit();
            ed.remove("Objects");
            ed.commit();
            data = null;
            adapter = new ParkingAdapter(data, this);
            AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(adapter);
            animationAdapter.setAbsListView(listView);
            listView.setAdapter(animationAdapter);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
