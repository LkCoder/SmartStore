package com.baidu.idl.main.facesdk.paymentlibrary.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

public abstract class BaseActivity extends Activity {
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        // 将activity设置为全屏显示
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
