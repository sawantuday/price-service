package org.upstox.tickerService.service;

import com.google.gson.Gson;
import org.upstox.tickerService.model.Tick;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class FileDataService implements DataService, Runnable {

    private final Queue<Tick> tickerQueue;
    private final Gson gson;
    private long lastMsgTS;
    private Logger logger = Logger.getLogger(getClass().getName());

    public FileDataService(Queue<Tick> tickerQueue) {
        this.tickerQueue = tickerQueue;
        this.gson = new Gson();
    }

    private BufferedReader getBufferedReader() throws FileNotFoundException {
        URL url = getClass().getClassLoader().getResource("trades.json");
        if(url == null){
            return null;
        }
        File file = new File(url.getPath());
        FileReader fileReader = new FileReader(file);
        return new BufferedReader(fileReader);
    }

    public void getTradeData(){
        try {
            BufferedReader reader = getBufferedReader();

            String line = reader.readLine();
            if(line != null){   // initialize last timestamp
                Tick tick = gson.fromJson(line, Tick.class);
                lastMsgTS = tick.getTimestamp();
                onLine(line);
            }

            while((line = reader.readLine()) != null){
                onLine(line);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onLine(String line) {
        Tick tick = gson.fromJson(line, Tick.class);

        // sleep till time to release this ticker
        long delayNS = tick.getTimestamp() - lastMsgTS;
        try {
            Thread.sleep(TimeUnit.NANOSECONDS.toMillis(delayNS));
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(tick);
        lastMsgTS = tick.getTimestamp();    // update lastMsgTimestamp
        tickerQueue.add(tick);              // send this tick to processing
    }

    @Override
    public void run() {
        getTradeData();
    }

    public static void main(String[] args) {
        FileDataService fileDataService = new FileDataService(new LinkedList<>());
        fileDataService.getTradeData();
    }

}
