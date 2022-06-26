package com.faislll.myapplication.model;

import java.io.Serializable;

public class Reseps implements Serializable {
    private String id_resep, nama_menu, cara_memasak, bahan, url_image, deskripsi, id_user;

    public Reseps() {
    }

    public Reseps(String id_resep, String nama_menu, String cara_memasak, String bahan, String url_image, String deskripsi, String id_user) {
        this.id_resep = id_resep;
        this.nama_menu = nama_menu;
        this.cara_memasak = cara_memasak;
        this.bahan = bahan;
        this.url_image = url_image;
        this.deskripsi = deskripsi;
        this.id_user = id_user;
    }

    public String getId_resep() {
        return id_resep;
    }

    public void setId_resep(String id_resep) {
        this.id_resep = id_resep;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public void setNama_menu(String nama_menu) {
        this.nama_menu = nama_menu;
    }

    public String getCara_memasak() {
        return cara_memasak;
    }

    public void setCara_memasak(String cara_memasak) {
        this.cara_memasak = cara_memasak;
    }

    public String getBahan() {
        return bahan;
    }

    public void setBahan(String bahan) {
        this.bahan = bahan;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }
}