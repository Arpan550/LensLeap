package com.example.lensleap.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.SimpleExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2; // Import ViewPager2 here

import com.example.lensleap.R;
import com.example.lensleap.datamodel.ReelsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

@UnstableApi
public class ReelsAdapter extends RecyclerView.Adapter<ReelsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ReelsModel> reels;
    private ViewPager2 viewPager; // Correct type for viewPager
    private SimpleExoPlayer currentExoPlayer; // To keep track of the currently playing ExoPlayer instance

    public ReelsAdapter(Context context, ArrayList<ReelsModel> reels, ViewPager2 viewPager) {
        this.context = context;
        this.reels = reels;
        this.viewPager = viewPager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reels, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReelsModel reelsModel = reels.get(position);

        // Bind data to UI elements
        holder.usernameTextView.setText(reelsModel.getUsername());
        holder.captionTextView.setText(reelsModel.getCaption());
        Picasso.get().load(reelsModel.getProfile_img()).into(holder.profileImageView);

        // Initialize a new SimpleExoPlayer instance for this ViewHolder
        SimpleExoPlayer exoPlayer = new SimpleExoPlayer.Builder(context).build();
        holder.playerView.setPlayer(exoPlayer);

        // Prepare media for playback
        Uri videoUri = Uri.parse(reelsModel.getReels_post());
        exoPlayer.setMediaItem(MediaItem.fromUri(videoUri));
        exoPlayer.prepare();

        // Set the playWhenReady flag based on the isPaused flag
        exoPlayer.setPlayWhenReady(!reelsModel.isPaused());

        // Pause playback of the current video if it exists and is not the currently visible page
        if (currentExoPlayer != null && currentExoPlayer.isPlaying() && viewPager.getCurrentItem() != position) {
            currentExoPlayer.pause();
        }

        // Set the currentExoPlayer to the newly initialized exoPlayer
        currentExoPlayer = exoPlayer;
    }


    @Override
    public int getItemCount() {
        return reels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PlayerView playerView;
        CircleImageView profileImageView;
        TextView usernameTextView, captionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.reelsvideoView);
            profileImageView = itemView.findViewById(R.id.reelsprofileImageView);
            usernameTextView = itemView.findViewById(R.id.reelsusernameTextView);
            captionTextView = itemView.findViewById(R.id.reelscaptionTextView);
        }
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        PlayerView playerView = holder.playerView;
        if (playerView != null) {
            SimpleExoPlayer exoPlayer = (SimpleExoPlayer) playerView.getPlayer();
            if (exoPlayer != null) {
                exoPlayer.stop();
                exoPlayer.release();
            }
        }
    }
}
