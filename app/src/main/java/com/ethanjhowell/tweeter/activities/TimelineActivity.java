package com.ethanjhowell.tweeter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ethanjhowell.tweeter.R;
import com.ethanjhowell.tweeter.adapters.TweetAdapter;
import com.ethanjhowell.tweeter.databinding.ActivityTimelineBinding;
import com.ethanjhowell.tweeter.models.Tweet;
import com.ethanjhowell.tweeter.proxy.TimelineDataSourceFactory;
import com.ethanjhowell.tweeter.proxy.TwitterApplication;
import com.ethanjhowell.tweeter.proxy.TwitterClient;

import org.parceler.Parcels;

public class TimelineActivity extends AppCompatActivity {
    private static final String TAG = TimelineActivity.class.getCanonicalName();
    private ActivityTimelineBinding binding;
    private TwitterClient client;
    private TweetAdapter adapter;
    private SwipeRefreshLayout swipeContainer;

    private static final short COMPOSE_REQUEST_CODE = 1;

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
            startActivityForResult(new Intent(this, ComposeActivity.class), COMPOSE_REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, String.format("onActivityResult: requestCode=%d, resultCode=%d", requestCode, resultCode));
        if (requestCode == COMPOSE_REQUEST_CODE && resultCode == RESULT_OK) {
            assert data != null;
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra(Tweet.class.getSimpleName()));
            Log.i(TAG, "onActivityResult: returning " + tweet.getText());
            adapter.prepend(tweet);
            binding.rvTweets.smoothScrollToPosition(0);
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    private void populateTimeline() {
        PagedList.Config build = new PagedList.Config.Builder().setPageSize(200).build();
        TimelineDataSourceFactory factory = new TimelineDataSourceFactory(client);
        LiveData<PagedList<Tweet>> tweets = new LivePagedListBuilder<Long, Tweet>(factory, build).build();

        tweets.observe(this, list -> {
            adapter.submitList(list);
        });
    }
}