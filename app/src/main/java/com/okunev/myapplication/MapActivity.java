package com.okunev.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 777 on 2/10/2016.
 */
public class MapActivity extends AppCompatActivity {
    private GoogleMap googleMap;
    private Drawer.Result drawerResult = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.title));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Карта парковок");



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
                                intent = new Intent(MapActivity.this, MainActivity.class);
                                intent.putExtra("need", true);
                                startActivity(intent);
                                break;
                            case 1:
                                intent = new Intent(MapActivity.this,PayActivity.class);
                                startActivity(intent);
                                break;
                            case 2:
                                intent = new Intent(MapActivity.this, NotificationActivity.class);
                                startActivity(intent);
                                break;
                            case 6:
                                intent = new Intent(MapActivity.this, HistoryActivity.class);
                                startActivity(intent);
                                break;
                            case 8:
                                intent = new Intent(MapActivity.this, AboutActivity.class);
                                startActivity(intent);
                                break;
                        }
                    }
                })
                .build();
        drawerResult.getSlider().setBackgroundColor(getResources().getColor(R.color.material_drawer_background1));
        drawerResult.setSelection(4);

        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().
                    findFragmentById(R.id.map1)).getMap();
        }
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        String in = loadJSONFromAsset();
        String errors = "";
        try {
            JSONObject reader = new JSONObject(in);
            JSONArray array = reader.getJSONArray("objects");
            int count = array.length();

            for(int i = 0;i<count;i++){
                try {
                    JSONObject first = array.getJSONObject(i);
                    JSONObject addr = first.getJSONObject("address");
                    JSONObject zone = first.getJSONObject("zone");
                    String num = zone.getString("number");
                    String street = addr.getString("street");
                    String house = addr.getString("house");
                    JSONObject second = first.getJSONObject("center");
                    JSONArray third = second.getJSONArray("coordinates");
                    String longt = third.getString(0);
                    String lat = third.getString(1);
                    LatLng coords = new LatLng(Double.parseDouble(lat), Double.parseDouble(longt));
                    googleMap.addMarker(new MarkerOptions().
                            position(coords).title("Номер: "+num+", "+street+", д"+house));
                }
                catch (Exception l){
                    errors+=""+i+" ";
                }
            }
        }
        catch (Exception l){
          //  Toast.makeText(this, "ERR", Toast.LENGTH_LONG).show();
        }
      //  Toast.makeText(this, errors, Toast.LENGTH_LONG).show();
        LatLng coords = new LatLng(54.193508, 37.602129);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(coords, 13.12f);
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(cameraUpdate);
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
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action) {

            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }
        else if (id == R.id.action1) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        else if (id == R.id.action2) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        else if (id == R.id.action3) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }
        return super.onOptionsItemSelected(item);
    }


    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("parking.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
