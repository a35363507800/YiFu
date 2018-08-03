package com.qingmang.market.utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


import com.alipay.zoloz.smile2pay.service.Zoloz;
import com.alipay.zoloz.smile2pay.service.ZolozCallback;
import com.qingmang.market.act.MainActivity;
import com.qingmang.market.constant.HttpMethods;
import com.qingmang.market.constant.SilenceSubscriber;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by bruce on 2018/6/15.
 */
public class ZolozUtils
{
    private static final String TAG = "smiletopay";
    Button mSmilePayButton;

    public static final String KEY_INIT_RESP_NAME = "zim.init.resp";
    private Zoloz zoloz;

    // 值为"1000"调用成功
    // 值为"1003"用户选择退出
    // 值为"1004"超时
    // 值为"1005"用户选用其他支付方式
    public static final String CODE_SUCCESS = "1000";
    static final String CODE_EXIT = "1003";
    static final String CODE_TIMEOUT = "1004";
    static final String CODE_OTHER_PAY = "1005";

    static final String TXT_EXIT = "已退出刷脸支付";
    static final String TXT_TIMEOUT = "操作超时";
    static final String TXT_OTHER_PAY = "已退出刷脸支付";
    public static final String TXT_OTHER = "抱歉未支付成功，请重新支付";

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




    /**
     * 发起刷脸支付请求.
     * @param zimId 刷脸付token，从服务端获取，不要mock传入
     * @param protocal 刷脸付协议，从服务端获取，不要mock传入
     */
    public static  void smile(String zimId, String protocal, Zoloz zoloz, MainActivity.CallBack callBack) {
        Map params = new HashMap();
        params.put(KEY_INIT_RESP_NAME, protocal);
        zoloz.zolozVerify(zimId, params, new ZolozCallback() {
            @Override
            public void response(final Map smileToPayResponse) {
                if (smileToPayResponse == null) {
                    ToastUtils.showLong(TXT_OTHER);
                    return;
                }

                ToastUtils.showLong(TXT_OTHER);
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
//                    try {
//                        pay(fToken, "0.01");
//                    } catch (Exception e) {
//                        promptText(SMILEPAY_TXT_FAIL);
//                    }

                    callBack.response(fToken);
                } else if (CODE_EXIT.equalsIgnoreCase(code)) {
                    ToastUtils.showLong(TXT_EXIT);
                } else if (CODE_TIMEOUT.equalsIgnoreCase(code)) {
                    ToastUtils.showLong(TXT_TIMEOUT);
                } else if (CODE_OTHER_PAY.equalsIgnoreCase(code)) {
                    ToastUtils.showLong(TXT_OTHER_PAY);
                } else {
                    String txt = TXT_OTHER;
                    if (!TextUtils.isEmpty(subCode)) {
                        txt = txt + "(" + subCode + ")";
                    }
                    ToastUtils.showLong(txt);
                }
            }

        });
    }


    public void requestData()
    {

        Map<String, Object> reqMap =new HashMap<>();
        reqMap.put("appid","2018070660548475");
        reqMap.put("partnerId","2088131709355682");
        HttpMethods.getInstance().startServerRequest(new SilenceSubscriber<String>()
        {
            @Override
            public void onNext(String response)
            {
                super.onNext(response);
             ToastUtils.showLong("服务器成功链接");
            }
        },"api.greenmangodata.com/extern/alipay/mchid",reqMap,true);
    }

//    /**
//     * 发起刷脸支付请求.
//     * @param ftoken 刷脸返回的token
//     * @param amount 支付金额
//     */
//    private void pay(String ftoken, String amount) throws Exception
//    {
//        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
//            appId,
//            appKey,
//            "json",
//            "utf-8",
//            null,
//            "RSA2");
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
//            new AlipayCallBack() {
//
//                @Override
//                public AlipayResponse onResponse(AlipayResponse response) {
//                    if (response != null && SMILEPAY_CODE_SUCCESS.equals(response.getCode())) {
//                        promptText(SMILEPAY_TXT_SUCCESS);
//                    } else {
//                        if (response != null) {
//                            String subCode = response.getSubCode();
//                            if (SMILEPAY_SUBCODE_LIMIT.equalsIgnoreCase(subCode)) {
//                                promptText(SMILEPAY_TXT_LIMIT);
//                            } else if(SMILEPAY_SUBCODE_BALANCE_NOT_ENOUGH.equalsIgnoreCase(subCode)) {
//                                promptText(SMILEPAY_TXT_EBALANCE_NOT_ENOUGH);
//                            } else if(SMILEPAY_SUBCODE_BANKCARD_BALANCE_NOT_ENOUGH.equalsIgnoreCase(subCode)) {
//                                promptText(SMILEPAY_TXT_BANKCARD_BALANCE_NOT_ENOUGH);
//                            } else {
//                                promptText(SMILEPAY_TXT_FAIL);
//                            }
//                        } else {
//                            promptText(SMILEPAY_TXT_FAIL);
//                        }
//                    }
//                    return null;
//                }
//            });
//        return;
//    }
}
