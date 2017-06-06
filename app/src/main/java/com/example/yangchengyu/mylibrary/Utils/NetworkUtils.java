package com.example.yangchengyu.mylibrary.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by YangChengyu on 2017/5/17.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static ConnectivityManager sConnectivityManager;

    public static boolean isNetworkConnected(Context context) {
        try {
            sConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = sConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isConnected();
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "isNetworkConnected fail:" + e.toString());
            // LG，有时没有connectivity_service，抛IllegalArgumentException
            return false;
        }
    }

    public static boolean isWifiConnected(Context context) {
        try {
            sConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = sConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isConnected();
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isMobileConnected(Context context) {
        if (context != null) {
            sConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = sConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }
}
