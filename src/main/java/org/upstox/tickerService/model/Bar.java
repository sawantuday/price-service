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

    public Bar(int barId){
        this.id = barId;
    }

    public Bar(Bar bar){    // deep copy constructor
        this.id = bar.id;
        this.open = bar.open;
        this.close = bar.close;
        this.low = bar.low;
        this.high = bar.high;
        this.isClosed = bar.isClosed;
        this.volume = bar.volume;
    }

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
            this.volume += tick.quantity;
            if(tick.price < low){
                this.low = tick.price;
            }
            if (tick.price > high){
                this.high = tick.price;
            }
            if (this.open == 0){    // to cover bars created without tick
                this.open = tick.price;
            }
            return true;
        }

        isClosed = true;
        return false;
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
