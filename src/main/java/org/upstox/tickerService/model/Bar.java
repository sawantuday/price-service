package org.upstox.tickerService.model;

import com.google.gson.annotations.SerializedName;

public class Bar {
    @SerializedName("o")
    double open;

    @SerializedName("h")
    double high;

    @SerializedName("l")
    double low;

    @SerializedName("c")
    double close;

    @SerializedName("volume")
    double volume;

    @SerializedName("symbol")
    String symbol;

    @SerializedName("bar_num")
    int id;

    String event = "ohlc_notify";

    transient boolean isClosed;
    transient double ltp;

    public Bar(int barId, String symbol){
        this.id = barId;
        this.symbol = symbol;
    }

    public Bar(Bar bar){    // deep copy constructor
        this.id = bar.id;
        this.open = bar.open;
        this.close = bar.close;
        this.low = bar.low;
        this.high = bar.high;
        this.isClosed = bar.isClosed;
        this.volume = bar.volume;
        this.symbol = bar.symbol;
        this.event = bar.event;
    }

    public void addTick(Tick tick){
        this.ltp = tick.price;
        this.volume += tick.quantity;
        if(tick.price < low || this.low == 0){
            this.low = tick.price;
        }
        if (tick.price > high){
            this.high = tick.price;
        }
        if (this.open == 0){    // to cover bars created without tick
            this.open = tick.price;
        }
    }

    public int getId() {
        return id;
    }

    public double getOpen() {
        return open;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getClose() {
        return close;
    }

    public double getVolume() {
        return volume;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(){
        this.isClosed = true;
        this.close = this.ltp;
        this.event = "bar_closed";
    }

    public String toString(){
        return String.format(
            "s:%s, o:%f, h:%f, l:%f, c:%f, v:%f, closed:%b",
            symbol, open, high, low, close, volume, isClosed
        );
    }
}
