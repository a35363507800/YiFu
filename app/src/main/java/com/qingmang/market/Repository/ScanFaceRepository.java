package com.qingmang.market.Repository;


import com.qingmang.market.constant.HttpMethods;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;

/**
 * Created by Administrator on 2018/7/5.
 *
 * @author ling
 */
public class ScanFaceRepository
{
    private static final String TAG = "ScanFaceRepository";

    /**
     * 获取zimId ，zimInitClientData
     *
     * @param observer
     */
    public void getScanFaceData(String cinfo,String interfaceName, Observer<String> observer)
    {
        Map<String, Object> reqMap =new HashMap<>();
        reqMap.put("aid","3");
        reqMap.put("key","asd");
        reqMap.put("cinfo", cinfo);
        HttpMethods.getInstance().startServerRequest(observer, interfaceName, reqMap, true);
    }

}
