/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qingmang.market.act;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;


import com.orhanobut.logger.Logger;
import com.qingmang.market.utils.CommonUtils;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;

import java.util.Arrays;
import java.util.List;

public class BaseActivity extends RxFragmentActivity
{
    private final static String TAG = BaseActivity.class.getSimpleName();

    protected InputMethodManager inputMethodManager;

    //不做统计的页面列表
    private List<String> notStatPages =
            Arrays.asList(new String[]{"WelcomeAct", "HomeAct", "DOrderMealDetailAct", "MyDateAct", "MyOrdersAct", "MyLevelAct", "TaskAct"});

    @Override
    protected void onCreate(Bundle arg0)
    {
        CommonUtils.setTransparentTopBar(this);
        super.onCreate(arg0);
        //状态栏黑字体
        CommonUtils.setStatusBarDarkMode(this, true);
        Logger.t(TAG).d("chat------>BaseActivity onCreate:" + this.getClass().getSimpleName());
        //http://stackoverflow.com/questions/4341600/how-to-prevent-multiple-instances-of-an-activity-when-it-is-launched-with-differ/
        // should be in launcher activity, but all app use this can avoid the problem
        //保证根activity只启动一个
        if (!isTaskRoot())
        {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN))
            {
                finish();
                return;
            }
        }
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Logger.t(TAG).d("chat------>BaseActivity onStart:" + this.getClass().getSimpleName());
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        try
        {
            Logger.t(TAG).d("chat------>BaseActivity onResume:" + this.getClass().getSimpleName());
//            // baidu统计
//            if (!notStatPages.contains(this.getClass().getSimpleName()))
//            {
//                StatService.onResume(this);
//            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Logger.t(TAG).d("chat------>BaseActivity onPause:" + this.getClass().getSimpleName());
//        HuanXinIMHelper.getInstance().dissmissGameInviteDialog();
//        if (!notStatPages.contains(this.getClass().getSimpleName()))
//        {
//            StatService.onPause(this);
//        }
//        //int count = ((EamApplication) getApplication()).getBadgerCount();
//        ((EamApplication) getApplication()).notifiBadgerCount();
//        //onpause时关闭游戏邀请弹窗
    }


    @Override
    protected void onStop()
    {
        super.onStop();
        Logger.t(TAG).d("chat------>BaseActivity onStop:" + this.getClass().getSimpleName());
        if (!CommonUtils.isAppOnForeground(this))
        {
            Logger.t(TAG).d("chat------>已经切后台");
        }
        else
        {
            Logger.t(TAG).d("chat------>没有切后台");
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Logger.t(TAG).d("chat------>BaseActivity onDestroy:" + this.getClass().getSimpleName());
    }

    @Override
    public void finish()
    {
        Logger.t(TAG).d("chat------>BaseActivity finish:" + this.getClass().getSimpleName());
        super.finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }

    @Override
    public Resources getResources()
    {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
        Logger.t(TAG).d("基类内存裁剪触发》" + level);
        switch (level)
        {
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:
                //ToastUtils.showShort(this,"设备运行内存太低，后台服务可能受限");
                Logger.t(TAG).d("设备运行内存太低，后台服务可能受限");
                //this.finish();
                break;
            default:
                /*
                  Release any non-critical data structures.
                  The app received an unrecognized memory level value
                  from the system. Treat this as a generic low-memory message.
                */
                break;
        }
    }


}
