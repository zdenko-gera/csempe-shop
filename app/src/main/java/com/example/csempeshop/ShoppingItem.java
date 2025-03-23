package com.example.csempeshop;

public class ShoppingItem {
    private String title;
    private String info;
    private String price;
    private int imageResource;

    public ShoppingItem() {    }

    public ShoppingItem(String title, String info, String price, int imageResource) {
        this.imageResource = imageResource;
        this.price = price;
        this.info = info;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getInfo() {
        return info;
    }

    public String getPrice() {
        return price;
    }

    public int getImageResource() {
        return imageResource;
    }
}
