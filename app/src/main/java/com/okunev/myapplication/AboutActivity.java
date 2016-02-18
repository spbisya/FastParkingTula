package com.okunev.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

/**
 * Created by 777 on 1/16/2016.
 */
public class AboutActivity extends ActionBarActivity {

    private Drawer.Result drawerResult = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.title));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("О разработчиках");


        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.p1);
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
                                intent = new Intent(AboutActivity.this, MainActivity.class);
                                intent.putExtra("need", true);
                                startActivity(intent);
                                break;
                            case 1:
                                intent = new Intent(AboutActivity.this, PayActivity.class);
                                startActivity(intent);
                                break;
                            case 2:
                                intent = new Intent(AboutActivity.this, NotificationActivity.class);
                                startActivity(intent);
                                break;
                            case 6:
                                intent = new Intent(AboutActivity.this, HistoryActivity.class);
                                startActivity(intent);
                                break;
                            case 4:
                                intent = new Intent(AboutActivity.this,MapActivity.class);
                                startActivity(intent);
                                break;
                        }
                    }
                })
                .build();
        drawerResult.getSlider().setBackgroundColor(getResources().getColor(R.color.material_drawer_background1));
        drawerResult.setSelection(8);

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
