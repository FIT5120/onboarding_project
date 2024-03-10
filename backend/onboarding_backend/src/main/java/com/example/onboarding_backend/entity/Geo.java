package com.example.onboarding_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("VIC_lat_lng")
public class Geo {
    @TableId("id")
    private int id;
    @TableField("suburb")
    private String suburb;
    @TableField("lat")
    private double lat;
    @TableField("lon")
    private double lon;

    public Geo(int id, String suburb, double lat, double lon) {
        this.id = id;
        this.suburb = suburb;
        this.lat = lat;
        this.lon = lon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "Geo{" +
                "id=" + id +
                ", suburb='" + suburb + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
