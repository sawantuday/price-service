package org.upstox.tickerService.model;

import com.google.gson.annotations.SerializedName;

public class Tick {

    @SerializedName("sym")
    String symbol;

    @SerializedName("P")
    double price;

    @SerializedName("Q")
    double quantity;

    @SerializedName("TS2")
    long timestamp;

//    @SerializedName("T")
//    String trade;
//    @SerializedName("side")
//    String side;
//    @SerializedName("TS")
//    double unixTime;


    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public double getQuantity() {
        return quantity;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString(){
        return String.format(
            "s:%s, p:%f, q:%f, ts:%d",
            symbol, price, quantity, timestamp
        );
    }
}