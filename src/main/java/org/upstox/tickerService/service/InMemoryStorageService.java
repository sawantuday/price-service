package org.upstox.tickerService.service;

import org.upstox.tickerService.model.Bar;
import org.upstox.tickerService.model.Tick;

import java.util.Queue;

/**
 *
 */
public class InMemoryStorageService implements StorageService, Runnable{


    private final Queue<Bar> barQueue;
    private final Queue<Tick> tickerQueue;

    public InMemoryStorageService(Queue<Tick> tickerQueue, Queue<Bar> barQueue) {
        this.tickerQueue = tickerQueue;
        this.barQueue = barQueue;
    }

    public void run() {

    }

    private void onTick(){

    }
}
