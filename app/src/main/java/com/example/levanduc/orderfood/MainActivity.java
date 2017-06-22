package com.example.levanduc.orderfood;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.levanduc.orderfood.Khach_Hang.Blank1Fragment;
import com.example.levanduc.orderfood.Khach_Hang.Blank2Fragment;
import com.example.levanduc.orderfood.Quan_Ly.Blank3Fragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.inflateMenu(R.menu.navigation);
        fragment = new Blank1Fragment();
        changeFragnment();
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.navigation_home:
                        fragment = new Blank1Fragment();
                        break;
                    case R.id.navigation_dashboard:
                        fragment = new Blank2Fragment();
                        break;
                    case R.id.navigation_notifications:
                        fragment = new Blank3Fragment();
                        break;
                }
                changeFragnment();
                return true;
            }
        });
    }
    void changeFragnment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragment).commit();
    }

}