package com.example.concertapplication.ui.recycleItem;

import com.example.concertapplication.R;

import java.sql.Time;
import java.sql.Timestamp;

public class RecycleItem {

    private int id;
    private String imageResource;
    private String text1;
    private String text2;
    private String baseURL;
    private int price;
    private double lat;
    private double lng;
    private String timestamp;
    private int orderId;

    public RecycleItem(int id, String imageResource, String text1, String text2, int price, double lat, double lng, String timestamp) {
        this.id = id;
        this.imageResource = "https://concert-backend-heroku.herokuapp.com" + imageResource;
        this.text1 = text1;
        this.text2 = text2;
        this.price = price;
        this.lat = lat;
        this.lng = lng;
        this.timestamp = timestamp;
        orderId = -1;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "RecycleItem{" +
                "id=" + id +
                ", imageResource='" + imageResource + '\'' +
                ", text1='" + text1 + '\'' +
                ", text2='" + text2 + '\'' +
                ", baseURL='" + baseURL + '\'' +
                ", price=" + price +
                ", lat=" + lat +
                ", lng=" + lng +
                ", timestamp='" + timestamp + '\'' +
                ", orderId=" + orderId +
                '}';
    }
}
