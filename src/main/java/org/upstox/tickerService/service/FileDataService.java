package org.upstox.tickerService.service;

import com.google.gson.Gson;
import org.upstox.tickerService.model.Tick;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class FileDataService implements DataService, Runnable {

    private final Queue<Tick> tickerQueue;
    private final Gson gson;
    private long lastMsgTS;

    public FileDataService(Queue<Tick> tickerQueue) {
        this.tickerQueue = tickerQueue;
        this.gson = new Gson();
    }

    public void getTradeData(){
        try {
            URL url = getClass().getClassLoader().getResource("trades.json");
            File file = new File(url.getPath());
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            int limit = 0;
            String line;
            lastMsgTS = 1538409725339216503L;
            while((line = bufferedReader.readLine()) != null){
                onLine(line);
                if(++limit > 10){
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void onLine(String line) throws InterruptedException {
        Tick tick = gson.fromJson(line, Tick.class);
        long delayNS = tick.getTimestamp() - lastMsgTS;
        System.out.println(delayNS);
        String utc = Instant.ofEpochSecond(0L, tick.getTimestamp())
                .atZone(ZoneId.of("Asia/Kolkata"))
                .format(DateTimeFormatter.ofPattern(
                        "dd-MM-uuuu HH:mm:ss" ,
                        Locale.ENGLISH
                ));
        System.out.println(utc);
        if(delayNS > 0){
            long durationInMs = TimeUnit.NANOSECONDS.toMillis(delayNS);
            Thread.sleep(durationInMs);
        }

        lastMsgTS = tick.getTimestamp();

        System.out.println(tick);

        tickerQueue.add(tick);

    }

    public void run() {

    }

    public static void main(String[] args) {
        FileDataService fileDataService = new FileDataService(new LinkedList<>());
        fileDataService.getTradeData();
//        long ts = 1538409725339216503L;
//        long seconds = ts / 1_000_000_000;
//        long nanos = ts % 1_000_000_000;
//        System.out.println(seconds);
//        System.out.println(nanos);

//        Thread.sleep(1, 2);
//        long delayNS = 0;
//        long durationInMs = TimeUnit.NANOSECONDS.toMillis(delayNS);

//        String utc = Instant.ofEpochSecond(0L, tick.getTimestamp())
//                .atZone(ZoneId.of("Asia/Kolkata"))
//                .format(DateTimeFormatter.ofPattern(
//                        "dd-MM-uuuu HH:mm:ss" ,
//                        Locale.ENGLISH
//                ));

    }

}
