package com.example.lensleap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lensleap.R;
import com.example.lensleap.datamodel.PostModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private Context context;
    private ArrayList<PostModel> postList;

    public PostAdapter(Context context, ArrayList<PostModel> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_layout, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostModel postModel = postList.get(position);

        // Bind data to UI elements
        holder.usernameTextView.setText(postModel.getUsername());
        holder.captionTextView.setText(postModel.getCaption());
        Picasso.get().load(postModel.getProfile_img()).into(holder.profileImageView);
        Picasso.get().load(postModel.getPost_img()).into(holder.postImageView);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView, postImageView;
        TextView usernameTextView, captionTextView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profile_image);
            postImageView = itemView.findViewById(R.id.post_image);
            usernameTextView = itemView.findViewById(R.id.post_username);
            captionTextView = itemView.findViewById(R.id.post_caption);
        }
    }
}
