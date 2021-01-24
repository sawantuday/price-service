package org.upstox.tickerService.model;

public class Bar {
    double open;
    double high;
    double low;
    double close;
    long volume;
    String symbol;

    int id;
    int barLength = 15;
    boolean isClosed;
    long openTime;

    public Bar(int barId, Tick tick){
        this.id = barId;
        this.openTime = tick.timestamp;
        this.close = tick.price;
        this.open = tick.price;
        this.high = tick.price;
        this.low = tick.price;
        this.volume += tick.quantity;
        this.symbol = tick.symbol;
    }

    public boolean addTick(Tick tick){
        if(tick.timestamp <= openTime + barLength){
            this.close = tick.price;
            this.high = tick.price > high ? tick.price : this.high;
            this.low = tick.price < high ? tick.price : this.high;
            this.volume += tick.quantity;
            return true;
        }

        isClosed = true;
        return false;
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
        return isClosed ? close : 0;
    }

    public long getVolume() {
        return volume;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public String toString(){
        return String.format(
            "s:%s, o:%f, h:%f, l:%f, c:%f, v:%d, closed:%b",
            symbol, open, high, low, close, volume, isClosed
        );
    }
}
