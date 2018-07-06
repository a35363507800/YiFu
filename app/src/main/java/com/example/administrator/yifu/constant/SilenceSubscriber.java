package com.example.administrator.yifu.constant;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Created by ben on 2017/3/30.
 */

public class SilenceSubscriber<T> implements Observer<T>
{
    private final static String TAG = SilenceSubscriber.class.getSimpleName();

    @Override
    public void onComplete()
    {

    }

    @Override
    public void onSubscribe(Disposable d)
    {

    }

    @Override
    public void onError(Throwable e)
    {
        try
        {
            if (e instanceof SocketTimeoutException)
            {
                //EamLogger.writeToDefaultFile("SocketTimeoutException 网络中断，请检查您的网络状态>" + e.getMessage());
                Logger.t(TAG).d("SocketTimeoutException 网络中断，请检查您的网络状态>" + e.getMessage());
                onHandledNetError(e);
                e.printStackTrace();
            }
            else if (e instanceof SocketException)
            {
                //EamLogger.writeToDefaultFile("SocketException 网络中断，请检查您的网络状态>" + e.getMessage());
                Logger.t(TAG).d("SocketException 网络中断，请检查您的网络状态>" + e.getMessage());
                onHandledNetError(e);
                e.printStackTrace();
            }
            else
            {
                e.printStackTrace();
                onHandledNetError(e);
                //EamLogger.writeToDefaultFile("网络请求发生了没有处理异常》" + e==null?"exception is null ": e.getMessage());
                Logger.t(TAG).d("网络请求发生了没有处理异常 网络中断，请检查您的网络状态>" + e.getMessage());
            }
        } catch (Exception e1)
        {
            e1.printStackTrace();
            //region 这是为了调试，这是一个坏的编程习惯，请在catch中做尽量少的工作，而且只做恢复和记录的工作--wb
            //EamLogger.t(TAG).t("错误处理出现了问题，请开发人员查询SilenceSubscriber2》"+e1.getMessage());
            //endregion
        }
    }

    //要处理特殊的错误码，重写这个函数，需要展示toast的调用super，不需要就不调用--wb
    //1：如果要特殊处理“GAME_OVER”,而且不希望弹出toast,那么就重写此函数，且不调用super。
    //2：如果不需要特殊处理“GAME_OVER”,只是想弹出“游戏结束”的toast，那么把“GAME_OVER”放入码表里面解析成对应的文案就好了。不要重写此函数！
    //3: 如果要将后台返回的提示直接显示成toast，不做任何处理，不要重写此函数。
    public void onHandledError(ApiException apiE)
    {
        Logger.t(TAG).d("网络请求onHandledError触发,下面显示异常结果:》"+apiE.getErrorCode()+" "+apiE.getErrBody());
//        String errMsg= ErrorCodeTable.parseErrorCode(apiE.getErrorCode());//码表里只存放不需要特殊处理只需要显示toast的错误码。
//        if (!TextUtils.isEmpty(errMsg))
//            ToastUtils.showShort(errMsg);
    }

    public void  onHandledNetError(Throwable throwable)
    {
        Logger.t(TAG).d("onHandledNetError》"+ (throwable==null?"null":throwable.getMessage()));
    }
    @Override
    public void onNext(T response)
    {
        Logger.t(TAG).d("onNext》"+response);
    }
}
