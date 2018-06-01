package com.ecopet.will.ecopet.EntityClasses.DataClasses;

import java.util.HashMap;
import java.util.List;

/**
 * Created by willrcneto on 15/03/18.
 */

public class User {

    private String email, description, name, image_url, uid;
    private HashMap<String,String> reports;

    public User() {
        reports = new HashMap<>();
    }

    public HashMap<String, String> getReports() {
        return reports;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

}
