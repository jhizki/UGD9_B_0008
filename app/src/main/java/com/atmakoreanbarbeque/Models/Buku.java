package com.atmakoreanbarbeque.Models;

import java.io.Serializable;

public class Buku implements Serializable {
    private int id_menu;
    private String nama_kategori, nama_bahan, nama_menu, deskripsi_menu, unit_menu;
    private Double harga_menu;

    public Buku (int id_menu, String nama_kategori, String nama_bahan, String nama_menu, String deskripsi_menu, String unit_menu, Double harga_menu) {
        this.id_menu = id_menu;
        this.nama_kategori = nama_kategori;
        this.nama_bahan = nama_bahan;
        this.nama_menu = nama_menu;
        this.deskripsi_menu = deskripsi_menu;
        this.unit_menu = unit_menu;
        this.harga_menu = harga_menu;
    }

    public Buku (String nama_menu, String deskripsi_menu, Double harga_menu) {
        this.nama_menu = nama_menu;
        this.deskripsi_menu = deskripsi_menu;
        this.harga_menu = harga_menu;
    }

    public Double getHarga() {
        return harga_menu;
    }

    public int getIdMenu() {
        return id_menu;
    }

    public String getNamaMenu() {
        return nama_menu;
    }

    public String getUnitMenu() {
        return unit_menu;
    }

    public String getDeskripsiMenu() {
        return deskripsi_menu;
    }

    public String getNamaKategori() {
        return nama_kategori;
    }

    public String getNamaBahan() {
        return nama_bahan;
    }
}
