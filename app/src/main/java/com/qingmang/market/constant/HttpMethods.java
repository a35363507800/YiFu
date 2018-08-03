package com.qingmang.market.constant;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Refactor by ling
 */
public class HttpMethods
{
    private static final String TAG = HttpMethods.class.getSimpleName();
    private static final int DEFAULT_TIMEOUT = 20;

    private Retrofit retrofit;
    private Retrofit specialRetrofit;
    private OkHttpClient okHttpClient;
    //private Gson g = new Gson();

    //构造方法私有
    private HttpMethods()
    {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.addInterceptor(logging);
        okHttpClient = builder.build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))  //gson 转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  // Rx 工厂。
                .baseUrl(Api.SERVER_SITE)
                .build();
    }

    //获取单例
    public static HttpMethods getInstance()
    {
        return SingletonHolder.INSTANCE;
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder
    {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }



    /**
     * 处理网络请求结果，返回的是后台接口的body里面的字符串
     * @param subscriber
     * @param interfaceName
     * @param reqParamMap
     * @param isObserveMainThread
     */
    public void startServerRequest(Observer<String> subscriber, String interfaceName, Map<String, Object> reqParamMap, boolean isObserveMainThread)
    {
        CommonQueueService service = retrofit.create(CommonQueueService.class);
        Logger.t(TAG).d(String.format("接口请求数据：%s  %s ",interfaceName, new Gson().toJson(reqParamMap)));
        Observable<String> observable = service.postRxBody(interfaceName,reqParamMap)
                .map(new EAMHttpResultFunc());
        toSubscribe(observable, subscriber, isObserveMainThread);
    }

    /**
     * 处理网络请求结果，将结果转换成的类型交给使用者处理
     * 此方法的优秀之处在于将数据处理完全放在了工作线程，转换成用户的目标类型后才切换到UI线程
     * @param subscriber
     * @param mapper
     * @param interfaceName
     * @param reqParamMap
     * @param <T>
     */
    public <T>void startServerRequest(Observer<T> subscriber, Function<String, T> mapper, String interfaceName, Map<String, Object> reqParamMap)
    {
        CommonQueueService service = retrofit.create(CommonQueueService.class);
        Logger.t(TAG).d(String.format("接口请求数据：%s  %s ",interfaceName, new Gson().toJson(reqParamMap)));
        Observable<T> observable = service.postRxBody(interfaceName,reqParamMap)
                .map(new EAMHttpResultFunc()).map(mapper);
        toSubscribe(observable, subscriber, true);
    }
    /**
     * 向服务器发起请求
     *
     * @param subscriber
     * @param baseUrl       服务器url 不传默认走客户端服务器
     * @param interfaceName 接口名称.
     * @param reqParamMap   请求参数
     */
    public void startServerRequest4Server(Observer<String> subscriber, String baseUrl, String interfaceName, Map<String, Object> reqParamMap, boolean isObserveMainThread)
    {
        if (!TextUtils.isEmpty(baseUrl))
        {
            Logger.t(TAG).d("baseUrl>" + baseUrl);
            if (specialRetrofit == null || !baseUrl.equals(specialRetrofit.baseUrl().toString()))
            {
                specialRetrofit = new Retrofit.Builder()
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create(new Gson()))  //gson 转换器
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  // Rx 工厂。
                        .baseUrl(baseUrl)
                        .build();
            }
        }
        else
        {
            if (specialRetrofit == null || !TextUtils.equals(Api.SERVER_SITE, specialRetrofit.baseUrl().toString()))
            {
                Logger.t(TAG).d("baseUrl>" + Api.SERVER_SITE);
                specialRetrofit = new Retrofit.Builder()
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create(new Gson()))  //gson 转换器
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  // Rx 工厂。
                        .baseUrl(Api.SERVER_SITE)
                        .build();
            }
        }
        CommonQueueService service = specialRetrofit.create(CommonQueueService.class);
        Observable<String> observable = service.postRxBody(interfaceName, reqParamMap)
                .map(new EAMHttpResultFunc());
        toSubscribe(observable, subscriber, isObserveMainThread);
    }


/*    public Flowable<ResponseResult> getApiService(String interfaceName, String isSync, Map<String, String> reqParamMap)
    {
        CommonQueueService service = retrofit.create(CommonQueueService.class);
        return service.postRx2String(createReqForm(interfaceName, isSync, reqParamMap))
                .map(new ResponseResultMapper());
    }*/

    //观察者启动器
    private <T> void toSubscribe(Observable<T> o, Observer<T> s, boolean isMainThread)
    {
        Scheduler observeScheduler = Schedulers.io();
        if (isMainThread)
            observeScheduler = AndroidSchedulers.mainThread();
        o.subscribeOn(Schedulers.io()) //绑定在io
                .observeOn(observeScheduler) //返回 内容 在Android 主线程
                .subscribe(s);  //放入观察者
    }

    /**
     * 组装消息体
     */
    private Map<String, Object> createReqBody(String interfaceName, Map<String, String> params)
    {
        Map<String, Object> m = new HashMap<>();
        Map<String, Object> clsm = new HashMap<>();
        clsm.put("reqName", interfaceName);
        m.put("head", clsm);
        m.put("body", params);
        Logger.t(TAG).d("接口请求数据：" + new Gson().toJson(m));
        return m;
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     */
    private class EAMHttpResultFunc implements Function<ResponseResult, String>
    {
        @Override
        public String apply(@NonNull ResponseResult httpResult) throws Exception
        {
            if (httpResult == null)
            {
                throw new NullPointerException("|返回结果为null|");
            }
            Logger.t(TAG).d("服务器返回结果" + httpResult.toString());
//            if ("1".equals(httpResult.getStatus()))
//            {
//                String bodyStr = httpResult.getBody();
//                String codeStr = httpResult.getCode();
//                throw new ApiException(codeStr == null ? "" : codeStr, bodyStr == null ? "" : bodyStr);
//            }
          return TextUtils.isEmpty(httpResult.getData().toString()) ? "{}" : httpResult.getData().toString();

        }
    }

    /**
     * 将后台返回的结果转化为需要的类型：ResponseResult
     */
/*    private class ResponseResultMapper implements Function<ResponseResultSkeleton, ResponseResult>
    {
        @Override
        public ResponseResult apply(ResponseResultSkeleton httpResult) throws Exception
        {
            *//*{
                "message":"non-standard access",
                    "messageJson":"{"RESPONSE_IDENTITY":"10.19.175.167-echoServer-98321490839655921",
                    "body":"{\"face\":\"988608\",\"balance\":\"6216.00\",\"meal\":\"185016\"}","code":"0","status":"0"}",
                "status":"0"
            }*//*
            //Logger 的最大字数为4000
            Logger.t(TAG).d("|中间件返回结果|》 " + new Gson().toJson(httpResult, ResponseResultSkeleton.class) + "\n\n");
            if (httpResult == null)
            {
                throw new NullPointerException("|中间件返回结果错误|》" + new Gson().toJson(httpResult, ResponseResultSkeleton.class));
            }

            String messageJson = httpResult.getMessageJson();
            ResponseResult resultBean = g.fromJson(messageJson, ResponseResult.class);
            if (resultBean == null)
            {
                throw new NullPointerException("|解析返回结果错误|");
            }

            //将服务器错误抛出
            if (!"0".equals(resultBean.getStatus()))
            {
                throw new ApiException(resultBean.getCode(), resultBean.getBody());
            }
            return resultBean;
        }
    }*/


}
