package com.example.lensleap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lensleap.R;
import com.example.lensleap.datamodel.StoryModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ADD_STORY = 0;
    private static final int VIEW_TYPE_USER_STORY = 1;
    private Context context;
    private ArrayList<StoryModel> storyModels;

    public StoryAdapter(Context context, ArrayList<StoryModel> storyModels) {
        this.context = context;
        this.storyModels = storyModels;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ADD_STORY) {
            View view = LayoutInflater.from(context).inflate(R.layout.add_story_layout, parent, false);
            return new AddStoryViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.story_layout, parent, false);
            return new StoryViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_USER_STORY) {
            StoryViewHolder storyViewHolder = (StoryViewHolder) holder;
            storyViewHolder.username.setText(storyModels.get(position - 1).getUsername());
            storyViewHolder.circleImageView.setImageResource(storyModels.get(position - 1).getImage());
        }
    }

    @Override
    public int getItemCount() {
        return storyModels.size() + 1; // Add 1 for the "Add Story" option
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_ADD_STORY : VIEW_TYPE_USER_STORY;
    }

    // ViewHolder for the "Add Story" option
    private class AddStoryViewHolder extends RecyclerView.ViewHolder {
        AddStoryViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views or handle click events if needed
        }
    }

    // ViewHolder for user stories
    private class StoryViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView username;

        StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.username);
        }
    }
}
