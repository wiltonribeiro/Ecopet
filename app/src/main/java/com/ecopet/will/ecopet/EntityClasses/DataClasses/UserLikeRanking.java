package com.ecopet.will.ecopet.EntityClasses.DataClasses;

import android.support.annotation.NonNull;

/**
 * Created by willrcneto on 04/04/18.
 */

public class UserLikeRanking implements Comparable<UserLikeRanking>{
    private String userKey;
    private String userName;
    private  Integer likes;

    public UserLikeRanking() {

    }

    public UserLikeRanking(String userKey, Integer likes) {
        this.userKey = userKey;
        this.userName = "";
        this.likes = likes;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    @Override
    public int compareTo(@NonNull UserLikeRanking userLikeRanking) {
        if (this.getLikes() > userLikeRanking.getLikes()) {
            return -1;
        }
        if (this.getLikes() < userLikeRanking.getLikes()) {
            return 1;
        }
        return 0;
    }
}
