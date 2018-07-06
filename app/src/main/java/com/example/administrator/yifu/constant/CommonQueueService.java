package com.example.administrator.yifu.constant;

import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface CommonQueueService
{
    //@Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("")
    Observable<ResponseResult> postRxBody(@Url String interfaceName, @Body Map<String, Object> reqParamMap);

}
