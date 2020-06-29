package com.codepath.apps.restclienttemplate.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.apps.restclienttemplate.adapters.TweetAdapter;
import com.codepath.apps.restclienttemplate.databinding.ActivityTimelineBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.proxy.TwitterApplication;
import com.codepath.apps.restclienttemplate.proxy.TwitterClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {
    private static final String TAG = TimelineActivity.class.getCanonicalName();
    private ActivityTimelineBinding binding;
    private TwitterClient client;
    private List<Tweet> tweets;
    private TweetAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimelineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        client = TwitterApplication.getRestClient(this);

        RecyclerView rvTweets = binding.rvTweets;
        tweets = new ArrayList<>();
        adapter = new TweetAdapter(this, tweets);
        rvTweets.setAdapter(adapter);
        rvTweets.setLayoutManager(new LinearLayoutManager(this));

        populateTimeline();
    }

    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess: " + json.toString());
                try {
                    tweets.addAll(Tweet.fromJsonArray(json.jsonArray));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(TAG, "onFailure: json error", e);
                    Toast.makeText(TimelineActivity.this, "Sorry, there was a problem.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure: unable to download timeline", throwable);
                Toast.makeText(TimelineActivity.this, "Sorry, there was a problem.", Toast.LENGTH_LONG).show();
            }
        });
    }
}