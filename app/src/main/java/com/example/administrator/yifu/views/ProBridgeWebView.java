package com.example.administrator.yifu.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.example.administrator.yifu.R;
import com.example.administrator.yifu.utils.CommonUtils;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.orhanobut.logger.Logger;

/**
 * Created by ben on 2017/2/24.
 */
public class ProBridgeWebView extends BridgeWebView
{
    private ProgressBar progressbar;

    private LoadingProgressListener pListener;

    public ProBridgeWebView(Context context)
    {
        super(context);
    }

    public ProBridgeWebView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        progressbar = new ProgressBar(context, null,
                android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                CommonUtils.dp2px(context,2.5f), 0, 0));

        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.progress_bar_states);
        progressbar.setProgressDrawable(drawable);
        progressbar.setVisibility(GONE);
        addView(progressbar);
        setWebChromeClient(new MyWebChromeClient());
        setWebViewClient(new MyWebViewClient(this));
        //是否可以缩放
        getSettings().setSupportZoom(false);
        getSettings().setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT >= 20) // KITKAT
        {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        setBackgroundColor(ContextCompat.getColor(context,R.color.FC5));
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public void setProgressListener(LoadingProgressListener listener)
    {
        this.pListener = listener;
    }

    public class MyWebChromeClient extends WebChromeClient
    {
        @Override
        public void onProgressChanged(WebView view, int newProgress)
        {
            if (pListener != null)
                pListener.onProgressChanged(newProgress);
            if (newProgress == 100)
            {
                progressbar.setVisibility(GONE);
            }
            else
            {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    public class MyWebViewClient extends BridgeWebViewClient
    {
        public MyWebViewClient(BridgeWebView webView)
        {
            super(webView);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error)
        {
            super.onReceivedError(view, request, error);
            if (Build.VERSION.SDK_INT >= 24)
            {
                Logger.t("ProgressBridgeWebView").d(request.getUrl().toString() + " | " +
                        request.getRequestHeaders().toString() + " | " + request.getMethod() + " error>" + error.getDescription());
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
        {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Logger.t("ProgressBridgeWebView").d("ProgressBridgeWebView--error");
            loadUrl("file:///android_asset/404.html");
        }
    }

    public interface LoadingProgressListener
    {
        void onProgressChanged(int progress);
    }
}
