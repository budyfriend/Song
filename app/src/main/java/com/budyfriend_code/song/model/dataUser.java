package com.budyfriend_code.song.model;

public class dataUser {
    private String id;

    private String username;
    private String jk;
    private String no_hp;
    private String alamat;
    private String tanggal_lahir;
    private String tanggal_buat;
    private boolean vote;

    public dataUser() {
    }

    public dataUser(String username, String jk, String no_hp, String alamat, String tanggal_lahir, String tanggal_buat,boolean vote) {
        this.username = username;
        this.jk = jk;
        this.no_hp = no_hp;
        this.alamat = alamat;
        this.tanggal_lahir = tanggal_lahir;
        this.tanggal_buat = tanggal_buat;
        this.vote = vote;
    }

    public String getTanggal_buat() {
        return tanggal_buat;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getJk() {
        return jk;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getTanggal_lahir() {
        return tanggal_lahir;
    }

    public boolean isVote() {
        return vote;
    }
}
