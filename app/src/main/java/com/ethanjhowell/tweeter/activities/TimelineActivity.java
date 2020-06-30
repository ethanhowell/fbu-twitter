package com.ethanjhowell.tweeter.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.ethanjhowell.tweeter.R;
import com.ethanjhowell.tweeter.adapters.TweetAdapter;
import com.ethanjhowell.tweeter.databinding.ActivityTimelineBinding;
import com.ethanjhowell.tweeter.models.Tweet;
import com.ethanjhowell.tweeter.proxy.TwitterApplication;
import com.ethanjhowell.tweeter.proxy.TwitterClient;

import org.json.JSONException;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {
    private static final String TAG = TimelineActivity.class.getCanonicalName();
    private ActivityTimelineBinding binding;
    private TwitterClient client;
    private TweetAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimelineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setTitle(R.string.timeline_title);
        setSupportActionBar(binding.toolbar);

        client = TwitterApplication.getRestClient(this);

        RecyclerView rvTweets = binding.rvTweets;
        adapter = new TweetAdapter(this);
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
                    adapter.addAll(Tweet.fromJsonArray(json.jsonArray));
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