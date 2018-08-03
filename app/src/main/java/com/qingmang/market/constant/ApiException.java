package com.qingmang.market.constant;

/**
 * 添加了 Object errorBody 来应对「status 1，code "key"，errorBody "string"」 body依然携带数据的问题<br>
 * 是T范型传入的，取值时候需要注意
 */
public class ApiException extends RuntimeException
{
    private String errorCode;
    private String errorBody;

    public ApiException(String detailMessage)
    {
        this(detailMessage,"{}");
    }

    public ApiException(String detailMessage, String errorBody)
    {
        super(detailMessage);
        this.errorBody = errorBody;
        this.errorCode=detailMessage;
    }

    public String getErrorCode()
    {
        return errorCode;
    }

    public String getErrBody()
    {
        return errorBody;
    }
}

