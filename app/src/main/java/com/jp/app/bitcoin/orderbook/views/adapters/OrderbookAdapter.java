package com.jp.app.bitcoin.orderbook.views.adapters;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jp.app.bitcoin.orderbook.R;

import java.math.BigDecimal;
import java.util.List;

import com.jp.app.bitcoin.orderbook.data.OrderItem;

/**
 * Created by jp on 16. 10. 14..
 */
public class OrderbookAdapter extends ArrayAdapter<OrderItem> {
    private Context mContext;
    private List<OrderItem> mValues;
    private int mResourceId;

    public OrderbookAdapter(Context context, int resource, List<OrderItem> values) {
        super(context, resource, values);
        this.mContext = context;
        this.mValues = values;
        this.mResourceId = resource;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(mResourceId, parent, false);
        if(position%2==1){
            rowView.setBackgroundResource(R.color.row_second);
        }
        TextView askQty = (TextView) rowView.findViewById(R.id.order_row_text_ask_qty);
        TextView price= (TextView) rowView.findViewById(R.id.order_row_text_price);
        TextView bidQty= (TextView) rowView.findViewById(R.id.order_row_text_bid_qty);

        OrderItem orderItem = getItem(position);
        if(orderItem.getOrder_type() == OrderItem.ORDER_TYPE.ASK){
            String text = orderItem.getQty().setScale(4, BigDecimal.ROUND_DOWN).toPlainString();
            final SpannableStringBuilder sp = new SpannableStringBuilder(text);
            int start = text.indexOf(".");

            if(start >= 1){
                sp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.blue_background)), 0
                        , start-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                sp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.blue_background_light)), start+1
                        , text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                sp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.icons)), start
                        , start+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }else{
                sp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.blue_background)), 0
                        , text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            askQty.setText(sp);

            price.setTextColor(mContext.getResources().getColor(R.color.blue_background));
        }else{
            String text = orderItem.getQty().setScale(4, BigDecimal.ROUND_DOWN).toPlainString();
            final SpannableStringBuilder sp = new SpannableStringBuilder(text);
            int start = text.indexOf(".");

            if(start >= 1){
                sp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.red_background)), 0
                        , start-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                sp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.red_background_light)), start+1
                        , text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                sp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.icons)), start
                        , start+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }else{
                sp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.blue_background)), 0
                        , text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            bidQty.setText(sp);
            price.setTextColor(mContext.getResources().getColor(R.color.red_background));

        }
        price.setText(String.format("%,d", orderItem.getPrice().intValue()));




        return rowView;
    }
    @Override
    public int getCount() {

        return mValues.size();
    }

    @Override
    public OrderItem getItem(int positon) {

        return mValues.get(positon);
    }

}
