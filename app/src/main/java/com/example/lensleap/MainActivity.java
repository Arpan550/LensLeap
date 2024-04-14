package com.example.lensleap;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.lensleap.databinding.ActivityMainBinding;
import com.example.lensleap.ui.AddFragment;
import com.example.lensleap.ui.HomeFragment;
import com.example.lensleap.ui.ProfileUserFragment;
import com.example.lensleap.ui.ReelsFragment;
import com.example.lensleap.ui.SearchFragment;
import com.example.lensleap.ui.StartFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        addFragment(new StartFragment(), 0);


        //locate into different fragment using bottom navigation menu item
        mainBinding.bottomNavigationViewBNV.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.home) {
                    addFragment(new StartFragment(), 1);
                    return true;
                } else if (menuItem.getItemId() == R.id.search) {
                    addFragment(new SearchFragment(), 1);
                    return true;
                } else if (menuItem.getItemId() == R.id.add_more) {
                    // Show AddFragment as a bottom sheet
                    AddFragment addFragment = new AddFragment();
                    addFragment.show(getSupportFragmentManager(), "AddFragment");
                    return true;
                } else if (menuItem.getItemId() == R.id.reels) {
                    addFragment(new ReelsFragment(), 1);
                    return true;
                } else if (menuItem.getItemId() == R.id.profile_user) {
                    addFragment(new ProfileUserFragment(), 1);
                    return true;
                }
                return false;
            }
        });


    }

    private void addFragment(Fragment fragment, int flag) {
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        if (flag == 0) {
            ft.add(mainBinding.displayFL.getId(), fragment);
        } else if (flag==1) {
            ft.replace(mainBinding.displayFL.getId(),fragment);
        }
        ft.commit();
    }
}