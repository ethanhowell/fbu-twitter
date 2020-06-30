package com.ethanjhowell.tweeter.models;

import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Tweet {
    private String text;
    private String createdAt;
    private User user;

    private static String twitterPattern = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
    private static SimpleDateFormat twitterFormat = new SimpleDateFormat(twitterPattern, Locale.US);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.US);

    public Tweet(JSONObject json) throws JSONException {
        text = json.getString("full_text");
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

    public String getRelativeTimeAgo() {
        try {
            Date created = twitterFormat.parse(getCreatedAt());
            long difference = System.currentTimeMillis() - created.getTime();
            if (difference < DateUtils.MINUTE_IN_MILLIS)
                return String.format("%ss", TimeUnit.MILLISECONDS.toSeconds(difference));
            else if (difference < DateUtils.HOUR_IN_MILLIS)
                return String.format("%sm", TimeUnit.MILLISECONDS.toMinutes(difference));
            else if (difference < DateUtils.DAY_IN_MILLIS)
                return String.format("%sh", TimeUnit.MILLISECONDS.toHours(difference));
            else
                return dateFormat.format(created);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }
}
