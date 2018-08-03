package com.qingmang.market.constant;

import android.content.Context;

import com.qingmang.market.utils.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by bruce on 2018/6/15.
 */

public class MerchantInfo
{
    /**
     * mock数据，真实商户请填写真实信息.
     */

    //这里三个值请填写自己真实的值
    //应用的签名私钥
    public final static String appKey = "asd";
    //商户id
    public final static String partnerId = "2088821635468298";
    //应用的appId
    public final static String appId = "3";

    public static Map mockInfo() {
        return merchantInfo;

    }
    public static Map merchantInfo = new HashMap();

    public static void initMockInfo(Context c) {
        MerchantInfo.mockInfo().put("partnerId", "2088821635468298");
        MerchantInfo.mockInfo().put("merchantId", "2088131709355682");
        MerchantInfo.mockInfo().put("appId", appId);
        MerchantInfo.mockInfo().put("alipayStoreCode", "");
        MerchantInfo.mockInfo().put("deviceNum", CommonUtils.getAndroidOsSystemProperties());
        MerchantInfo.mockInfo().put("storeCode", "");
        MerchantInfo.mockInfo().put("brandCode", "yiFu");
        MerchantInfo.mockInfo().put("areaCode", "tianJin");
        MerchantInfo.mockInfo().put("geo", "");
        MerchantInfo.mockInfo().put("wifiMac", CommonUtils.getMac(c));
        MerchantInfo.mockInfo().put("wifiName",CommonUtils.getConnectWifiSsid(c));
        MerchantInfo.mockInfo().put("deviceMac",CommonUtils.getMac(c));

    }
    public static void addMockInfo(Context c, String info) {
        try
        {
            JSONObject jsonObject=new JSONObject(info);
            MerchantInfo.mockInfo().put("partnerId",jsonObject.getString("partnerId"));
            MerchantInfo.mockInfo().put("merchantId",jsonObject.getString("merchantId"));
            MerchantInfo.mockInfo().put("appId",jsonObject.getString("appId"));
            MerchantInfo.mockInfo().put("alipayStoreCode",jsonObject.getString("alipayStoreCode"));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

    }
}
