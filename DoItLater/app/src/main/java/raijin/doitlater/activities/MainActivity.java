package raijin.doitlater.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import java.lang.reflect.Field;

import raijin.doitlater.R;
import raijin.doitlater.fragments.About;
import raijin.doitlater.fragments.AllTasksFragment;
import raijin.doitlater.fragments.CompletedFragment;
import raijin.doitlater.fragments.FragmentWithSearch;
import raijin.doitlater.fragments.ToDoFragment;
import raijin.doitlater.managers.FragmentType;
import raijin.doitlater.managers.ScreenManager;
import raijin.doitlater.utils.Utils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ScreenManager screenManager;
    private NavigationView navigationView;
    private SearchView searchView;
    private FloatingActionButton fab;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.do_it_later);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoteActivity.activityType = ActivityType.ADDNOTE;
                startActivity(new Intent(MainActivity.this, NoteActivity.class));
                overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //
//        ColorStateList myColorStateList = new ColorStateList(
//                new int[][]{
//                        new int[]{android.R.attr.state_checked},
//                        new int[]{0}
//                },
//                new int[]{
//                        Color.WHITE,
//                        Color.GRAY
//                }
//        );
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//        navigationView.setItemIconTintList(myColorStateList);
        initializeFirstView();
        overridePendingTransition(R.anim.trans_back_in, R.anim.trans_back_out);
        setNavTheme();
    }

    public void setFabColor(int color) {
        fab.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    public void setFabVisibility(int visibility) {
        fab.setVisibility(visibility);
    }

    public void setToolbarColor(int color) {
        toolbar.setBackgroundColor(color);
    }

    public void setNavTheme() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = settings.getString(MainActivity.SettingsFragment.PREF_KEY_THEME_COLOR, "");
        View headerView = navigationView.getHeaderView(0);
        if (theme.equals("theme_default")) {
            headerView.setBackgroundResource(R.drawable.side_nav_bar);
            navigationView.setItemBackgroundResource(R.drawable.nav_item_background);
        } else if (theme.equals("theme_teal")) {
            headerView.setBackgroundResource(R.drawable.side_nav_bar_teal);
            navigationView.setItemBackgroundResource(R.drawable.nav_item_background_teal);
        } else if (theme.equals("theme_purple")) {
            headerView.setBackgroundResource(R.drawable.side_nav_bar_purple);
            navigationView.setItemBackgroundResource(R.drawable.nav_item_background_purple);
        } else if (theme.equals("theme_grey")) {
            headerView.setBackgroundResource(R.drawable.side_nav_bar_grey);
            navigationView.setItemBackgroundResource(R.drawable.nav_item_background_grey);
        } else if (theme.equals("theme_orange")) {
            headerView.setBackgroundResource(R.drawable.side_nav_bar_orange);
            navigationView.setItemBackgroundResource(R.drawable.nav_item_background_orange);
        } else if (theme.equals("theme_pink")) {
            headerView.setBackgroundResource(R.drawable.side_nav_bar_pink);
            navigationView.setItemBackgroundResource(R.drawable.nav_item_background_pink);
        }
    }

    private void initializeFirstView() {
        screenManager = new ScreenManager(this.getSupportFragmentManager(), R.id.container);
        FragmentType currentFragment = ScreenManager.getCurrentFragment();
        if (currentFragment == FragmentType.TODO) {
            screenManager.openFragment(ToDoFragment.create(this.screenManager), false);
            navigationView.getMenu().getItem(0).setChecked(true);
        } else if (currentFragment == FragmentType.COMPLETED) {
            screenManager.openFragment(CompletedFragment.create(this.screenManager), false);
            navigationView.getMenu().getItem(1).setChecked(true);
        } else if (currentFragment == FragmentType.ALLTASK) {
            screenManager.openFragment(AllTasksFragment.create(this.screenManager), false);
            navigationView.getMenu().getItem(2).setChecked(true);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItemImpl menuItem = (MenuItemImpl) menu.findItem(R.id.action_search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                closeSearch();
            }
        });
        initSearchView(searchView);
        return true;
    }

    private void initSearchView(SearchView searchView) {
        final AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById(R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, R.drawable.cursor);
        } catch (Exception e) {
        }

        searchTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    MainActivity.this.doSearch(searchTextView.getText().toString());
                }
                return false;
            }
        });
