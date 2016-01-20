package com.okunev.myapplication;

import android.content.res.AssetManager;

/**
 * Created by 777 on 1/16/2016.
 */
public class Item {
    int logo_parking, logo_time;
    String parking_number, parking_address,
            parking_start_time, parking_end_time, status;
    public AssetManager assets;

    public Item(int logo_parking, int logo_time, String parking_number,
                String parking_address, String parking_start_time,
                String parking_end_time, String status, AssetManager assets) {
        this.logo_parking = logo_parking;
        this.logo_time = logo_time;
        this.parking_number = parking_number;
        this.parking_address = parking_address;
        this.parking_start_time = parking_start_time;
        this.parking_end_time = parking_end_time;
        this.status = status;
        this.assets = assets;
    }

    public AssetManager getAssets() {
        return assets;
    }

    public void setAssets(AssetManager assets) {
        this.assets = assets;
    }

    public int getLogo_parking() {
        return logo_parking;
    }

    public void setLogo_parking(int logo_parking) {
        this.logo_parking = logo_parking;
    }

    public int getLogo_time() {
        return logo_time;
    }

    public void setLogo_time(int logo_time) {
        this.logo_time = logo_time;
    }

    public String getParking_number() {
        return parking_number;
    }

    public void setParking_number(String parking_number) {
        this.parking_number = parking_number;
    }

    public String getParking_address() {
        return parking_address;
    }

    public void setParking_address(String parking_address) {
        this.parking_address = parking_address;
    }

    public String getParking_start_time() {
        return parking_start_time;
    }

    public void setParking_start_time(String parking_start_time) {
        this.parking_start_time = parking_start_time;
    }

    public String getParking_end_time() {
        return parking_end_time;
    }

    public void setParking_end_time(String parking_end_time) {
        this.parking_end_time = parking_end_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
