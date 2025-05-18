package com.example.csempeshop;

public class ShoppingItem {
    private String id;
    private String title;
    private String info;
    private String price;
    private int imageResource;
    private boolean inSale;

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
    public String _getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public boolean getInSale() {
        return inSale;
    }

    public void setInSale(boolean inSale) {
        this.inSale = inSale;
    }
}
