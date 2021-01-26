package org.upstox.tickerService;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.upstox.tickerService.model.Bar;
import org.upstox.tickerService.model.Tick;
import org.upstox.tickerService.service.*;

import javax.servlet.ServletException;
import java.io.File;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TickerService {

    private final Logger logger;
    private final DataService dataService;
    private final StorageService storageService;
    private final WebSocketService socketService;

    public TickerService() throws ServletException, LifecycleException {
        // message passing queues
        Queue<Tick> tickerQueue = new ConcurrentLinkedQueue<>();
        Queue<Bar> barQueue = new ConcurrentLinkedQueue<>();

        logger = Logger.getLogger(TickerService.class.getName());

        dataService = new FileDataService(tickerQueue);
        storageService = new InMemoryStorageService(tickerQueue, barQueue);
        socketService = WebSocketService.getInstance();
        socketService.setBarQueue(barQueue);

        this.initTomcat();
    }

    public void initTomcat() throws ServletException, LifecycleException {
        logger.log(Level.INFO, "Starting Tomcat");
        // start tomcat
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        File file = new File("src/main/resources");
        tomcat.addWebapp("", file.getAbsolutePath());

        tomcat.start();
        tomcat.getServer().await();
    }

    public void startThreads(){
        logger.log(Level.INFO, "Starting threads");
        (new Thread(storageService)).start();
        (new Thread(socketService)).start();
        (new Thread(dataService)).start();
    }

    public static void main(String[] args) throws ServletException, LifecycleException {
        // basic configuration for Logger
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");

        TickerService tickerService = new TickerService();
        tickerService.initTomcat();
        tickerService.startThreads();
    }
}
