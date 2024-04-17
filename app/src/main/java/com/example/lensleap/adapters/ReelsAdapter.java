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

import com.example.lensleap.R;
import com.example.lensleap.datamodel.ReelsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

@UnstableApi public class ReelsAdapter extends RecyclerView.Adapter<ReelsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ReelsModel> reels;
    private SimpleExoPlayer exoPlayer;

    public ReelsAdapter(Context context, ArrayList<ReelsModel> reels, SimpleExoPlayer exoPlayer) {
        this.context = context;
        this.reels = reels;
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

        // Load profile image using Picasso
        Picasso.get().load(reelsModel.getProfile_img()).into(holder.profileImageView);

        // Initialize ExoPlayer
        if (exoPlayer == null) {
            exoPlayer = new SimpleExoPlayer.Builder(context).build();
            holder.playerView.setPlayer(exoPlayer);
        }

        // Prepare media for playback
        Uri videoUri = Uri.parse(reelsModel.getReels_post());
        exoPlayer.setMediaItem(MediaItem.fromUri(videoUri));
        exoPlayer.prepare();
        exoPlayer.setPlayWhenReady(true); // Auto play video
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
            playerView = itemView.findViewById(R.id.videoView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            captionTextView = itemView.findViewById(R.id.captionTextView);
        }
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }
}
