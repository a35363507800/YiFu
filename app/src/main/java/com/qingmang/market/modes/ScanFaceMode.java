package com.qingmang.market.modes;

import android.arch.lifecycle.MutableLiveData;


import com.qingmang.market.Repository.ScanFaceRepository;
import com.qingmang.market.constant.Api;
import com.qingmang.market.constant.ApiException;
import com.qingmang.market.constant.SilenceSubscriber;
import com.qingmang.market.utils.ToastUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Created by Administrator on 2018/7/5.
 *
 * @author ling
 */
public class ScanFaceMode
{
    private static final String TAG = "ScanFaceMode";

    private ScanFaceRepository scanFaceRepository;

    public MutableLiveData<String> getScanFaceDataResult()
    {
        return scanFaceDataResult;
    }

    private MutableLiveData<String> scanFaceDataResult = new MutableLiveData<>();
    public ScanFaceMode()
    {
        this.scanFaceRepository = new ScanFaceRepository();
    }

    public void getScanFaceData(String cinfo)
    {
        scanFaceRepository.getScanFaceData(cinfo, Api.SCAN_FACE_INTERFACE, new SilenceSubscriber<String>()
        {
            @Override
            public void onNext(String response)
            {
                super.onNext(response);
                scanFaceDataResult.setValue(response);
            }

            @Override
            public void onHandledNetError(Throwable throwable)
            {
                super.onHandledNetError(throwable);
                ToastUtils.showLong(Api.SCAN_FACE_INTERFACE+"接口发生异常");
            }

            @Override
            public void onHandledError(ApiException apiE)
            {
                super.onHandledError(apiE);
                ToastUtils.showLong(Api.SCAN_FACE_INTERFACE+"接口发生异常");
            }
        });
    }
}
