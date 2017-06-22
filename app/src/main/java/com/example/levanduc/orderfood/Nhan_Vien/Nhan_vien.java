package com.example.levanduc.orderfood.Nhan_Vien;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.levanduc.orderfood.R;

public class Nhan_vien extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private NhanvienAdapter mNhanvienAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhan_vien);
        android.support.v7.app.ActionBar AB=getSupportActionBar();
        AB.hide();
        Log.d(TAG, "onCreate: Starting.");

        mNhanvienAdapter = new NhanvienAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        NhanvienAdapter adapter = new NhanvienAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment(), "Món Ăn Đã Chế Biến Xong");
        adapter.addFragment(new Tab2Fragment(), "Khách Hàng Yêu Cầu Thanh Toán");
        viewPager.setAdapter(adapter);
    }

}
