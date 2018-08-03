package com.qingmang.market.bean;

/**
 * Created by Administrator on 2018/7/6.
 *
 * @author ling
 */
public class PrintListBean
{
    //序列号
    private String id;
    //商品编号
    private String olderId;
    //商品数量
    private String count;
    //商品单价
    private String price;
    //商品总价
    private String prices;
    //商品名称
    private String nickName;
    //活动
    private String sala;
    //活动优惠单价
    private String salaPrice;
    //活动总优惠
    private String salaPrices;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getOlderId()
    {
        return olderId;
    }

    public void setOlderId(String olderId)
    {
        this.olderId = olderId;
    }

    public String getCount()
    {
        return count;
    }

    public void setCount(String count)
    {
        this.count = count;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getPrices()
    {
        return prices;
    }

    public void setPrices(String prices)
    {
        this.prices = prices;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getSala()
    {
        return sala;
    }

    public void setSala(String sala)
    {
        this.sala = sala;
    }

    public String getSalaPrice()
    {
        return salaPrice;
    }

    public void setSalaPrice(String salaPrice)
    {
        this.salaPrice = salaPrice;
    }

    public String getSalaPrices()
    {
        return salaPrices;
    }

    public void setSalaPrices(String salaPrices)
    {
        this.salaPrices = salaPrices;
    }

    @Override
    public String toString()
    {
        return "PrintListBean{" +
                "id='" + id + '\'' +
                ", olderId='" + olderId + '\'' +
                ", count='" + count + '\'' +
                ", price='" + price + '\'' +
                ", prices='" + prices + '\'' +
                ", nickName='" + nickName + '\'' +
                ", sala='" + sala + '\'' +
                ", salaPrice='" + salaPrice + '\'' +
                ", salaPrices='" + salaPrices + '\'' +
                '}';
    }
}
