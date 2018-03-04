package com.online.foodplus.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.online.foodplus.R;
import com.online.foodplus.adapters.ActivityUserViewPagerAdapter;
import com.online.foodplus.utils.Tool;

import static com.online.foodplus.utils.ImageStorage.getImageStorage;


/**
 * Created by thanhthang on 25/11/2016.
 */

public class UserActivity extends BaseActivity implements View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private SharedPreferences pref;
    private TextView tvUsername;
    private TextView tvLogOut;
    private CircularImageView imgAvatar;
    private ImageView backImage;
    private ImageView menuImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        init();
        addListener();
        setUpDrawerLayout(R.id.nav_user_item);
        detaultValue();
    }

    private void init() {
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvLogOut = (TextView) findViewById(R.id.tvLogout);
        imgAvatar = (CircularImageView) findViewById(R.id.img_avatar);

        backImage = (ImageView) findViewById(R.id.img_back);
        menuImage = (ImageView) findViewById(R.id.img_menu);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        setUpViewPagerAndTabLayout();
    }

    private void addListener() {
        tvLogOut.setOnClickListener(this);
        backImage.setOnClickListener(this);
        menuImage.setOnClickListener(this);
    }

    private void setUpViewPagerAndTabLayout() {
        ActivityUserViewPagerAdapter adapter = new ActivityUserViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.thong_tin_tai_khoan));
        //        tabLayout.addTab(tabLayout.newTab().setText(R.string.cua_toi));
        //        tabLayout.addTab(tabLayout.newTab().setText(R.string.luot_ghim));
        //        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
        //            @Override
        //            public void onTabSelected(TabLayout.Tab tab) {
        //                viewPager.setCurrentItem(tab.getPosition());
        //            }
        //
        //            @Override
        //            public void onTabUnselected(TabLayout.Tab tab) {
        //
        //            }
        //
        //            @Override
        //            public void onTabReselected(TabLayout.Tab tab) {
        //
        //            }
        //        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void detaultValue() {
        pref = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        String username = pref.getString("username", null);
        String email = pref.getString("email", null);

        //Chuyển màn hình Login nếu user chưa đăng nhập
        if (Tool.getUserId(this).equals("")) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        tvUsername.setText("ID: " + username);
        Bitmap avatar = getImageStorage(email);
        if (avatar != null)
            imgAvatar.setImageBitmap(avatar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                navigateUp();
                break;
            case R.id.img_menu:
                drawerLayout.openDrawer(GravityCompat.END);
                break;
            case R.id.tvLogout:
                pref.edit().putString("username", "").commit();
                pref.edit().putString("email", "").commit();
                pref.edit().putString("gender", "").apply();
                pref.edit().putString("user_id", "").commit();
                pref.edit().putString("login_type", "").apply();
                pref.edit().putString("token", "").apply();
                pref.edit().putString("name", "").apply();
                pref.edit().putString("phone", "").apply();
                pref.edit().putString("avatar", "").apply();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);//901 code
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        navigateUp();
    }
}
