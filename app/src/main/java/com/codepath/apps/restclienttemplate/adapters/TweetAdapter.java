package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.databinding.ItemTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import java.util.List;


public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {
    private final static String TAG = TweetAdapter.class.getCanonicalName();
    Context context;
    List<Tweet> tweets;
    ItemTweetBinding binding;

    public TweetAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
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

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfileImage;
        TextView tvName;
        TextView tvTweetMeta;
        TextView tvText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = binding.ivProfileImage;
            tvName = binding.tvName;
            tvTweetMeta = binding.tvTweetMeta;
            tvText = binding.tvText;
        }

        public void bind(Tweet tweet) {
            User user = tweet.getUser();
            tvName.setText(user.getName());
            tvTweetMeta.setText(String.format("@%s · %s", user.getScreenName(), tweet.getRelativeTimeAgo()));
            tvText.setText(tweet.getText());

            Glide.with(context)
                    .load(user.getProfileImageUrl())
                    .circleCrop()
                    .into(ivProfileImage);
        }
    }
}
