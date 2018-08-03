package com.qingmang.market.bean;

/**
 * Created by Administrator on 2018/7/30.
 *
 * @author ling
 */
public class PayInfoBean
{

    /**
     * statusCode : 0-0
     * isPrintSuccess : false
     * buttonTimer : 30
     * isFacePay : false
     * store : 1300
     * channel : 1300
     * cashier : 1300
     * id : 1807301320220009704
     * newtime : 2018-07-30 13:20:22
     * status : SUCCESS
     * title : 青芒测试C
     * total : 1
     * goods : {"details":[{"user":"1300","index":"6920202888883","name":"\u7ea2\u725b\u542c\u88c5250\u6beb\u5347","class":"\u8fd0\u52a8\u996e\u54c1","image":null,"utime":"2018-07-25 00:00:00","client":"0","price":"5.9","body":null,"attach":null,"count":1}]}
     * method : SCAN
     * type : ALIPAY
     * bank : NONE
     * receipt : 1
     * payment : 1
     * fee : 0
     * innerid : 2018073021001004440580230343
     * paytime : 2018-07-30 13:20:23
     * buyid : 2088112010229448
     * buyer : 133***@qq.com
     */

    private String statusCode;
    private boolean isPrintSuccess;
    private int buttonTimer;
    private boolean isFacePay;
    private String store;
    private String channel;
    private String cashier;
    private String id;
    private String newtime;
    private String status;
    private String title;
    private int total;
    private String goods;
    private String method;
    private String type;
    private String bank;
    private int receipt;
    private int payment;
    private int fee;
    private String innerid;
    private String paytime;
    private String buyid;
    private String buyer;

    public String getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(String statusCode)
    {
        this.statusCode = statusCode;
    }

    public boolean isIsPrintSuccess()
    {
        return isPrintSuccess;
    }

    public void setIsPrintSuccess(boolean isPrintSuccess)
    {
        this.isPrintSuccess = isPrintSuccess;
    }

    public int getButtonTimer()
    {
        return buttonTimer;
    }

    public void setButtonTimer(int buttonTimer)
    {
        this.buttonTimer = buttonTimer;
    }

    public boolean isIsFacePay()
    {
        return isFacePay;
    }

    public void setIsFacePay(boolean isFacePay)
    {
        this.isFacePay = isFacePay;
    }

    public String getStore()
    {
        return store;
    }

    public void setStore(String store)
    {
        this.store = store;
    }

    public String getChannel()
    {
        return channel;
    }

    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    public String getCashier()
    {
        return cashier;
    }

    public void setCashier(String cashier)
    {
        this.cashier = cashier;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getNewtime()
    {
        return newtime;
    }

    public void setNewtime(String newtime)
    {
        this.newtime = newtime;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public int getTotal()
    {
        return total;
    }

    public void setTotal(int total)
    {
        this.total = total;
    }

    public String getGoods()
    {
        return goods;
    }

    public void setGoods(String goods)
    {
        this.goods = goods;
    }

    public String getMethod()
    {
        return method;
    }

    public void setMethod(String method)
    {
        this.method = method;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getBank()
    {
        return bank;
    }

    public void setBank(String bank)
    {
        this.bank = bank;
    }

    public int getReceipt()
    {
        return receipt;
    }

    public void setReceipt(int receipt)
    {
        this.receipt = receipt;
    }

    public int getPayment()
    {
        return payment;
    }

    public void setPayment(int payment)
    {
        this.payment = payment;
    }

    public int getFee()
    {
        return fee;
    }

    public void setFee(int fee)
    {
        this.fee = fee;
    }

    public String getInnerid()
    {
        return innerid;
    }

    public void setInnerid(String innerid)
    {
        this.innerid = innerid;
    }

    public String getPaytime()
    {
        return paytime;
    }

    public void setPaytime(String paytime)
    {
        this.paytime = paytime;
    }

    public String getBuyid()
    {
        return buyid;
    }

    public void setBuyid(String buyid)
    {
        this.buyid = buyid;
    }

    public String getBuyer()
    {
        return buyer;
    }

    public void setBuyer(String buyer)
    {
        this.buyer = buyer;
    }

}
