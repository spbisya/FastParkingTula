package com.okunev.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends ActionBarActivity {
    private Drawer.Result drawerResult = null;
    SharedPreferences sPref;
    SharedPreferences.Editor ed;
    MaterialEditText caredit;
    TextView tv2, tv3;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.material_drawer_primary_text));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        caredit = (MaterialEditText) findViewById(R.id.caredit);
        tv2 = (TextView) findViewById(R.id.textView2);
        tv3 = (TextView) findViewById(R.id.textView3);
        next = (Button) findViewById(R.id.button2);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, PayActivity.class);
                startActivity(intent);
            }
        });
        tv2.setText("Введите номер вашего автомобиля\n");
        tv2.setLines(2);
        tv3.setText("\nПри повторном использовании приложения номер автомобиля сохранится, и Вам больше не придётся вводить его заново.");
        caredit.setText(sPref.getString("number", ""));
        Bundle extras = getIntent().getExtras();
        Boolean need = false;
        if(extras!=null){
            need = extras.getBoolean("need");
        }
        if(!caredit.getText().toString().equals("")&!need){
            Intent intent;
            intent = new Intent(MainActivity.this, PayActivity.class);
            startActivity(intent);
        }
        caredit.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
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
                            case 1:
                                intent = new Intent(MainActivity.this, PayActivity.class);
                                startActivity(intent);
                                break;
                            case 2:
                                intent = new Intent(MainActivity.this, NotificationActivity.class);
                                startActivity(intent);
                                break;
                            case 6:
                                intent = new Intent(MainActivity.this, HistoryActivity.class);
                                startActivity(intent);
                                break;
                            case 8:
                                intent = new Intent(MainActivity.this, AboutActivity.class);
                                startActivity(intent);
                                break;
                            case 4:
                                intent = new Intent(MainActivity.this, MapActivity.class);
                                startActivity(intent);
                                break;
                        }
                    }
                })
                .build();
        drawerResult.getSlider().setBackgroundColor(getResources().getColor(R.color.material_drawer_background1));

    }

    public void onPause() {
        super.onPause();
        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        ed = sPref.edit();
        ed.putString("number", caredit.getText().toString());
        String number = caredit.getText().toString().replace(" ", "").replace("|", "");
        String end = number.substring(6);
        if(end.toCharArray()[0]=='0')end = end.substring(1);
        String negin = number.substring(0,6);
        ed.putString("carnumber", negin + end);
     //   Toast.makeText(this, caredit.getText().toString().replace(" ", "").replace("|", ""), Toast.LENGTH_LONG).show();
        ed.commit();
    }

    public void onResume() {
        super.onResume();
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
                    case 14:
                        caredit.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                        View view = getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        sPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        ed = sPref.edit();
                        ed.putString("number", caredit.getText().toString());
                        String number = caredit.getText().toString().replace(" ", "").replace("|", "");
                        String end = number.substring(6);
                        if(end.toCharArray()[0]=='0')end = end.substring(1);
                        String negin = number.substring(0,6);
                        ed.putString("carnumber", negin + end);
                        ed.commit();
                      //  Toast.makeText(MainActivity.this,negin + end,Toast.LENGTH_LONG).show();
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
