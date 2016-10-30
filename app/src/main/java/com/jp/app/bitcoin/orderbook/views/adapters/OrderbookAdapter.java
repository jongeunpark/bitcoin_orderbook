package com.jp.app.bitcoin.orderbook.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jp.app.bitcoin.orderbook.R;

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
            askQty.setText(orderItem.getQty().toPlainString());
            price.setTextColor(mContext.getResources().getColor(R.color.blue_background));
        }else{

            bidQty.setText(orderItem.getQty().toPlainString());
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
