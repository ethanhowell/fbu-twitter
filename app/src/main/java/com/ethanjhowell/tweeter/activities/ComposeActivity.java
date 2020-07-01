package com.ethanjhowell.tweeter.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ethanjhowell.tweeter.databinding.ActivityComposeBinding;
import com.ethanjhowell.tweeter.databinding.ToolbarBinding;

import java.util.Objects;

public class ComposeActivity extends AppCompatActivity {
    private static final String TAG = ComposeActivity.class.getCanonicalName();
    private ActivityComposeBinding binding;
    private static final int MAX_TWEET_LENGTH = 280;
    private EditText etTweetBody;
    private TextView tvCharLeft;
    private Button bTweet;

    private void updateCharLeft() {
        tvCharLeft.setText(Integer.toString(MAX_TWEET_LENGTH - etTweetBody.length()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComposeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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