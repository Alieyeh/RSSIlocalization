package com.example.nazanin.finalproject.model.DTO;

/**
 * Created by Alieyeh on 6/8/20.
 */

import android.arch.persistence.room.*;


@Entity(primaryKeys= {"lon", "lat"})
public class UMTS {

    @ColumnInfo(name = "lon")
    private double lon;

    @ColumnInfo(name = "lat")
    private double lat;

    @ColumnInfo(name = "lac")
    private int lac;

    @ColumnInfo(name = "rac")
    private int rac;

    @ColumnInfo(name = "power")
    private int power;
//
//    @ColumnInfo(name = "ecn0")
//    private int ecn0;

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public int getLac() {
        return lac;
    }

    public void setLac(int lac) {
        this.lac = lac;
    }

    public int getRac() {
        return rac;
    }

    public void setRac(int rac) {
        this.rac = rac;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }
//
//    public int getEcn0() {
//        return ecn0;
//    }
//
//    public void setEcn0(int ecn0) {
//        this.ecn0 = ecn0;
//    }
}
