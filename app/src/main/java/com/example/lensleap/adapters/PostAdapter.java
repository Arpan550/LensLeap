package com.example.lensleap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lensleap.R;
import com.example.lensleap.datamodel.PostModel;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    Context context;
    ArrayList<PostModel> postModels;

    public PostAdapter(Context context, ArrayList<PostModel> postModels) {
        this.context = context;
        this.postModels = postModels;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.post_layout, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        PostModel postModel = postModels.get(position);
        String postImageURL = postModel.getPost_img();
        String profileImageURL = postModel.getProfile_img();
        String username = postModel.getUsername();
        String caption = postModel.getCaption();

        // Load post image into ImageView using Glide
        Glide.with(context)
                .load(postImageURL)
                .placeholder(R.drawable.img_placeholder) // Placeholder image while loading
                .error(R.drawable.rounded_button_background) // Error image if loading fails
                .into(holder.post_image);

        // Load profile image into ImageView using Glide
        Glide.with(context)
                .load(profileImageURL)
                .placeholder(R.drawable.img_placeholder) // Placeholder image while loading
                .error(R.drawable.rounded_button_background) // Error image if loading fails
                .into(holder.profile_image);


    }

    @Override
    public int getItemCount() {
        return postModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView username, caption;
        ImageView profile_image, post_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.post_username);
            profile_image = itemView.findViewById(R.id.profile_image);
            post_image = itemView.findViewById(R.id.post_image);
            caption = itemView.findViewById(R.id.post_caption);
        }
    }
}
