package com.budyfriend_code.song.model;

public class dataSong {
    private String key;

    private String title;
    private String description;
    private String img;
    private long raiting;

    public dataSong() {
    }

    public dataSong(String title, String description, String img, long raiting) {
        this.title = title;
        this.description = description;
        this.img = img;
        this.raiting = raiting;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImg() {
        return img;
    }

    public long getRaiting() {
        return raiting;
    }
}
