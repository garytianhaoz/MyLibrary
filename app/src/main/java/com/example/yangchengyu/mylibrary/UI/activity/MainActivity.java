package com.example.yangchengyu.mylibrary.UI.activity;

import android.Manifest;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.yangchengyu.mylibrary.R;
import com.example.yangchengyu.mylibrary.UI.Adapter.ViewPagerAdapter;
import com.example.yangchengyu.mylibrary.UI.Fragment.BookListCardFragment;
import com.example.yangchengyu.mylibrary.UI.Fragment.MyLibraryFragment;
import com.example.yangchengyu.mylibrary.UI.Fragment.SettingFragment;
import com.example.yangchengyu.mylibrary.Utils.BottomNavigationViewHelper;
import com.example.yangchengyu.mylibrary.Utils.StatusBarActivity;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends StatusBarActivity implements ViewPager.OnPageChangeListener, EasyPermissions.PermissionCallbacks, SearchView.OnQueryTextListener {

    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private BottomNavigationView mBottom_navigation;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ViewPager mViewPager;
    private MenuItem menuItem;
    private SearchView mSearchView;

    @Override
    protected void onStart() {
        requestCodeQRCodePermissions();
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_activity_drawer);

        mNavigationView = (NavigationView) findViewById(R.id.main_activity_navigation);
        mNavigationView.setNavigationItemSelectedListener(mNavigationItemSelectedListener);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        mViewPager = (ViewPager) findViewById(R.id.main_activity_viewPager);

        mBottom_navigation = (BottomNavigationView) findViewById(R.id.main_activity_bottom_navigation);
        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
        BottomNavigationViewHelper.disableShiftMode(mBottom_navigation);
        mBottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        setupAdapter(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(mFabOnClickListener);

       // SettingActivity.restartActivity(MainActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search for your book");
        mSearchView.onActionViewExpanded();
        mSearchView.setIconifiedByDefault(true);

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            item.setChecked(true);
            switch (item.getItemId()) {
                case R.id.menu_bottom_home:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.menu_bottom_my_library:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.menu_bottom_setting:
                    mViewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    private NavigationView.OnNavigationItemSelectedListener mNavigationItemSelectedListener
            = new NavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            item.setChecked(true);
            int menuItem = item.getItemId();
            switch (menuItem) {
                case R.id.nav_scan:
                    startActivityForResult(new Intent(MainActivity.this, ScanActivity.class), 0);
                    break;
                case R.id.nav_about:
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                    break;
            }
            mDrawerLayout.closeDrawers();
            return false;
        }
    };

    private FloatingActionButton.OnClickListener mFabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, ScanActivity.class));
        }
    };

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (menuItem != null) {
                menuItem.setChecked(false);
            } else {
                mBottom_navigation.getMenu().getItem(0).setChecked(false);
            }
            menuItem = mBottom_navigation.getMenu().getItem(position);
            menuItem.setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setupAdapter(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(BookListCardFragment.newInstance("推荐"));
        viewPagerAdapter.addFragment(MyLibraryFragment.newInstance("图书"));
        viewPagerAdapter.addFragment(SettingFragment.newInstance("设置"));

        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