//        RxSearchView
//                .queryTextChangeEvents(searchView)
//                .debounce(500, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<SearchViewQueryTextEvent>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                    }
//
//                    @Override
//                    public void onNext(SearchViewQueryTextEvent searchViewQueryTextEvent) {
//                        MainActivity.this.doSearch(searchViewQueryTextEvent.queryText().toString());
//                    }
//                });
    }

    private void closeSearch() {
        FragmentWithSearch fragmentWithSearch = (FragmentWithSearch) getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragmentWithSearch != null) {
            fragmentWithSearch.closeSearch();
        }
    }

    private void doSearch(String searchString) {
        FragmentWithSearch fragmentWithSearch = (FragmentWithSearch) getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragmentWithSearch != null) {
            fragmentWithSearch.doSearch(searchString);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.to_do_menu_item) {
            screenManager.openFragment(ToDoFragment.create(this.screenManager), false);
            fab.setVisibility(View.VISIBLE);
        } else if (id == R.id.completed_menu_item) {
            screenManager.openFragment(CompletedFragment.create(this.screenManager), false);
            fab.setVisibility(View.VISIBLE);
        } else if (id == R.id.all_menu_item) {
            screenManager.openFragment(AllTasksFragment.create(this.screenManager), false);
            fab.setVisibility(View.VISIBLE);
        } else if (id == R.id.preference) {
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new SettingsFragment())
                    .commit();
        } else if (id == R.id.about) {
            screenManager.openFragment(About.create(this.screenManager), false);
            fab.setVisibility(View.INVISIBLE);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        public static final String PREF_KEY_THEME_COLOR = "pref_theme_color";
        public static final String PREF_KEY_SHOW_IMAGE = "pref_key_show_image";
        public static final String PREF_KEY_SHOW_LOCATION = "pref_key_show_location";
        public static final String PREF_KEY_SHOW_REMINDER = "pref_key_show_reminder";
        public static final String PREF_KEY_SORT = "pref_key_sort";

        private SharedPreferences settings;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            MainActivity activity = (MainActivity) getActivity();
            activity.getSupportActionBar().setTitle("Preferences");
            addPreferencesFromResource(R.xml.preferences);
            activity.setFabVisibility(View.GONE);
            settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        }

        @Override
        public void onResume() {
            super.onResume();
            getView().setBackgroundColor(Color.parseColor("#e0e0e0"));
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            if (s.equals(PREF_KEY_THEME_COLOR)) {
                String theme = settings.getString(PREF_KEY_THEME_COLOR, "");
                Window window = getActivity().getWindow();
                int primaryColor = Color.parseColor("#2196f3");
                int primaryDarkColor = Color.parseColor("#0d47a1");
                if (theme.equals("theme_default")) {
                    primaryColor = Color.parseColor("#2196f3");
                    primaryDarkColor = Color.parseColor("#0d47a1");
                } else if (theme.equals("theme_teal")) {
                    primaryColor = Color.parseColor("#009688");
                    primaryDarkColor = Color.parseColor("#004d40");
                } else if (theme.equals("theme_purple")) {
                    primaryColor = Color.parseColor("#673ab7");
                    primaryDarkColor = Color.parseColor("#311b92");
                } else if (theme.equals("theme_grey")) {
                    primaryColor = Color.parseColor("#607d8b");
                    primaryDarkColor = Color.parseColor("#263238");
                } else if (theme.equals("theme_orange")) {
                    primaryColor = Color.parseColor("#ff5722");
                    primaryDarkColor = Color.parseColor("#bf360c");
                } else if (theme.equals("theme_pink")) {
                    primaryColor = Color.parseColor("#e91e63");
                    primaryDarkColor = Color.parseColor("#880e4f");
                }
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.setNavTheme();
                mainActivity.setFabColor(primaryColor);
                mainActivity.setToolbarColor(primaryColor);
                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(primaryDarkColor);
                }
            }
        }
    }
}
