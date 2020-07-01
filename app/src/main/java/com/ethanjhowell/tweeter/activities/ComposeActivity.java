package com.ethanjhowell.tweeter.activities;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.ethanjhowell.tweeter.databinding.ActivityComposeBinding;
import com.ethanjhowell.tweeter.databinding.ToolbarBinding;

import java.util.Objects;

public class ComposeActivity extends AppCompatActivity {
    private static final String TAG = ComposeActivity.class.getCanonicalName();
    private ActivityComposeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComposeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ToolbarBinding toolbarBinding = binding.include;
        toolbarBinding.toolbar.setTitle("");
        setSupportActionBar(toolbarBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        EditText etTweetBody = binding.etTweetBody;
        // show keyboard and focus the edittext
        etTweetBody.requestFocus();
    }
}