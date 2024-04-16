// ReelsAdapter.java
package com.example.lensleap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lensleap.R;
import com.example.lensleap.datamodel.ReelsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReelsAdapter extends RecyclerView.Adapter<ReelsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ReelsModel> reels;

    public ReelsAdapter(Context context, ArrayList<ReelsModel> reels) {
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
        holder.videoView.setVideoPath(reelsModel.getReels_post());
        holder.videoView.start();

        // Load profile image using Picasso
        Picasso.get().load(reelsModel.getProfile_img()).into(holder.profileImageView);
    }

    @Override
    public int getItemCount() {
        return reels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;
        TextView usernameTextView, captionTextView;
        CircleImageView profileImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            captionTextView = itemView.findViewById(R.id.captionTextView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
        }
    }
}
