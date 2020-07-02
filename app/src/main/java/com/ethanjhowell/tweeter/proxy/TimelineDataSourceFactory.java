package com.ethanjhowell.tweeter.proxy;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;
import androidx.paging.ItemKeyedDataSource;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.ethanjhowell.tweeter.models.Tweet;

import org.json.JSONException;

import okhttp3.Headers;

public class TimelineDataSourceFactory extends DataSource.Factory<Long, Tweet> {
    private static final String TAG = TimelineDataSourceFactory.class.getCanonicalName();
    private TwitterClient mClient;

    public TimelineDataSourceFactory(TwitterClient mClient) {
        this.mClient = mClient;
    }

    @NonNull
    @Override
    public DataSource<Long, Tweet> create() {
        return new TimelineDataSource();
    }

    private class TimelineDataSource extends ItemKeyedDataSource<Long, Tweet> {
        private JsonHttpResponseHandler createHandler(LoadCallback<Tweet> callback) {
            return new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.i(TAG, "onSuccess: " + json.toString());
                    try {
                        callback.onResult(Tweet.fromJsonArray(json.jsonArray));
                    } catch (JSONException e) {
                        Log.e(TAG, "onFailure: json error", e);
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.e(TAG, "onFailure: unable to download timeline - " + response, throwable);
                }
            };
        }

        @Override
        public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Tweet> callback) {
            mClient.getHomeTimeline(null, createHandler(callback));
        }

        @Override
        public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Tweet> callback) {
        }

        @Override
        public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Tweet> callback) {
            mClient.getHomeTimeline(params.key - 1, createHandler(callback));
        }

        @NonNull
        @Override
        public Long getKey(@NonNull Tweet item) {
            return item.getId();
        }
    }
}
