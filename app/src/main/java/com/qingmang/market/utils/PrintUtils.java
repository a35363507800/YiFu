package com.qingmang.market.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.text.Layout;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.qingmang.market.AppController;
import com.qingmang.market.R;
import com.qingmang.market.adapter.PrintDataListAdapter;
import com.qingmang.market.bean.GoogsBean;
import com.qingmang.market.bean.PrintListBean;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/5.
 *
 * @author ling
 */
public class PrintUtils
{


//    public static void createPrintBitmap(Context mCon, ImageView imageView)
//    {
//     //  PayInfoBean bean= AppController.getGsonInstance().fromJson(printData, PayInfoBean.class);
//
//        View view = LayoutInflater.from(mCon).inflate(R.layout.print_mode, null);
//
//        ImageView imageView1=view.findViewById(R.id.qrcode);
//        TextView  name=view.findViewById(R.id.name);
//        TextView  userInfo=view.findViewById(R.id.user_info);
//        TextView  date=view.findViewById(R.id.date);
//        TextView  orderForm=view.findViewById(R.id.order_form);
//        TextView  goodsCount=view.findViewById(R.id.goods_count);
//        TextView  totalPrices=view.findViewById(R.id.total_prices);
//        TextView  prices=view.findViewById(R.id.prices);
//        TextView  prices2=view.findViewById(R.id.prices2);
//        TextView  zfbSale=view.findViewById(R.id.zfb_sale);
//        TextView  sale=view.findViewById(R.id.sale);
//        TextView  olderNumber=view.findViewById(R.id.older_number);
//        TextView  hint=view.findViewById(R.id.hint);
//        TextView  hintTime=view.findViewById(R.id.hint_time);
//        ImageView  logo=view.findViewById(R.id.logo);
//        ListView listView = view.findViewById(R.id.listView);
//
//
//        date.setText(CommonUtils.getNowDate());
//        hintTime.setText(CommonUtils.getNowDate());
//        imageView1.setImageBitmap(generateBitmap("100",8,800,300));
//
//
//        listView.setAdapter(new PrintDataListAdapter(addTest(), mCon));
//        setListViewHeightBasedOnChildren(listView);
//
//        imageView.setImageBitmap(CommonUtils.getViewBitmap(view));
//
//    }



    public static void createPrintBitmap(Context mCon, String printData,ImageView imageView)
    {
        try
        {
          //  JSONObject jsonObject=new JSONObject(printData);

                Map<String,Object> map= AppController.getGsonInstance().fromJson(printData, Map.class);

                View view = LayoutInflater.from(mCon).inflate(R.layout.print_mode, null);
                ImageView imageView1=view.findViewById(R.id.qrcode);
                TextView  name=view.findViewById(R.id.name

                );
                TextView  userInfo=view.findViewById(R.id.user_info);
                TextView  date=view.findViewById(R.id.date);
                TextView  orderForm=view.findViewById(R.id.order_form);
                TextView  goodsCount=view.findViewById(R.id.goods_count);
                TextView  totalPrices=view.findViewById(R.id.total_prices);
                TextView  prices=view.findViewById(R.id.prices);
                TextView  prices2=view.findViewById(R.id.prices2);
                TextView  zfbSale=view.findViewById(R.id.zfb_sale);
                TextView  sale=view.findViewById(R.id.sale);
                TextView  onSale=view.findViewById(R.id.on_sale);
                TextView  olderNumber=view.findViewById(R.id.older_number);
                TextView  hint=view.findViewById(R.id.hint);
                TextView  hintTime=view.findViewById(R.id.hint_time);
                ImageView  logo=view.findViewById(R.id.logo);
                ListView listView = view.findViewById(R.id.listView);

                name.setText(map.get("title")+"");
                userInfo.setText(map.get("store")+"     "+ map.get("sname")+"  ("+map.get("nick")+")");
                date.setText(map.get("paytime")+"");
                orderForm.setText(map.get("id")+"");
                hintTime.setText(map.get("paytime")+"");

                goodsCount.setText(map.get("totalNum")+"");
                prices.setText(map.get("receipt")+"");
                prices2.setText(map.get("payment")+"");
                totalPrices.setText(map.get("total")+"");
                zfbSale.setText(map.get("alipayDiscountValue")+"");
                onSale.setText(map.get("discountValue")+"");

                imageView1.setImageBitmap(generateBitmap(map.get("id")+"",8,1200,400));

                List<GoogsBean> dinerLst = AppController.getGsonInstance().fromJson(map.get("goods")+"", new TypeToken<List<GoogsBean>>(){}.getType());
                listView.setAdapter(new PrintDataListAdapter(dinerLst, mCon));
                setListViewHeightBasedOnChildren(listView);


                imageView.setImageBitmap(CommonUtils.getViewBitmap(view));


                AidlUtil.getInstance().printBitmap(CommonUtils.getViewBitmap(imageView) );
                imageView.setImageBitmap(null);

        }catch (Exception c){};

    }


    /**
     * 动态设置ListView的高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null)
            return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
// pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static List<PrintListBean> addTest()
    {
        List<PrintListBean> list = new ArrayList<>();

        PrintListBean bean = new PrintListBean();
        bean.setId("1.");
        bean.setOlderId("16732674");
        bean.setCount("x2");
        bean.setPrice("100.00");
        bean.setPrices("100.00");
        bean.setNickName("大香蕉");
        bean.setSala("优惠5折");
        bean.setSalaPrice("50.00");
        bean.setSalaPrices("50.00");

        list.add(bean);
        list.add(bean);
        list.add(bean);
        list.add(bean);
        list.add(bean);
        list.add(bean);


        return list;
    }

    /**
     * 生成条码bitmap
     * @param content
     * @param format
     * @param width
     * @param height
     * @return
     */
    public static Bitmap generateBitmap(String content, int format, int width, int height) {
        if(content == null || content.equals(""))
            return null;
        BarcodeFormat barcodeFormat;
        switch (format){
            case 0:
                barcodeFormat = BarcodeFormat.UPC_A;
                break;
            case 1:
                barcodeFormat = BarcodeFormat.UPC_E;
                break;
            case 2:
                barcodeFormat = BarcodeFormat.EAN_13;
                break;
            case 3:
                barcodeFormat = BarcodeFormat.EAN_8;
                break;
            case 4:
                barcodeFormat = BarcodeFormat.CODE_39;
                break;
            case 5:
                barcodeFormat = BarcodeFormat.ITF;
                break;
            case 6:
                barcodeFormat = BarcodeFormat.CODABAR;
                break;
            case 7:
                barcodeFormat = BarcodeFormat.CODE_93;
                break;
            case 8:
                barcodeFormat = BarcodeFormat.CODE_128;
                break;
            case 9:
                barcodeFormat = BarcodeFormat.QR_CODE;
                break;
            default:
                barcodeFormat = BarcodeFormat.QR_CODE;
                height = width;
                break;
        }
        MultiFormatWriter qrCodeWriter = new MultiFormatWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "GBK");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        try {
            BitMatrix encode = qrCodeWriter.encode(content, barcodeFormat, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        return null;
    }

}
