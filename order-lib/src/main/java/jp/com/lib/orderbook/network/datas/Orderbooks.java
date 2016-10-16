package jp.com.lib.orderbook.network.datas;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by jp on 16. 10. 12..
 */
public class Orderbooks {
    private List<Orderbook> bidArray;
    private List<Orderbook> askArray;
    private Date responseTime;

    public Orderbooks() {
        setResponseTime(new Date());
        setBidArray(new ArrayList<Orderbook>());
        setAskArray(new ArrayList<Orderbook>());
    }

    public void addBid(Orderbook orderbook) {
        bidArray.add(orderbook);
    }

    public void addAsk(Orderbook orderbook) {
        askArray.add(orderbook);
    }


    public Date getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    public List<Orderbook> getBidArray() {
        return bidArray;
    }

    public void setBidArray(List<Orderbook> bidArray) {
        this.bidArray = bidArray;
    }

    public List<Orderbook> getAskArray() {

        return askArray;
    }

    public void setAskArray(List<Orderbook> askArray) {
        this.askArray = askArray;
    }
    public void sort(){

        Collections.sort(getAskArray(), new Comparator<Orderbook>() {
            @Override
            public int compare(Orderbook t1, Orderbook t2) {

                return t1.getPrice().compareTo(t2.getPrice());
            }
        });


        Collections.sort(getBidArray(), new Comparator<Orderbook>() {
            @Override
            public int compare(Orderbook t1, Orderbook t2) {
                return t2.getPrice().compareTo(t1.getPrice());
            }
        });
    }

}
