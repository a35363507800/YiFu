package com.qingmang.market.bean;

import com.google.gson.annotations.SerializedName;

public class GoogsBean {

//     * goods : {"details":[{"user":"1300","index":"6920202888883","name":"\u7ea2\u725b\u542c\u88c5250\u6beb\u5347",
//            "class":"\u8fd0\u52a8\u996e\u54c1",
//            "image":null,
//            "utime":"2018-07-25 00:00:00","client":"0","price":"5.9","body":null,"attach":null,"count":1}]}
//     *\

    private String user;
    private String index;
    private String name;
    private String image;
    private String utime;
    private String client;
    private String body;
    private String attach;
    private String count;
    private String price;
    private String prices;
    @SerializedName("class")
    private String test;

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUtime() {
        return utime;
    }

    public void setUtime(String utime) {
        this.utime = utime;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "GoogsBean{" +
                "user='" + user + '\'' +
                ", index='" + index + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", utime='" + utime + '\'' +
                ", client='" + client + '\'' +
                ", body='" + body + '\'' +
                ", attach='" + attach + '\'' +
                ", count='" + count + '\'' +
                '}';
    }
}
