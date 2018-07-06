package com.example.administrator.yifu.act;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alipay.zoloz.smile2pay.service.Zoloz;
import com.alipay.zoloz.smile2pay.service.ZolozCallback;
import com.example.administrator.yifu.R;
import com.example.administrator.yifu.modes.ScanFaceMode;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import static com.example.administrator.yifu.constant.MerchantInfo.appId;
import static com.example.administrator.yifu.constant.MerchantInfo.appKey;
import static com.example.administrator.yifu.constant.MerchantInfo.mockInfo;

/**
 * Created by Administrator on 2018/7/5.
 *
 * @author ling
 */
public class ScanFaceAct extends  BaseActivity
{

    private static final String TAG = "smiletopay";
    Button mSmilePayButton;

    public static final String KEY_INIT_RESP_NAME = "zim.init.resp";
    private Zoloz zoloz;

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

    private ScanFaceMode scanFaceMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanFaceMode=new ScanFaceMode();

        scanFaceMode.getScanFaceDataResult().observe(ScanFaceAct.this,String->
        {
            String zimId ="zimId";
            String zimInitClientData = "zimInitClientData";

            smile(zimId, zimInitClientData);


        });


        //在这之前先zolozInstall初始化，该初始化务必放在app启动的时候，见MYApplication否则影响人脸的正常使用
        zoloz = com.alipay.zoloz.smile2pay.service.Zoloz.getInstance(getApplicationContext());
        //zoloz.zolozInstall(mockInfo());
        smilePay();
    }

    /**
     * 发起刷脸支付请求，先zolozGetMetaInfo获取本地app信息，然后调用服务端获取刷脸付协议.
     */
    private void smilePay() {
        zoloz.zolozGetMetaInfo(mockInfo(), new ZolozCallback() {
            @Override
            public void response(Map smileToPayResponse) {
                if (smileToPayResponse == null) {
                    Log.e(TAG, "response is null");
                    promptText(TXT_OTHER);
                    return;
                }

                String code = (String)smileToPayResponse.get("code");
                String metaInfo = (String)smileToPayResponse.get("metainfo");

                //获取metainfo成功
                if (CODE_SUCCESS.equalsIgnoreCase(code) && metaInfo != null) {
                    com.orhanobut.logger.Logger.t(TAG).d("扫脸获取metainfo成功");


                    scanFaceMode.getScanFaceData();


                } else {
                    promptText(TXT_OTHER);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        zoloz.zolozUninstall();
    }

    /**
     * 发起刷脸支付请求.
     * @param zimId 刷脸付token，从服务端获取，不要mock传入
     * @param protocal 刷脸付协议，从服务端获取，不要mock传入
     */
    private void smile(String zimId, String protocal) {
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
                        pay(fToken, "0.01");
                    } catch (Exception e) {
                        promptText(SMILEPAY_TXT_FAIL);
                    }
                } else if (CODE_EXIT.equalsIgnoreCase(code)) {
                    promptText(TXT_EXIT);
                } else if (CODE_TIMEOUT.equalsIgnoreCase(code)) {
                    promptText(TXT_TIMEOUT);
                } else if (CODE_OTHER_PAY.equalsIgnoreCase(code)) {
                    promptText(TXT_OTHER_PAY);
                } else {
                    String txt = TXT_OTHER;
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

    /**
     * 发起刷脸支付请求.
     * @param ftoken 刷脸返回的token
     * @param amount 支付金额
     */
    private void pay(String ftoken, String amount) throws Exception {
//        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
//                appId,
//                appKey,
//                "json",
//                "utf-8",
//                null,
//                "RSA2");
//        AlipayTradePayRequest alipayTradePayRequest = new AlipayTradePayRequest();
//        TradepayParam tradepayParam = new TradepayParam();
//        tradepayParam.setOut_trade_no(UUID.randomUUID().toString());
//
//        //auth_code和scene填写需要注意
//        tradepayParam.setAuth_code(ftoken);
//        tradepayParam.setScene("security_code");
//        tradepayParam.setSubject("smilepay");
//        tradepayParam.setStore_id("smilepay test");
//        tradepayParam.setTimeout_express("5m");
//        tradepayParam.setTotal_amount(amount);
//        alipayTradePayRequest.setBizContent(JSON.toJSONString(tradepayParam));
//        alipayClient.execute(alipayTradePayRequest,
//                new AlipayCallBack() {
//
//                    @Override
//                    public AlipayResponse onResponse(AlipayResponse response) {
//                        if (response != null && SMILEPAY_CODE_SUCCESS.equals(response.getCode())) {
//                            promptText(SMILEPAY_TXT_SUCCESS);
//                        } else {
//                            if (response != null) {
//                                String subCode = response.getSubCode();
//                                if (SMILEPAY_SUBCODE_LIMIT.equalsIgnoreCase(subCode)) {
//                                    promptText(SMILEPAY_TXT_LIMIT);
//                                } else if(SMILEPAY_SUBCODE_BALANCE_NOT_ENOUGH.equalsIgnoreCase(subCode)) {
//                                    promptText(SMILEPAY_TXT_EBALANCE_NOT_ENOUGH);
//                                } else if(SMILEPAY_SUBCODE_BANKCARD_BALANCE_NOT_ENOUGH.equalsIgnoreCase(subCode)) {
//                                    promptText(SMILEPAY_TXT_BANKCARD_BALANCE_NOT_ENOUGH);
//                                } else {
//                                    promptText(SMILEPAY_TXT_FAIL);
//                                }
//                            } else {
//                                promptText(SMILEPAY_TXT_FAIL);
//                            }
//                        }
//                        return null;
//                    }
//                });
//        return;
    }

}
