package com.example.administrator.yifu;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.alipay.zoloz.smile2pay.service.Zoloz;
import com.example.administrator.yifu.constant.Api;
import com.example.administrator.yifu.utils.CommonUtils;
import com.google.gson.Gson;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.example.administrator.yifu.constant.MerchantInfo.mockInfo;

/**
 * Copyright (C) 2018 科技发展有限公司
 * 完全享有此软件的著作权，违者必究
 *
 * @author ben
 * @version 1.0
 * @modifier
 * @createDate 2018/4/20 16:38
 * @description
 */
public class AppController extends Application //implements HasActivityInjector
{



    private static AppController appInstance;
    private static Gson gson;
    private List<Activity> activityList = new ArrayList<>();
    private Zoloz zoloz;
    public int videoPlayPagePosition = 0;
    public boolean videoRefuseNetTip = false;

//    @Inject
//    DispatchingAndroidInjector<Activity> activityInjector;

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        appInstance = this;
        String processName = CommonUtils.getProcessName(this, android.os.Process.myPid());
        Logger.init("EchoesShortVideo")
                .logLevel(LogLevel.FULL);

        //zolozInstall初始化，该初始化务必放在app启动的时候，否则影响人脸的正常使用
        Zoloz zoloz = com.alipay.zoloz.smile2pay.service.Zoloz.getInstance(getApplicationContext());
        zoloz.zolozInstall(mockInfo());

    }

//    @Override
//    public AndroidInjector<Activity> activityInjector()
//    {
//        return activityInjector;
//    }

    /**
     * 切换app的执行环境
     *
     * @param envType 1：公网正式环境 2：公网测试环境 3：内侧环境 4.昕琰ip、测试直播接口
     */
    private void switchAppEnvironment(int envType)
    {
        switch (envType)
        {
            case 1:
                Api.SERVER_SITE = "http://wp.echoesnet.com:8855";
                Api.SHARE_URL = "http://fx.echoesnet.com/share/index.html";
                break;
            case 2:
                Api.SERVER_SITE = "http://wpcs.echoesnet.com:8855";
                Api.SHARE_URL = "http://fxcs.echoesnet.com/share/index.html";
                break;
        }
    }


    public static AppController getInstance()
    {
        return appInstance;
    }

    //添加activity到容器中
    public void addActivity(Activity activity)
    {
        activityList.add(activity);
    }

    //遍历所有的Activiy并finish
    public void clearTopActivity()
    {
        for (Activity activity : activityList)
        {
            activity.finish();
        }
        activityList.clear();
    }

    public static Gson getGsonInstance()
    {
        if (gson == null)
            gson = new Gson();
        return gson;
    }
}
