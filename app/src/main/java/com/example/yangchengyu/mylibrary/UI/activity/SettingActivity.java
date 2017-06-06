package com.example.yangchengyu.mylibrary.UI.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;

import com.example.yangchengyu.mylibrary.R;
import com.example.yangchengyu.mylibrary.Utils.NetworkUtils;
import com.lb.material_preferences_library.PreferenceActivity;
import com.lb.material_preferences_library.custom_preferences.ListPreference;

/**
 * Created by YangChengyu on 2017/5/16.
 */

public class SettingActivity extends PreferenceActivity {

    private Preference mPreference_net_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        set_default_mode();
        super.onCreate(savedInstanceState);
        ListPreference themeListPreference = (ListPreference) findPreference(getString(R.string.pref_key_night_day_setting));
        themeListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                restartActivity(SettingActivity.this);
                return true;
            }
        });

        mPreference_net_state = findPreference(getString(R.string.pref_key_net_flow_info));
        int a = NetworkUtils.getConnectedType(this);
        switch (a) {
            case ConnectivityManager.TYPE_WIFI:
                mPreference_net_state.setSummary("当前网络：WIFI");
                break;
            case ConnectivityManager.TYPE_MOBILE:
                mPreference_net_state.setSummary("当前网络：移动网络");
                break;
            default:
                mPreference_net_state.setSummary("");
                break;
        }
    }

    public void set_default_mode() {
        final String themePrefKey = getString(R.string.pref_key_night_day_setting), defaultTheme = getResources().getString(R.string.pref_theme_default);
        final String theme = PreferenceManager.getDefaultSharedPreferences(this).getString(themePrefKey, defaultTheme);
        switch (theme) {
            case "dark":
                setTheme(R.style.AppTheme_Dark);
                break;
            case "light":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }
    }

    @Override
    protected int getPreferencesXmlId() {
        return R.xml.pref_settings;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void restartActivity(final Activity activity) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            activity.recreate();
        else {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    activity.overridePendingTransition(0, 0);
                    activity.startActivity(activity.getIntent());
                }
            });
            activity.finish();
        }
    }
}
