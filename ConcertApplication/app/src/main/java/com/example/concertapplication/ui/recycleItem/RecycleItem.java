package com.example.concertapplication.ui.recycleItem;

import com.example.concertapplication.R;

public class RecycleItem {

    private int id;
    private String imageResource;
    private String text1;
    private String text2;
    private String baseURL;
    private int price;

    public RecycleItem(int id, String imageURL, String text1, String text2, int price){
        this.id = id;
        this.imageResource = "http://10.0.2.2:8080" + imageURL;
        this.text1 = text1;
        this.text2 = text2 + "\nprice: " + price;
        this.price = price;
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

    @Override
    public String toString() {
        return "RecycleItem{" +
                "id=" + id +
                ", imageResource='" + imageResource + '\'' +
                ", text1='" + text1 + '\'' +
                ", text2='" + text2 + '\'' +
                ", price=" + price +
                '}';
    }
}
