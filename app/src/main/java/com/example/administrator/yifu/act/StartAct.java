package com.example.administrator.yifu.act;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.administrator.yifu.R;

/**
 * Created by Administrator on 2018/6/29.
 *
 * @author ling
 */
public class StartAct extends BaseActivity
{

    @Override
    protected void onCreate(Bundle arg0)
    {
        super.onCreate(arg0);
        setContentView(R.layout.activity_start);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
               startActivity(new Intent(StartAct.this,MainActivity.class));
            }
        },1500);
    }
}
