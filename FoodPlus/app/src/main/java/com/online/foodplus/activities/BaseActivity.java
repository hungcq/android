package com.online.foodplus.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.online.foodplus.Constants;
import com.online.foodplus.R;
import com.online.foodplus.utils.Tool;
import com.online.foodplus.widgets.ToastCustom;

/**
 * Created by thanhthang on 06/12/2016.
 */

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected ImageButton bHomeFooter, bPinFooter, bCommentFooter, bAlertFooter, bUserFooter;
    protected NavigationView navigationView;
    protected DrawerLayout drawerLayout;
    ImageView imgNavAvatar;
    TextView tvNavUsername;
    protected Snackbar snackbar;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    /**
     * COMMON FUNCTION
     */

    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void updateNavHeader() {
        final boolean uid = Tool.isLogin(getApplicationContext());

        if (tvNavUsername != null) {
            //System.out.println("-----------------------updateNavHeader: OK");
            String username = Tool.getUsername(getApplicationContext());
            if (!username.equals(""))
                tvNavUsername.setText("ID: " + username);
            tvNavUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!uid)
                        startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 900);
                    else
                        startActivityForResult(new Intent(getApplicationContext(), UserActivity.class), 901);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            });
        }


        if (imgNavAvatar != null) {
            imgNavAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!uid)
                        startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 900);
                    else
                        startActivityForResult(new Intent(getApplicationContext(), UserActivity.class), 901);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            });
            String avatar = Tool.getAvatar(getApplicationContext());
            if (!avatar.equals("")) {
                Glide.with(getApplicationContext()).load(avatar)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgNavAvatar);
                //                Picasso.with(getApplicationContext())
                //                        .load(avatar)
                //                        .resize(200, 200)
                //                        .centerCrop()
                //                        .into(imgNavAvatar);
            } else
                imgNavAvatar.setImageResource(R.drawable.ic_user_round);

        }
    }

    public void initFooter(final Activity activity) {
        bHomeFooter = (ImageButton) findViewById(R.id.bHomeFooter);
        bAlertFooter = (ImageButton) findViewById(R.id.bAlertFooter);
        bCommentFooter = (ImageButton) findViewById(R.id.bCommentFooter);
        bUserFooter = (ImageButton) findViewById(R.id.bUserFooter);
        bPinFooter = (ImageButton) findViewById(R.id.bPinFooter);
        bHomeFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                if (activity instanceof MainActivity) {
                //                    //Nothing to do
                //                } else {
                //                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                //                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                //                }
            }
        });
        bUserFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        bPinFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Tool.getUserId(getApplicationContext()).equals(""))
                    startActivity(new Intent(getApplicationContext(), PinActivity.class));
                else
                    startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 900);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        bAlertFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EventActivity.class).putExtra("title", "Email"));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        bCommentFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SurveyActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    public void initToolbarSimple(String title) {
        initToolbarSimple(title, true);
    }

    public void initToolbarSimple(String title, boolean menuVisibility) {
        ImageView imgToolbar = (ImageView) findViewById(R.id.imgToolbarBack);
        ImageView imgMenu = (ImageView) findViewById(R.id.imgToolbarMenu);
        imgToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //NavUtils.navigateUpFromSameTask(BaseActivity.this);
                onBackPressed();
            }
        });
        if (menuVisibility)
            imgMenu.setVisibility(View.VISIBLE);
        else {
            imgMenu.setVisibility(View.GONE);
        }
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout != null) drawerLayout.openDrawer(GravityCompat.END);
            }
        });
        TextView tvToolbarTitle = (TextView) findViewById(R.id.tvToolbarTitle);
        if (title != null)
            tvToolbarTitle.setText(title);
    }

    protected void setUpDrawerLayout(int checkedItem) {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (checkedItem != 0) {
            navigationView.setCheckedItem(checkedItem);
        }
        View headerView = navigationView.getHeaderView(0);
        imgNavAvatar = (ImageView) headerView.findViewById(R.id.imgNavAvatar);
        tvNavUsername = (TextView) headerView.findViewById(R.id.tvNavUsername);


        //User info in nav header
        updateNavHeader();

    }

    public void showDrawable() {
        if (drawerLayout != null) drawerLayout.openDrawer(GravityCompat.END);
    }

    //SNACKBAR CHECK NETWORK CONNECTED
    protected void initNetworkConnectedCheck(View view) {
        if (view != null && view instanceof CoordinatorLayout) {
            snackbar = Snackbar
                    .make(view, getResources().getString(R.string.not_connected_internet), Snackbar.LENGTH_LONG)

                    .setDuration(15000).setAction("Cài đặt", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), Constants.STATUS_NETWORK_CONNECT);
                            snackbar.dismiss();
                        }
                    });
            // Changing message text color
            snackbar.setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.Yellow));

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

            //set background color
            sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.Red));
        }

    }

    protected void showNetworkStatusNotification(boolean isConnected) {
        if (snackbar != null) {
            if (!isConnected)
                snackbar.show();
            else {
                if (snackbar.isShown())
                    snackbar.dismiss();
            }
        } else {
            if (!isConnected)
                ToastCustom.show(getApplicationContext(), getResources().getString(R.string.not_connected_internet), ToastCustom.LENGTH_LONG, ToastCustom.ERROR);
        }
    }

    /**
     * OVERRIDE FUNCTION
     */
    @Override
    public boolean onKeyDown(int keycode, KeyEvent e) {
        switch (keycode) {
            case KeyEvent.KEYCODE_MENU:
                if (drawerLayout != null) drawerLayout.openDrawer(GravityCompat.END);
                return true;
        }

        return super.onKeyDown(keycode, e);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        Bundle bundle;
        switch (item.getItemId()) {
            case R.id.nav_home_item:
                navigateUp();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.nav_event_item:
                startActivity(new Intent(this, EventActivity.class).putExtra("title", "Bài ghim")
                        .putExtra("active", "Sự kiện  - Thông báo"));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.nav_food_item:
                intent = new Intent(this, CategoryActivity.class);
                bundle = new Bundle();
                bundle.putString("title", getResources().getString(R.string.thuc_pham_extended));
                bundle.putString("active", "thucpham");
                bundle.putString("t", String.valueOf(1));
                bundle.putString("cid", String.valueOf(43));
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.nav_recipe_item:
                intent = new Intent(this, CategoryActivity.class);
                bundle = new Bundle();
                bundle.putString("title", getResources().getString(R.string.nau_an_extended));
                bundle.putString("active", "congthuc");
                bundle.putString("t", String.valueOf(3));
                bundle.putString("cid", String.valueOf(42));
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.nav_knife_item:
                intent = new Intent(this, CategoryActivity.class);
                bundle = new Bundle();
                bundle.putString("title", getResources().getString(R.string.mon_an));
                bundle.putString("active", "monan");
                bundle.putString("t", String.valueOf(2));
                bundle.putString("cid", String.valueOf(21));
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.nav_coffee_item:
                intent = new Intent(this, CategoryActivity.class);
                bundle = new Bundle();
                bundle.putString("title", getResources().getString(R.string.do_uong));
                bundle.putString("active", "douong");
                bundle.putString("t", String.valueOf(4));
                bundle.putString("cid", String.valueOf(41));
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.nav_spa_item:
                intent = new Intent(this, CategoryActivity.class);
                bundle = new Bundle();
                bundle.putString("title", getResources().getString(R.string.khoe_dep_extended));
                bundle.putString("active", "khoedep");
                bundle.putString("t", String.valueOf(5));
                bundle.putString("cid", String.valueOf(44));
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.nav_facebook_fanpage:
                try {
                    Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                    String facebookUrl = Tool.getFacebookPageURL(this);
                    facebookIntent.setData(Uri.parse(facebookUrl));
                    startActivity(facebookIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.nav_alarm_item:
                startActivity(new Intent(this, EventActivity.class).putExtra("title", "Bài ghim")
                        .putExtra("active", "thongbao"));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.nav_user_item:
                startActivity(new Intent(this, UserActivity.class).putExtra("title", "Tài khoản")
                        .putExtra("active", "taikhoan"));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.nav_pin_item:
                if (!Tool.getUserId(getApplicationContext()).equals(""))
                    startActivity(new Intent(getApplicationContext(), PinActivity.class));
                else
                    startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 900);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }
        if (drawerLayout != null) drawerLayout.closeDrawer(GravityCompat.END);
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //        System.out.println("--------------requestCode: " + requestCode + "----------resultCode: " + resultCode);
        switch (requestCode) {
            case 900:   //Login Success
                if (resultCode == Activity.RESULT_OK) {
                    updateNavHeader();
                }
                break;
            case 901:   //Logout Success
                if (resultCode == Activity.RESULT_OK) {
                    tvNavUsername.setText(getResources().getString(R.string.login));
                    imgNavAvatar.setImageResource(R.drawable.ic_user_round);
                }
                break;
        }
    }

    protected void navigateUp() {
        Intent intent = NavUtils.getParentActivityIntent(this);
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            NavUtils.navigateUpTo(this, intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateNavHeader();
        showNetworkStatusNotification(Tool.isNetworkConnected(getApplicationContext()));
    }
}
