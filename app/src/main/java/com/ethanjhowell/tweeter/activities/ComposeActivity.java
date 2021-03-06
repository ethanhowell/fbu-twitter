package com.ethanjhowell.tweeter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.ethanjhowell.tweeter.databinding.ActivityComposeBinding;
import com.ethanjhowell.tweeter.databinding.ToolbarBinding;
import com.ethanjhowell.tweeter.models.Tweet;
import com.ethanjhowell.tweeter.proxy.TwitterApplication;
import com.ethanjhowell.tweeter.proxy.TwitterClient;

import org.json.JSONException;
import org.parceler.Parcels;

import java.util.Objects;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {
    private static final String TAG = ComposeActivity.class.getCanonicalName();
    private ActivityComposeBinding binding;
    private static final int MAX_TWEET_LENGTH = 280;
    private EditText etTweetBody;
    private TextView tvCharLeft;
    private Button bTweet;
    private TwitterClient client;

    private void updateCharLeft() {
        tvCharLeft.setText(Integer.toString(MAX_TWEET_LENGTH - etTweetBody.length()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComposeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        client = TwitterApplication.getRestClient(this);


        ToolbarBinding toolbarBinding = binding.include;
        toolbarBinding.toolbar.setTitle("");
        setSupportActionBar(toolbarBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        etTweetBody = binding.etTweetBody;
        tvCharLeft = binding.tvCharLeft;
        bTweet = binding.bTweet;
        updateCharLeft();


        bTweet.setOnClickListener(v -> {
            Log.d(TAG, "onCreate: preparing to tweet: " + etTweetBody.getText());
            client.publishTweet(etTweetBody.getText().toString(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.i(TAG, "onSuccess: " + json.jsonObject);
                    try {
                        Tweet tweet = new Tweet(json.jsonObject);
                        Intent intent = new Intent();
                        intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                        setResult(RESULT_OK, intent);
                    } catch (JSONException e) {
                        Log.e(TAG, "onSuccess: failure deserializing tweet", e);
                        setResult(RESULT_CANCELED);
                    }
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Toast.makeText(ComposeActivity.this, "Sorry, there was a problem. Try again.", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "onFailure: unable to publish tweet - " + response, throwable);
                }
            });
        });

        // show keyboard and focus the edittext, set max length of input
        etTweetBody.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_TWEET_LENGTH)});
        etTweetBody.requestFocus();
        etTweetBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateCharLeft();
                bTweet.setEnabled(editable.length() > 0);
            }
        });
    }
}