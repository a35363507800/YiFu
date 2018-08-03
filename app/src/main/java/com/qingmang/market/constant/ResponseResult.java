package com.qingmang.market.constant;

import org.json.JSONObject;
import org.json.JSONStringer;

/**
 * Created by liuyang on 2016/11/10.
 */

public class ResponseResult
{
/*"{    "body":"{\"face\":\"988608\",\"balance\":\"6216.00\",\"meal\":\"185016\"}",
        "code":"0",
        "status":"0"}"*/

    public int getErrno()
    {
        return errno;
    }

    public void setErrno(int errno)
    {
        this.errno = errno;
    }

    public String getErrmsg()
    {
        return errmsg;
    }

    public Object getData()
    {
        return data;
    }

    public void setData(Object data)
    {
        this.data = data;
    }

    public void setErrmsg(String errmsg)
    {
        this.errmsg = errmsg;
    }





    private int errno;
    private String errmsg;
    private Object data;



    @Override
    public String toString()
    {
        return "ResponseResult{" +
                "errno='" + errno + '\'' +
                ", errmsg='" + errmsg + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
