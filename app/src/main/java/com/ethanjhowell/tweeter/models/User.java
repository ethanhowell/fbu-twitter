package com.ethanjhowell.tweeter.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private String name;
    private String screenName;
    private String profileImageUrl;

    public User(JSONObject json) throws JSONException {
        name = json.getString("name");
        screenName = json.getString("screen_name");
        profileImageUrl = json.getString("profile_image_url_https");
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
