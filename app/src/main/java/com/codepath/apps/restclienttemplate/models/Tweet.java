package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tweet {
    private String text;
    private String createdAt;
    private User user;

    public Tweet(JSONObject json) throws JSONException {
        text = json.getString("text");
        createdAt = json.getString("created_at");
        user = new User(json.getJSONObject("user"));
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(new Tweet(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

    public String getText() {
        return text;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }
}
