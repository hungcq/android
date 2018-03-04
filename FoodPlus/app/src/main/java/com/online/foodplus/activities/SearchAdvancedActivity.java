package com.online.foodplus.activities;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.online.foodplus.R;
import com.online.foodplus.adapters.ActivitySearchViewPagerAdapter;
import com.online.foodplus.adapters.SearchAutoCompleteAdapter;
import com.online.foodplus.dialogs.SelectCityDialog;
import com.online.foodplus.fragments.NearbyFragment;
import com.online.foodplus.interfaces.BaseListener;
import com.online.foodplus.interfaces.FilterListener;
import com.online.foodplus.models.Base;
import com.online.foodplus.widgets.DelayAutoCompleteTextView;

public class SearchAdvancedActivity extends BaseActivity implements View.OnClickListener, FilterListener, BaseListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private RelativeLayout toolbarLayout;

    ////////////////////////////////////////////////
    private ImageView imgFilter;
    private String city_id;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_advanced);
        init();
        initData();
        initViewPagerAndTabLayout();
        setSupportActionBar(toolbar);
        initToolbar();
        listener();
    }

    private void init() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarLayout = (RelativeLayout) findViewById(R.id.layout_toolbar);
    }

    private void initData() {
        pref = getSharedPreferences("data", Context.MODE_PRIVATE);
        city_id = pref.getString("city_id", "");

    }

    private void initToolbar() {
        final DelayAutoCompleteTextView autoFind = (DelayAutoCompleteTextView) findViewById(R.id.autoFind);
        autoFind.setDropDownWidth(getResources().getDisplayMetrics().widthPixels);                  //Cài đặt chiều rộng tối đa của Danh sách gợi ý (bằng với chiều rộng thiết bị)
        autoFind.setDropDownHeight(getResources().getDisplayMetrics().heightPixels / 2);            //Cài đặt chiều cao tối đa của Danh sách gợi ý (50% full height)
        autoFind.setAdapter(new SearchAutoCompleteAdapter(this));
        autoFind.setThreshold(2);                                                                   //Nhập tối thiểu 2 ký tự thì bắt đầu tìm kiếm
        autoFind.setLoadingIndicator(
                (android.widget.ProgressBar) findViewById(R.id.progressBar));                       //Loading Icon
        autoFind.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Base base = (Base) adapterView.getItemAtPosition(position);
                Intent intent = new Intent(SearchAdvancedActivity.this, DetailActivity.class);
                intent.putExtra("id", base.getId());
                intent.putExtra("t", base.getTid());
                intent.putExtra("cid", base.getCid());
                autoFind.setText("");
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        imgFilter = (ImageView) findViewById(R.id.imgFilter);
        setImageConfigVisibility();
    }

    private void setImageConfigVisibility() {
        imgFilter.setVisibility(View.VISIBLE);
    }

    private void listener() {
        imgFilter.setOnClickListener(this);
    }

    private void initViewPagerAndTabLayout() {
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new ActivitySearchViewPagerAdapter(getSupportFragmentManager()));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.gan_toi));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.moi_nhat));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.pho_bien));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                setImageConfigVisibility();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (((FragmentStatePagerAdapter) viewPager.getAdapter()).getItem(viewPager.getCurrentItem()) instanceof NearbyFragment)
                    imgFilter.setVisibility(View.GONE);
                else
                    imgFilter.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void showToolbarAndTabLayout() {
        ObjectAnimator showToolbar = ObjectAnimator.ofFloat(toolbarLayout, View.TRANSLATION_Y, 0);
        showToolbar.start();
    }

    public void hideToolbarAndTabLayout() {
        ObjectAnimator hideToolbar = ObjectAnimator.ofFloat(toolbarLayout, View.TRANSLATION_Y, -toolbarLayout.getHeight());
        hideToolbar.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgFilter:
                city_id = pref.getString("city_id", "");
                SelectCityDialog newFragment = SelectCityDialog.newInstance(city_id);
                newFragment.show(getSupportFragmentManager(), "dialog");
                break;
            default:
                break;
        }
    }

    public RelativeLayout getToolbarLayout() {
        return toolbarLayout;
    }


    @Override
    public void onFilterItemChosen() {
        viewPager.getAdapter().notifyDataSetChanged();
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void updateResult(Base base) {

        pref.edit().putString("city_id", base.getId()).commit();
        pref.edit().putString("city_title", base.getTitle()).commit();
        city_id = base.getId();
    }
}
