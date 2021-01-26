package org.upstox.tickerService.service;

import org.upstox.tickerService.model.Bar;
import org.upstox.tickerService.model.Tick;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class InMemoryStorageService implements StorageService {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private final Queue<Tick> tickerQueue;
    private final Queue<Bar> history;  // maintain history for rest api
    private final Queue<Bar> barQueue;  // to WebSocket

    private final Map<String, Bar> bars;
    private final Object barLock = new Object();
    private final int startId = 1;

    public InMemoryStorageService(Queue<Tick> tickerQueue, Queue<Bar> barQueue) {
        this.tickerQueue = tickerQueue;
        this.barQueue = barQueue;
        this.history = new LinkedList<>();
        this.bars = new HashMap<>();
    }

    private void initBar(String symbol){
        logger.log(Level.INFO, "Starting bar creation routine at 15 second delay");
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            String sym;
            @Override
            public void run() {
                this.sym = symbol;
                synchronized (barLock){
                    Bar bar = bars.get(this.sym);
                    int id = startId;

                    try {
                        if (bar != null) {
                            bar.setClosed();
                            Bar barCopy = new Bar(bar); // deep copy
                            barQueue.add(barCopy);  // send to websocket
                            history.add(barCopy);   // maintain history for rest api
                            id = bar.getId() + 1;   // ID for next bar
                        }
                    }catch(Exception e){
                        logger.log(Level.WARNING, "Exception while trying to update bar for {0}", this.sym);
                        e.printStackTrace();
                    }

                    bar = new Bar(id, this.sym);
                    bars.put(this.sym, bar);
                    logger.log(Level.INFO, "Generating a new bar: {0} for Symbol: {1}",
                            new Object[]{bar.getId(), this.sym});
                }
            }
        }, 0, 15000);
    }

    @Override
    public void run() {
        logger.log(Level.INFO, "MemoryStorage thread started");
        while(true){    // TODO: add some logic to stop this thread
            while(tickerQueue.isEmpty());   // wait till we get new tick
            logger.log(Level.INFO, "Received tick");
            onTick(tickerQueue.remove());   // add it to current bar
        }
    }

    @Override
    public void onTick(Tick tick){
        Bar bar = this.bars.get(tick.getSymbol());
        if(bar == null){
            initBar(tick.getSymbol());
            while (bar==null){
                bar = this.bars.get(tick.getSymbol());
            };
        }

        synchronized (barLock){
            bar.addTick(tick);
            Bar barCopy = new Bar(bar); // deep copy
            barQueue.add(barCopy);  // send to WebSocket
        }

        logger.log(Level.INFO, "Processed tick for {0} on bar {1}",
                new Object[]{tick.getSymbol(), bar.getId()});
    }
}
