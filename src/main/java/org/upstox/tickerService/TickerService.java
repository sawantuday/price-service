package org.upstox.tickerService;

import org.upstox.tickerService.model.Bar;
import org.upstox.tickerService.model.Tick;
import org.upstox.tickerService.service.FileDataService;
import org.upstox.tickerService.service.InMemoryStorageService;
import org.upstox.tickerService.websocket.WebSocketService;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

public class TickerService {

    private Logger logger = Logger.getLogger(getClass().getName());
    public static void main(String[] args){

        Queue<Tick> tickerQueue = new ConcurrentLinkedQueue<>();
        Queue<Bar> barQueue = new ConcurrentLinkedQueue<>();

        FileDataService dataService = new FileDataService(tickerQueue);
        InMemoryStorageService storageService =
                new InMemoryStorageService(tickerQueue, barQueue);
        WebSocketService socketService = new WebSocketService(barQueue);

        (new Thread(dataService)).start();
        (new Thread(storageService)).start();
        (new Thread(socketService)).start();

    }
}
