package com.qingmang.market.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingmang.market.R;
import com.qingmang.market.bean.GoogsBean;


import java.util.List;

/**
 * Created by Administrator on 2018/7/6.
 *
 * @author ling
 */
public class PrintDataListAdapter extends BaseAdapter
{

    private List<GoogsBean> list;

    private Context mCon;

    public PrintDataListAdapter(List<GoogsBean> list, Context mCon)
    {
        this.list = list;
        this.mCon = mCon;
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public Object getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Holder holder=null;
      if(convertView==null)
      {
          convertView= LayoutInflater.from(mCon).inflate(R.layout.print_mode_list_adp,null);
           holder=new Holder(convertView.findViewById(R.id.number)
                  ,convertView.findViewById(R.id.older)
                  ,convertView.findViewById(R.id.count)
                  ,convertView.findViewById(R.id.name)
          ,convertView.findViewById(R.id.price1)
          ,convertView.findViewById(R.id.prices1)
          ,convertView.findViewById(R.id.sale)
          ,convertView.findViewById(R.id.price2)
          ,convertView.findViewById(R.id.prices2)
          ,convertView.findViewById(R.id.sale_layout)
          );
          convertView.setTag(holder);
      }else
          {
               holder= (Holder) convertView.getTag();
          }

        holder.textView1.setText(position+1+".");
        holder.textView2.setText(list.get(position).getIndex()+"");
        holder.textView3.setText("x"+list.get(position).getCount()+"");
        holder.textView4.setText(list.get(position).getName()+"");
        holder.textView5.setText(list.get(position).getPrice()+"");
        holder.textView6.setText(list.get(position).getPrices()+"");

//        if(TextUtils.isEmpty(list.get(position).getSala()))
//        {
       //     holder.relativeLayout.setVisibility(View.GONE);
//        }
//        else
//            {
//                holder.relativeLayout.setVisibility(View.VISIBLE);
//                holder.textView7.setText(list.get(position).getSala());
//                holder.textView8.setText(list.get(position).getSalaPrice());
//                holder.textView9.setText(list.get(position).getSalaPrices());
//            }

        return convertView;
    }


    private  class Holder
    {
         TextView textView1;
         TextView textView2;
         TextView textView3;
         TextView textView4;
         TextView textView5;
         TextView textView6;
         TextView textView7;
         TextView textView8;
         RelativeLayout relativeLayout;

        public Holder(TextView textView1, TextView textView2, TextView textView3, TextView textView4, TextView textView5, TextView textView6, TextView textView7, TextView textView8, TextView textView9,RelativeLayout relativeLayout)
        {
            this.textView1 = textView1;
            this.textView2 = textView2;
            this.textView3 = textView3;
            this.textView4 = textView4;
            this.textView5 = textView5;
            this.textView6 = textView6;
            this.textView7 = textView7;
            this.textView8 = textView8;
            this.textView9 = textView9;
            this.textView9 = textView9;
            this.relativeLayout = relativeLayout;
        }

        TextView textView9;


    }
}
