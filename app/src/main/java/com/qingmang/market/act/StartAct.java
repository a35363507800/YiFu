package com.qingmang.market.act;


import android.os.Bundle;


import com.qingmang.market.R;



/**
 * Created by Administrator on 2018/6/29.
 *
 * @author ling
 */
public class StartAct extends BaseActivity
{
    private static final String TAG = "StartAct";
    @Override
    protected void onCreate(Bundle arg0)
    {
        super.onCreate(arg0);
        setContentView(R.layout.activity_start);


      //  ((ImageView)findViewById(R.id.image)).setImageBitmap(null);
//        new Handler().postDelayed(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//               startActivity(new Intent(StartAct.this,MainActivity.class));
//            }
//        },1500);
    }



}
