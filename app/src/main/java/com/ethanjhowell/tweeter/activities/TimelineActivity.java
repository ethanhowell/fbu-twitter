package com.ethanjhowell.tweeter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimelineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.timeline_title);
        setSupportActionBar(toolbar);

        client = TwitterApplication.getRestClient(this);

        RecyclerView rvTweets = binding.rvTweets;
        adapter = new TweetAdapter(this);
        populateTimeline();
        rvTweets.setAdapter(adapter);
        rvTweets.setLayoutManager(new LinearLayoutManager(this));

        swipeContainer = binding.swipeContainer;
        swipeContainer.setOnRefreshListener(() -> {
            Log.d(TAG, "swiping");
            populateTimeline();
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.composeTweet) {
            Log.i(TAG, "onOptionsItemSelected: composing new tweet");
            startActivity(new Intent(this, ComposeActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateTimeline() {
        Log.d(TAG, "populateTimeline: fetching timeline");
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess: " + json.toString());
                try {
                    adapter.setTweets(Tweet.fromJsonArray(json.jsonArray));
                } catch (JSONException e) {
                    Log.e(TAG, "onFailure: json error", e);
                    Toast.makeText(TimelineActivity.this, "Sorry, there was a problem.", Toast.LENGTH_LONG).show();
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure: unable to download timeline - " + response, throwable);
                Toast.makeText(TimelineActivity.this, "Sorry, there was a problem.", Toast.LENGTH_LONG).show();
                swipeContainer.setRefreshing(false);
            }
        });
    }
}