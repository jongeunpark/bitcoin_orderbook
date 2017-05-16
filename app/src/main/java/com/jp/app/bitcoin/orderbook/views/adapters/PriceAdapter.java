package com.jp.app.bitcoin.orderbook.views.adapters;

import android.content.Context;
import android.icu.math.BigDecimal;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jp.app.bitcoin.orderbook.R;

import java.util.List;

import jp.com.lib.orderbook.network.datas.LastPrice;

/**
 * Created by jp on 16. 10. 14..
 */
public class PriceAdapter extends ArrayAdapter<LastPrice> {
    private Context mContext;
    private List<LastPrice> mValues;
    private int mResourceId;

    public PriceAdapter(Context context, int resource, List<LastPrice> values) {
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


        TextView textPrice = (TextView) rowView.findViewById(R.id.price_text_price);
        TextView textCode = (TextView) rowView.findViewById(R.id.price_text_code);
        TextView textCurrency = (TextView) rowView.findViewById(R.id.price_text_currency);
        LastPrice lastPrice = getItem(position);
        if(position%2==1){
            rowView.setBackgroundResource(R.color.row_second);
            textCode.setTextColor(mContext.getResources().getColor(R.color.red_background));
        }



        String codeName = "";
        String currency = "";
        if(lastPrice.getCode() == LastPrice.CODE_POLONIEX){
            codeName = mContext.getString(R.string.code_poloniex);
        }else if(lastPrice.getCode() == LastPrice.CODE_BTC38){
            codeName = mContext.getString(R.string.code_btc38);
        }else if(lastPrice.getCode() == LastPrice.CODE_KRAKEN){
            codeName = mContext.getString(R.string.code_kraken);
        }
        if(lastPrice.getPrice().setScale(2, BigDecimal.ROUND_DOWN).doubleValue() > 0.0) {

            if (lastPrice.getCurrency() == LastPrice.CURRENCY_CNY) {
                currency = mContext.getString(R.string.currency_cny);
            } else if (lastPrice.getCurrency() == LastPrice.CURRENCY_JPY) {
                currency = mContext.getString(R.string.currency_jpy);
            } else if (lastPrice.getCurrency() == LastPrice.CURRENCY_EUR) {
                currency = mContext.getString(R.string.currency_eur);
            } else if (lastPrice.getCurrency() == LastPrice.CURRENCY_USD) {
                currency = mContext.getString(R.string.currency_usd);
            }


            textPrice.setText(String.format("%,.2f", lastPrice.getPrice().setScale(2, BigDecimal.ROUND_DOWN).doubleValue()));
            textCurrency.setText(currency);
        }else{
            textCurrency.setText(mContext.getString(R.string.error_price));
        }
        textCode.setText(codeName);



        return rowView;
    }
    @Override
    public int getCount() {

        return mValues.size();
    }

    @Override
    public LastPrice getItem(int positon) {

        return mValues.get(positon);
    }

}
