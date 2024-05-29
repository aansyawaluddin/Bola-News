package com.example.afinal;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.afinal.fragment.AkunFragment;
import com.example.afinal.fragment.BaseBallFragment;
import com.example.afinal.fragment.BasketBallFragment;
import com.example.afinal.fragment.FotballFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FotballFragment fotballFragment = new FotballFragment();
        Fragment fragment = fragmentManager.findFragmentByTag(FotballFragment.class.getSimpleName());
        if (!(fragment instanceof FotballFragment)){
            fragmentManager
                    .beginTransaction()
                    .add(R.id.frame_container, fotballFragment)
                    .commit();
        }

        BottomNavigationView bottomNav = findViewById(R.id.navmenu);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.fotball_btn) {
                selectedFragment = new FotballFragment();
            } else if (item.getItemId() == R.id.basket_btn) {
                selectedFragment = new BasketBallFragment();
            } else if (item.getItemId() == R.id.baseball_btn) {
                selectedFragment = new BaseBallFragment();
            } else if (item.getItemId() == R.id.akun_btn) {
                selectedFragment = new AkunFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_container, selectedFragment)
                        .commit();

                return true;
            } else {
                return false;
            }
        });
    }
}