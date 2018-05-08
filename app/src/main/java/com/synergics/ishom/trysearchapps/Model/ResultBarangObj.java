package com.synergics.ishom.trysearchapps.Model;

public class ResultBarangObj {

    private int id;
    private String image;
    private String name;
    private int harga;

    public ResultBarangObj(int id, String image, String name, int harga) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.harga = harga;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }
}
