package org.upstox.tickerService.service;

import org.upstox.tickerService.model.Bar;

import java.util.Queue;

public class WebSocketService implements Runnable {
    private final Queue<Bar> barQueue;

    public WebSocketService(Queue<Bar> barQueue) {
        this.barQueue = barQueue;
    }

    public void run() {

    }
}
