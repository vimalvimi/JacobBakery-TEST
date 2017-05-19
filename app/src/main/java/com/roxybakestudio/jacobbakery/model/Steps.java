package com.roxybakestudio.jacobbakery.model;


import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Steps implements Serializable {

    @Expose
    private String  id;
    @Expose
    private String shortDescription;
    @Expose
    private String description;
    @Expose
    private String videoURL;
    @Expose
    private String thumbnailURL;

    public Steps(String  id, String shortDescription, String description,
                 String videoURL, String thumbnailURL) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    public String getStepsId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return videoURL;
    }

    public String getThumbnail() {
        return thumbnailURL;
    }
}
