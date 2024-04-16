package com.example.lensleap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lensleap.R;
import com.example.lensleap.datamodel.MyPostModel;

import java.util.ArrayList;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.ViewHolder> {
    private Context context;
    private ArrayList<MyPostModel> myPostModels;

    public MyPostAdapter(Context context, ArrayList<MyPostModel> myPostModels) {
        this.context = context;
        this.myPostModels = myPostModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_post_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyPostModel myPostModel = myPostModels.get(position);
        String imageUrl = myPostModel.getImageUrl();

        // Load image into ImageView using Glide
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.img_placeholder) // Placeholder image while loading
                .error(R.drawable.rounded_button_background) // Error image if loading fails
                .into(holder.post_image);
    }

    @Override
    public int getItemCount() {
        return myPostModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView post_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post_image = itemView.findViewById(R.id.post_image);
        }
    }
}
