package org.upstox.tickerService.websocket;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.upstox.tickerService.model.Bar;
import org.upstox.tickerService.model.Message;

import javax.servlet.ServletException;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint(value = "/v1/ticker",
        decoders = WSDecoder.class,
        encoders = WSEncoder.class
)
public class WebSocketService implements Runnable {
    private final Queue<Bar> barQueue;
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final Map<String, List<String>> tickerToUsers = new HashMap<>();
    private final Map<String, List<String>> userToTickers = new HashMap<>();

    public WebSocketService(Queue<Bar> barQueue) {
        this.barQueue = barQueue;
    }

    public WebSocketService(){
        barQueue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void run() {

    }

    @OnOpen
    public void onOpen(Session session){
        logger.log(Level.INFO, "New session: {0}", session.getId());
    }

    @OnClose
    public void onClose(Session session){
        logger.log(Level.INFO, "Session closed for: {0}", session.getId());
    }

    @OnMessage
    public void onMessage(Message message, Session session){
        logger.log(Level.INFO, "Message: {0} by session: {1}",
                new Object[]{message.message, session.getId()});
    }

    @OnError
    public void onError(Throwable t, Session session){
        logger.log(Level.INFO, "Websocket error: {0} on session {1}",
                new Object[]{t.getMessage(), session.getId()});
    }

    public static void main(String[] args) throws ServletException, LifecycleException {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        File file = new File("src/main/resources");
        tomcat.addWebapp("", file.getAbsolutePath());

        tomcat.start();
        tomcat.getServer().await();

    }



}
