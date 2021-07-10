package com.dapm.android.dapm_inm;

import com.google.firebase.database.Exclude;

public class Inmueble {
    @Exclude
            private String key; //exclude para que la base de datos omita esta variable cuando se recuperen los datos

    int mMeters;
    int mN_Rooms;
    int mFloors;
    int mPrice;

    String mType;
    String mAddress;
    String mAdq;
    String mImg;

    public Inmueble() {
    }

    public Inmueble(int meters, int n_Rooms, int floors, int price, String type, String address, String adq, String img) {
        mMeters = meters;
        mN_Rooms = n_Rooms;
        mFloors = floors;
        mPrice = price;
        mType = type;
        mAddress = address;
        mAdq = adq;
        mImg = img;
    }

    public int getMeters() {
        return mMeters;
    }

    public void setMeters(int meters) {
        mMeters = meters;
    }

    public int getN_Rooms() {
        return mN_Rooms;
    }

    public void setN_Rooms(int n_Rooms) {
        mN_Rooms = n_Rooms;
    }

    public int getFloors() {
        return mFloors;
    }

    public void setFloors(int floors) {
        mFloors = floors;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getAdq() {
        return mAdq;
    }

    public void setAdq(String adq) {
        mAdq = adq;
    }

    public String getImg() {
        return mImg;
    }

    public void setImg(String img) {
        mImg = img;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }
}
