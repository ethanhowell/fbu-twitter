package com.ethanjhowell.tweeter.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ethanjhowell.tweeter.R;
import com.ethanjhowell.tweeter.databinding.ActivityComposeBinding;

public class ComposeActivity extends AppCompatActivity {
    private static final String TAG = ComposeActivity.class.getCanonicalName();
    private ActivityComposeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComposeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}