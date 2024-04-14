package com.example.lensleap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lensleap.R;
import com.example.lensleap.datamodel.MyPostModel;

import java.util.ArrayList;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.ViewHolder> {
    Context context;
    ArrayList<MyPostModel> myPostModels;

    public MyPostAdapter(Context context, ArrayList<MyPostModel> myPostModels) {
        this.context = context;
        this.myPostModels = myPostModels;
    }

    @NonNull
    @Override
    public MyPostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_post_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostAdapter.ViewHolder holder, int position) {
        holder.post_image.setImageResource(myPostModels.get(position).getPostImage());
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
