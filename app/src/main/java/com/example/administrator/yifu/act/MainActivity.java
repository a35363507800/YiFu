package com.example.administrator.yifu.act;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.administrator.yifu.R;
import com.example.administrator.yifu.views.ProBridgeWebView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
{
    @BindView(R.id.web_ranking)
    ProBridgeWebView mWebView;
    private Activity mActivity;
    private final String URL="https://www.greenmangodata.com/self-purchase/index.html";
    private final String TESTURL="http://192.168.1.214:3000/index.html";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 20)
        {
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);



        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setDatabasePath(MainActivity.this.getApplicationContext().getCacheDir().getAbsolutePath());

        mWebView.addJavascriptInterface(new HybridInterface(this), "shixintest");

        mWebView.loadUrl(URL);



    }


    private void getLocalStorageUserKey() {
        if (mWebView != null) {
            mWebView.loadUrl(
                    "javascript:(function(){ var localStorage = window.localStorage; window.shixintest.getUserKey(localStorage.getItem('userKey'))})()");
    }
}


    public class HybridInterface {
        Context context;

        HybridInterface(Context context) {
            this.context = context;
        }

        //Js 回调方法，
        @JavascriptInterface
        public void getUserKey(String userKey){

            //已经拿到值，进行相关操作
        }

    }

}
