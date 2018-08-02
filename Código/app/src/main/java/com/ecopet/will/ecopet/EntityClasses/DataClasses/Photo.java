package com.ecopet.will.ecopet.EntityClasses.DataClasses;

import java.util.HashMap;

/**
 * Created by willrcneto on 16/03/18.
 */

public class Photo {

    private String uid_photo, uid_user, description, image_url, tag, name_user;
    private int reports;
    private HashMap<String,Object> likes = new HashMap<>();

    public Photo() {
    }

    public String getName_user() {
        return name_user;
    }

    public int getReports() {
        return reports;
    }

    public void setReports(int reports) {
        this.reports = reports;
    }

    public void setLikes(HashMap<String, Object> likes) {
        this.likes = likes;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getUid_photo() {
        return uid_photo;
    }

    public void setUid_photo(String uid_photo) {
        this.uid_photo = uid_photo;
    }

    public String getUid_user() {
        return uid_user;
    }

    public void setUid_user(String uid_user) {
        this.uid_user = uid_user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public HashMap<String, Object> getLikes() {
        return likes;
    }
}
