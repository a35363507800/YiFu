package com.qingmang.market.act;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;

import android.widget.Toast;

import com.alipay.zoloz.smile2pay.service.Zoloz;
import com.alipay.zoloz.smile2pay.service.ZolozCallback;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.qingmang.market.R;
import com.qingmang.market.constant.MerchantInfo;
import com.qingmang.market.modes.ScanFaceMode;
import com.qingmang.market.utils.AidlUtil;
import com.qingmang.market.utils.PrintUtils;
import com.qingmang.market.utils.ToastUtils;
import com.qingmang.market.utils.ZolozUtils;
import com.qingmang.market.views.ProBridgeWebView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.qingmang.market.constant.MerchantInfo.mockInfo;


public class MainActivity extends BaseActivity
{
    @BindView(R.id.web_ranking)
    ProBridgeWebView mWebView;
    private Activity mActivity;
    private final String URL = "https://www.greenmangodata.com/self-purchase/index.html";
    private final String TESTURL = "http://192.168.1.214:3000/index.html";
    private final String URL2 = "https://www.greenmangodata.com/client/market/index.html";
    private Zoloz zoloz;
    private ScanFaceMode scanFaceMode;
    private static final String TAG = "MainActivity";
    private CallBackFunction payFunction =null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //       设置屏幕始终在前面，不然点击鼠标，重新出现虚拟按键
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav
                        // bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);


