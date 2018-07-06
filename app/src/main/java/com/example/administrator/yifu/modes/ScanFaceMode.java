package com.example.administrator.yifu.modes;

import android.arch.lifecycle.MutableLiveData;

import com.example.administrator.yifu.Repository.ScanFaceRepository;
import com.example.administrator.yifu.constant.Api;
import com.example.administrator.yifu.constant.SilenceSubscriber;

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

    public void getScanFaceData()
    {
        scanFaceRepository.getScanFaceData(Api.SCAN_FACE_INTERFACE, new SilenceSubscriber<String>()
        {
            @Override
            public void onNext(String response)
            {
                super.onNext(response);
                scanFaceDataResult.setValue(response);
            }
        });
    }
}
