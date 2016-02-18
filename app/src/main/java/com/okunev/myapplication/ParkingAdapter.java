package com.okunev.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 777 on 1/16/2016.
 */
public class ParkingAdapter extends BaseAdapter {
    ArrayList<Item> items = new ArrayList<>();
    Context context;

    public ParkingAdapter(ArrayList<Item> items, Context context) {
        if (items != null) {
            this.items = items;
        }
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void remove(int position){items.remove(position); notifyDataSetChanged();}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_new, parent, false);
        }

        Typeface face = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");

        ImageView logo_parking = (ImageView)convertView.findViewById(R.id.logo_parking);
        logo_parking.setImageResource(items.get(position).getLogo_parking());
        ImageView logo_time = (ImageView)convertView.findViewById(R.id.logo_time);
        logo_time.setImageResource(items.get(position).getLogo_time());

        TextView parking_number = (TextView) convertView.findViewById(R.id.parking_number);
        parking_number.setText(items.get(position).getParking_number());
        parking_number.setTypeface(face);
        TextView parking_address = (TextView) convertView.findViewById(R.id.parking_address);
        parking_address.setText(items.get(position).getParking_address());
        parking_address.setTypeface(face);


        TextView parking_start_time = (TextView) convertView.findViewById(R.id.parking_start_time);
        parking_start_time.setText(items.get(position).getParking_start_time());
        parking_start_time.setTypeface(face);

        TextView parking_end_time = (TextView) convertView.findViewById(R.id.parking_end_time);
        parking_end_time.setText(items.get(position).getParking_end_time());
        parking_end_time.setTypeface(face);

        TextView status = (TextView) convertView.findViewById(R.id.parking_status);
        status.setText(items.get(position).getStatus());
        status.setTypeface(face);
        status.setTextColor((items.get(position).getStatus().equals("Оплачено")) ? Color.GREEN : Color.RED);

        TextView parking_end_text = (TextView) convertView.findViewById(R.id.parking_end_text);
        parking_end_text.setTypeface(face);
        TextView parking_start_text = (TextView) convertView.findViewById(R.id.parking_start_text);
        parking_start_text.setTypeface(face);

        return convertView;
    }
}