        if (Build.VERSION.SDK_INT >= 20)
        {
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setBlockNetworkImage(false);//

      //  mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setDatabasePath(MainActivity.this.getApplicationContext().getCacheDir().getAbsolutePath());

        mWebView.loadUrl(URL2);


        mWebView.registerHandler("facepay", new BridgeHandler()
        {
            @Override
            public void handler(String data, CallBackFunction function)
            {
                zoloz.zolozGetMetaInfo(mockInfo(), new ZolozCallback() {
                    @Override
                    public void response(Map smileToPayResponse) {

                        if (smileToPayResponse == null) {
                            Log.e(TAG, "response is null");
                            ToastUtils.showLong(TXT_OTHER);
                            return;
                        }

                        String code = (String)smileToPayResponse.get("code");
                        String metaInfo = (String)smileToPayResponse.get("metainfo");

                        //获取metainfo成功
                        if (CODE_SUCCESS.equalsIgnoreCase(code) && metaInfo != null)
                        {
                            function.onCallBack(metaInfo);
                          //  scanFaceMode.getScanFaceData(metaInfo);
                        }

                    }
                });
            }
        });
        mWebView.registerHandler("facepay2", new BridgeHandler()
        {
            @Override
            public void handler(String data, CallBackFunction function)
            {
                JSONObject jsonObject = null;
                try
                {
                    payFunction=function;
                    jsonObject = new JSONObject(data);
                    smile(jsonObject.getString("zimId"), jsonObject.getString("zimInitClientData"),function);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        });

        mWebView.registerHandler("mokeInfo", new BridgeHandler()
        {
            @Override
            public void handler(String data, CallBackFunction function)
            {
                MerchantInfo.addMockInfo(MainActivity.this,data);

//                //初始化
//                zoloz.zolozInstall(MerchantInfo.mockInfo());
            }
        });


        mWebView.registerHandler("print", new BridgeHandler()
        {
            @Override
            public void handler(String data, CallBackFunction function)
            {
                PrintUtils.createPrintBitmap(MainActivity.this, data,findViewById(R.id.image));
                function.onCallBack(AidlUtil.getInstance().updatePrinterState()+"");

            }
        });


        //zolozInstall初始化，该初始化务必放在app启动的时候，否则影响人脸的正常使用
        zoloz = com.alipay.zoloz.smile2pay.service.Zoloz.getInstance(getApplicationContext());

        scanFaceMode=new ScanFaceMode();
        scanFaceMode.getScanFaceDataResult().observe(this, new Observer<String>()
        {
            @Override
            public void onChanged(@Nullable String s)
            {

                if (!TextUtils.isEmpty(s))
                {
                    try
                    {
                        JSONObject jsonObject = new JSONObject(s);
                        ZolozUtils.smile(jsonObject.getString("zimId"), jsonObject.getString("zimInitClientData"), zoloz, callBack);
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }
            }
        });

        AidlUtil.getInstance().initPrinter();

    }


    private void getLocalStorageUserKey()
    {
        if (mWebView != null)
        {
            mWebView.loadUrl(
                    "javascript:(function(){ var localStorage = window.localStorage; window.shixintest.getUserKey(localStorage.getItem('userKey'))})()");
        }
    }
    // 值为"1000"调用成功
    // 值为"1003"用户选择退出
    // 值为"1004"超时
    // 值为"1005"用户选用其他支付方式
    static final String CODE_SUCCESS = "1000";
    static final String CODE_EXIT = "1003";
    static final String CODE_TIMEOUT = "1004";
    static final String CODE_OTHER_PAY = "1005";

    static final String TXT_EXIT = "已退出刷脸支付";
    static final String TXT_TIMEOUT = "操作超时";
    static final String TXT_OTHER_PAY = "已退出刷脸支付";
    static final String TXT_OTHER = "抱歉未支付成功，请重新支付";

    //刷脸支付相关
    static final String SMILEPAY_CODE_SUCCESS = "10000";
    static final String SMILEPAY_SUBCODE_LIMIT = "ACQ.PRODUCT_AMOUNT_LIMIT_ERROR";
    static final String SMILEPAY_SUBCODE_BALANCE_NOT_ENOUGH = "ACQ.BUYER_BALANCE_NOT_ENOUGH";
    static final String SMILEPAY_SUBCODE_BANKCARD_BALANCE_NOT_ENOUGH = "ACQ.BUYER_BANKCARD_BALANCE_NOT_ENOUGH";

    static final String SMILEPAY_TXT_LIMIT = "刷脸支付超出限额，请选用其他支付方式";
    static final String SMILEPAY_TXT_EBALANCE_NOT_ENOUGH = "账户余额不足，支付失败";
    static final String SMILEPAY_TXT_BANKCARD_BALANCE_NOT_ENOUGH = "账户余额不足，支付失败";
    static final String SMILEPAY_TXT_FAIL = "抱歉未支付成功，请重新支付";
    static final String SMILEPAY_TXT_SUCCESS = "刷脸支付成功";
    public static final String KEY_INIT_RESP_NAME = "zim.init.resp";

    /**
     * 发起刷脸支付请求.
     * @param zimId 刷脸付token，从服务端获取，不要mock传入
     * @param protocal 刷脸付协议，从服务端获取，不要mock传入
     */
    private void smile(String zimId, String protocal,CallBackFunction function) {
        Map params = new HashMap();
        params.put(KEY_INIT_RESP_NAME, protocal);
        zoloz.zolozVerify(zimId, params, new ZolozCallback() {
            @Override
            public void response(final Map smileToPayResponse) {
                if (smileToPayResponse == null) {
                    promptText(TXT_OTHER);
                    return;
                }
                String code = (String)smileToPayResponse.get("code");
                String fToken = (String)smileToPayResponse.get("ftoken");
                String subCode = (String)smileToPayResponse.get("subCode");
                String msg = (String)smileToPayResponse.get("msg");
                Log.d(TAG, "ftoken is:" + fToken);

                //刷脸成功
                if (CODE_SUCCESS.equalsIgnoreCase(code) && fToken != null) {
                    //promptText("刷脸成功，返回ftoken为:" + fToken);
                    //这里在Main线程，网络等耗时请求请放在异步线程中
                    //后续这里可以发起支付请求
                    //https://docs.open.alipay.com/api_1/alipay.trade.pay
                    //需要修改两个参数
                    //scene固定为security_code
                    //auth_code为这里获取到的fToken值
                    //支付一分钱，支付需要在服务端发起，这里只是模拟
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try
                                {
                                    function.onCallBack(  new JSONStringer().object().key("status").value(code).key("ftoken").value(fToken).endObject().toString());
                                } catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }

                            }
                        });

                    } catch (Exception e) {
                        promptText(SMILEPAY_TXT_FAIL);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try
                                {
                                    function.onCallBack(  new JSONStringer().object().key("status").value(CODE_EXIT).endObject().toString());
                                } catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                } else if (CODE_EXIT.equalsIgnoreCase(code)) {
                    promptText(TXT_EXIT);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try
                            {
                                function.onCallBack(  new JSONStringer().object().key("status").value(CODE_EXIT).endObject().toString());
                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });
                } else if (CODE_TIMEOUT.equalsIgnoreCase(code)) {
                    promptText(TXT_TIMEOUT);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try
                            {
                                function.onCallBack(  new JSONStringer().object().key("status").value(TXT_TIMEOUT).endObject().toString());
                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });
                } else if (CODE_OTHER_PAY.equalsIgnoreCase(code)) {
                    promptText(TXT_OTHER_PAY);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try
                            {
                                function.onCallBack(  new JSONStringer().object().key("status").value(CODE_OTHER_PAY).endObject().toString());
                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    String txt = TXT_OTHER;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try
                            {
                                function.onCallBack(  new JSONStringer().object().key("status").value("1006").endObject().toString());
                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });
                    if (!TextUtils.isEmpty(subCode)) {
                        txt = txt + "(" + subCode + ")";
                    }
                    promptText(txt);
                }
            }

        });
    }

    /**
     * 发起刷脸支付请求.
     * @param txt toast文案
     */
    void promptText(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_LONG).show();
            }
        });
    }


    private CallBack callBack = new CallBack()
    {
        @Override
        public void response(String fToken)
        {
             if(payFunction!=null)
             {
                 payFunction.onCallBack(fToken);
             }

        }
    };

    public interface CallBack
    {
        void response(String fToken);
    }


}
