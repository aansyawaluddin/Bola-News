package com.example.afinal.models;

import com.google.gson.annotations.SerializedName;

public class NewsModel {

    @SerializedName("description")
    private String description;

    @SerializedName("headline")
    private String headline;

    @SerializedName("published")
    private String published;

    @SerializedName("link")
    private String link;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
