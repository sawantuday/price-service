package org.upstox.tickerService;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.upstox.tickerService.model.Bar;
import org.upstox.tickerService.model.Tick;
import org.upstox.tickerService.service.FileDataService;
import org.upstox.tickerService.service.InMemoryStorageService;
import org.upstox.tickerService.service.WebSocketService;

import javax.servlet.ServletException;
import java.io.File;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

public class TickerService {

    private static Logger logger;
    public static void main(String[] args) throws ServletException, LifecycleException {
        // basic configuration for Logger
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");

        logger = Logger.getLogger(TickerService.class.getName());

        // message passing queues
        Queue<Tick> tickerQueue = new ConcurrentLinkedQueue<>();
        Queue<Bar> barQueue = new ConcurrentLinkedQueue<>();

        FileDataService dataService = new FileDataService(tickerQueue);
        InMemoryStorageService storageService =
                new InMemoryStorageService(tickerQueue, barQueue);
        WebSocketService socketService = WebSocketService.getInstance();
        socketService.setBarQueue(barQueue);

        // start threads
        (new Thread(dataService)).start();
        (new Thread(storageService)).start();
        (new Thread(socketService)).start();

        // start tomcat
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        File file = new File("src/main/resources");
        tomcat.addWebapp("", file.getAbsolutePath());

        tomcat.start();
        tomcat.getServer().await();

    }
}
