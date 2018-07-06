package com.example.administrator.yifu.Repository;


import com.example.administrator.yifu.constant.HttpMethods;

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
    public void getScanFaceData(String interfaceName, Observer<String> observer)
    {
        Map<String, Object> reqMap =new HashMap<>();

        HttpMethods.getInstance().startServerRequest(observer, interfaceName, reqMap, true);
    }

}
