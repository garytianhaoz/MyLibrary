package com.example.yangchengyu.mylibrary.UI.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.yangchengyu.mylibrary.Bean.BookInfo;
import com.example.yangchengyu.mylibrary.R;
import com.example.yangchengyu.mylibrary.Utils.JSONUtils;
import com.example.yangchengyu.mylibrary.Utils.StatusBarActivity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;

/**
 * Created by YangChengyu on 2017/5/3.
 */

public class ScanActivity extends StatusBarActivity implements QRCodeView.Delegate, View.OnClickListener {

    private static final String TAG = ScanActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    private static final int SCAN_SUCCESS=100;
    private static final int TIMEOUT = 8000;
    private static final int CONNECT_TIMEOUT = 10000;
    public static final String GET = "get";
    public static final int SCAN_FAILED = 111;
    private QRCodeView mQrCodeView;
    private Button mButton_flash;
    private Button mButton_isbn;
    private boolean isFlashing = false;
    private boolean isISBN = false;
    private Button mButton_gallery;
    private static final String http_api = "http://feedback.api.juhe.cn/ISBN";
    private static final String key = "47d52e601f78b1204ec61a3ac7667730";
    private BookInfo mBookInfo;

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==SCAN_SUCCESS){
                Toast.makeText(ScanActivity.this,"扫描成功，请稍后",Toast.LENGTH_SHORT).show();
            }
            else if(msg.what==SCAN_FAILED){
                Toast.makeText(ScanActivity.this,"未找到该书\nError:"+msg.obj.toString(),Toast.LENGTH_SHORT).show();
                mQrCodeView.startSpotAndShowRect();
            }
        }
    };

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_scan;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("扫一扫");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mButton_flash = (Button) findViewById(R.id.btn_scan_flash);
        mButton_isbn = (Button) findViewById(R.id.btn_scan_ISBN);
        mButton_gallery = (Button) findViewById(R.id.btn_scan_gallery);

        mButton_flash.setOnClickListener(this);
        mButton_isbn.setOnClickListener(this);
        mButton_gallery.setOnClickListener(this);

        mQrCodeView = (QRCodeView) findViewById(R.id.zxingView);
        mQrCodeView.setDelegate(this);

        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQrCodeView.startCamera();
        mQrCodeView.startSpot();
        mQrCodeView.showScanRect();
    }

    @Override
    protected void onStop() {
        mQrCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQrCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i(TAG, "result:" + result);
        vibrate();
        mHandler.sendEmptyMessage(SCAN_SUCCESS);
        final String getBookISBN_url = http_api + "?sub=" + result.toString() + "&key=" +key;

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                String Jsontext = JSONUtils.getBookISBN(getBookISBN_url);
                try {
                    JSONObject jsonObject = new JSONObject(Jsontext);
                    String reason = jsonObject.getString("reason");
                    String error_code = jsonObject.getString("error_code");
                    if (reason.equals("查询成功")) {
                        JSONObject Jsonresult = jsonObject.getJSONObject("result");
                        String title = Jsonresult.getString("title");
                        String author = Jsonresult.getString("author");
                        String pubdate = Jsonresult.getString("pubdate");
                        String publisher = Jsonresult.getString("publisher");
                        String summary = Jsonresult.getString("summary");
                        String image = Jsonresult.getString("images_medium");
                        mBookInfo = new BookInfo(title, author, publisher, pubdate, summary, image);
                        Intent intent = new Intent(ScanActivity.this, DetailActivity.class);
                        intent.putExtra("title", mBookInfo.getTitle());
                        intent.putExtra("info", mBookInfo.getInfoString());
                        intent.putExtra("introduce", mBookInfo.getSummary());
                        intent.putExtra("image", mBookInfo.getImage());

                        intent.putExtra("author",mBookInfo.getAuthor());
                        intent.putExtra("pubdate",mBookInfo.getPubdate());
                        intent.putExtra("publisher",mBookInfo.getPublisher());
                        startActivity(intent);
                    } else {
                        Message message=new Message();
                        message.what=SCAN_FAILED;
                        message.obj=error_code;
                        mHandler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scan_flash:
                if (isFlashing) {
                    mQrCodeView.closeFlashlight();
                    mButton_flash.setText("Open flash");
                    isFlashing = false;
                } else {
                    mQrCodeView.openFlashlight();
                    mButton_flash.setText("Close flash");
                    isFlashing = true;
                }
                break;
            case R.id.btn_scan_ISBN:
                if (isISBN) {
                    mQrCodeView.changeToScanQRCodeStyle();
                    mButton_isbn = (Button) findViewById(R.id.btn_scan_ISBN);
                    mButton_isbn.setText("BarCode");
                    isISBN = false;
                } else {
                    mQrCodeView.changeToScanBarcodeStyle();
                    mButton_isbn.setText("QRCode");
                    isISBN = true;
                }
                break;
            case R.id.btn_scan_gallery:
                startActivityForResult(BGAPhotoPickerActivity.newIntent(this, null, 1, null, false), REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mQrCodeView.showScanRect();

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            final String picturePath = BGAPhotoPickerActivity.getSelectedImages(data).get(0);
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    return QRCodeDecoder.syncDecodeQRCode(picturePath);
                }

                @Override
                protected void onPostExecute(String result) {
                    if (TextUtils.isEmpty(result)) {
                        Toast.makeText(ScanActivity.this, "未发现二维码", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ScanActivity.this, result, Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        }

        onScanQRCodeSuccess(data.getExtras().getString("result"));
    }
}
