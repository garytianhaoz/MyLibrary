package com.example.yangchengyu.mylibrary.Utils;

import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by YangChengyu on 2017/5/7.
 */

public class JSONUtils {
    private static final int TIMEOUT = 8000;
    private static final int CONNECT_TIMEOUT = 10000;
    private static final String GET = "GET";
    private static final String UTF_8 = "UTF-8";
    public static final String TAG = JSONUtils.class.getSimpleName();

    public static void getBook(final String url, final android.os.Handler handler) {
        Thread thread = new Thread(new Runnable() {

            private BufferedReader mBufferedReader;
            private InputStream mInputStream;
            private HttpURLConnection mHttpURLConnection;

            @Override
            public void run() {

                try {
                    URL http_url = new URL(url);
                    mHttpURLConnection = (HttpURLConnection) http_url.openConnection();
                    mHttpURLConnection.setRequestMethod(GET);
                    mHttpURLConnection.setReadTimeout(TIMEOUT);
                    mHttpURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
                    mInputStream = mHttpURLConnection.getInputStream();
                    mBufferedReader = new BufferedReader(new InputStreamReader(mInputStream));
                    String strRead = null;
                    StringBuilder result = new StringBuilder();

                    while ((strRead = mBufferedReader.readLine()) != null) {
                        result.append(strRead);
                    }

                    Message msg = new Message();
                    msg.obj = result.toString();
                    handler.sendMessage(msg);

                    mBufferedReader.close();
                    mInputStream.close();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        thread.start();
    }

    public static String getBookISBN(final String url) {
        final String[] result = {null};

        Thread thread = new Thread(new Runnable() {

            private HttpURLConnection mHttpURLConnection;
            private StringBuffer mStringBuffer;

            @Override
            public void run() {
                InputStream inputStream = null;
                BufferedReader bufferedReader = null;
                mStringBuffer = new StringBuffer();

                try {
                    URL target = new URL(url);

                    mHttpURLConnection = (HttpURLConnection) target.openConnection();
                   // mHttpURLConnection.setReadTimeout(TIMEOUT);
                   // mHttpURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
                    mHttpURLConnection.setRequestMethod(GET);
                    mHttpURLConnection.connect();

                    inputStream = mHttpURLConnection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream, UTF_8));
                    String strRead = null;

                    while ((strRead = bufferedReader.readLine()) != null) {

                        mStringBuffer.append(strRead);
                        mStringBuffer.append("\r\n");
                    }

                    result[0] = mStringBuffer.toString();
                    bufferedReader.close();
                    inputStream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i(TAG, Error.class.toString());
                } finally {
                    if (mHttpURLConnection != null) {
                        mHttpURLConnection.disconnect();
                    }
                }

            }
        });

        thread.start();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result[0];
    }
}
