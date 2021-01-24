package org.upstox.tickerService.service;

import org.upstox.tickerService.model.Bar;

import java.util.Queue;
import java.util.logging.Logger;

public class WebSocketService implements Runnable {
    private final Queue<Bar> barQueue;
    private Logger logger = Logger.getLogger(getClass().getName());

    public WebSocketService(Queue<Bar> barQueue) {
        this.barQueue = barQueue;
    }

    public void run() {

    }
}
