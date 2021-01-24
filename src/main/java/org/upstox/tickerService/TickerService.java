package org.upstox.tickerService;

import org.upstox.tickerService.model.Bar;
import org.upstox.tickerService.model.Tick;
import org.upstox.tickerService.service.FileDataService;
import org.upstox.tickerService.service.InMemoryStorageService;
import org.upstox.tickerService.service.WebSocketService;

import java.util.LinkedList;
import java.util.Queue;

public class TickerService {

    public static void main(String[] args){
        // add event bus one for tick event other for bar event
        // whenever ticker happens send that tick to storage service
        // onTick update bar and raise bar event for websocket service
        // capture bar event and send updates
        // avoid synchronization

        Queue<Tick> tickerQueue = new LinkedList<Tick>();
        Queue<Bar> barQueue = new LinkedList<Bar>();

        FileDataService dataService = new FileDataService(tickerQueue);
        InMemoryStorageService storageService = new InMemoryStorageService(tickerQueue, barQueue);
        WebSocketService socketService = new WebSocketService(barQueue);

        (new Thread(dataService)).start();
        (new Thread(storageService)).start();
        (new Thread(socketService)).start();
    }
}
