package com.example.karan.winedine;

/**
 * Created by karan on 03/30/2017.
 */

public class CartTemp {
    public String id;
    public String content;
    public String details;
    public String url;

    public CartTemp(String id, String content, String details, String url) {
        this.id = id;
        this.content = content;
        this.details = details;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return id+" "+content+" "+details;
    }
}
