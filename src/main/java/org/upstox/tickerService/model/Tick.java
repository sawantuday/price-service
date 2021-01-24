package org.upstox.tickerService.model;

import com.google.gson.annotations.SerializedName;

public class Tick {

    @SerializedName("sym")
    String symbol;

    @SerializedName("P")
    double price;

    @SerializedName("Q")
    double qty;

    @SerializedName("TS2")
    long timestamp;

//    String trade;
//    String side;
//    double unixTime;

    @Override
    public String toString(){
        return String.format("s:%s, p:%f, q:%f", symbol, price, qty);
    }
}