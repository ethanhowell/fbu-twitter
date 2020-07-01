package com.ethanjhowell.tweeter.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.ethanjhowell.tweeter.databinding.ItemTweetBinding;
import com.ethanjhowell.tweeter.models.Tweet;
import com.ethanjhowell.tweeter.models.User;

import java.util.ArrayList;
import java.util.List;


public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {
    private final static String TAG = TweetAdapter.class.getCanonicalName();
    private Context context;
    private List<Tweet> tweets;
    private ItemTweetBinding binding;

    public TweetAdapter(Context context) {
        this.context = context;
        this.tweets = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemTweetBinding.inflate(LayoutInflater.from(context), parent, false);
        View view = binding.getRoot();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(tweets.get(position));
    }

    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    public void prepend(Tweet tweet) {
        tweets.add(0, tweet);
        notifyItemInserted(0);
    }

    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

    public void setTweets(List<Tweet> tweets) {
        this.tweets = tweets;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProfileImage;
        private TextView tvName;
        private TextView tvTweetMeta;
        private TextView tvText;
        private ImageView ivTweetImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = binding.ivProfileImage;
            tvName = binding.tvName;
            tvTweetMeta = binding.tvTweetMeta;
            tvText = binding.tvText;
            ivTweetImage = binding.ivTweetImage;
        }

        public void bind(Tweet tweet) {
            User user = tweet.getUser();
            tvName.setText(user.getName());
            tvTweetMeta.setText(String.format(" @%s · %s", user.getScreenName(), tweet.getRelativeTimeAgo()));
            tvText.setText(tweet.getText());

            if (tweet.hasImageUrl()) {
                ivTweetImage.setVisibility(View.VISIBLE);
                Log.d(TAG, "bind: attaching image for " + tweet.getText() + " " + tweet.getImageUrl());
                Glide.with(context)
                        .load(tweet.getImageUrl())
                        .transform(new RoundedCorners(50))
                        .into(ivTweetImage);
            } else {
                ivTweetImage.setVisibility(View.GONE);
            }

            Glide.with(context)
                    .load(user.getProfileImageUrl())
                    .circleCrop()
                    .into(ivProfileImage);
        }
    }
}
