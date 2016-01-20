package com.okunev.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

import java.util.ArrayList;

/**
 * Created by 777 on 1/16/2016.
 */
public class HistoryActivity extends ActionBarActivity {
    private Drawer.Result drawerResult = null;
    private ArrayList<Item> data = new ArrayList<>();
    private DynamicListView listView;
    ParkingAdapter adapter = new ParkingAdapter(data, this);

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
        data.add(new Item(R.drawable.not, R.drawable.clock, "2040", "Таганская ул., г. Москва",
                "Понедельник, 14/01/16 10:50", "Понедельник, 14/01/16 11:50", "Оплачено", getAssets()));
        data.add(new Item(R.drawable.not, R.drawable.clock, "2040", "Таганская ул., г. Москва",
                "Понедельник, 14/01/16 10:50", "Понедельник, 14/01/16 10:50", "Отменено", getAssets()));
        data.add(new Item(R.drawable.not, R.drawable.clock, "2040", "Таганская ул., г. Москва",
                "Понедельник, 14/01/16 10:50", "Понедельник, 14/01/16 10:50", "Оплачено", getAssets()));
        data.add(new Item(R.drawable.not, R.drawable.clock, "2040", "Таганская ул., г. Москва",
                "Понедельник, 14/01/16 10:50", "Понедельник, 14/01/16 11:50", "Отменено", getAssets()));

        adapter = new ParkingAdapter(data, this);
        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(adapter);
        animationAdapter.setAbsListView(listView);
        listView.setAdapter(animationAdapter);


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
                                intent = new Intent(HistoryActivity.this, MainActivity.class);
                                startActivity(intent);
                                break;
                            case 8:
                                intent = new Intent(HistoryActivity.this, AboutActivity.class);
                                startActivity(intent);
                                break;
                        }
                    }
                })
                .build();
        drawerResult.getSlider().setBackgroundColor(getResources().getColor(R.color.material_drawer_background1));


    }

    @Override
    public void onBackPressed() {
        if (drawerResult.isDrawerOpen()) {
            drawerResult.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

}
