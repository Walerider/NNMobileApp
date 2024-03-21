package com.walerider.nnmobileapp;

public class FavoriteItem {
    private int mainImage;
    private String coordinates;
    private String title;
    private String address;

    public FavoriteItem(int mainImage, String coordinates, String title, String address) {
        this.mainImage = mainImage;
        this.coordinates = coordinates;
        this.title = title;
        this.address = address;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public int getMainImage() {
        return mainImage;
    }

    public void setMainImage(int mainImage) {
        this.mainImage = mainImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
