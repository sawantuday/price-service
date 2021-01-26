package org.upstox.tickerService.service;

import org.upstox.tickerService.model.Bar;
import org.upstox.tickerService.websocket.WSServer;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebSocketService implements Runnable {

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final Map<String, Set<String>> tickerToUsers = new HashMap<>();
    private final Map<String, Set<String>> userToTickers = new HashMap<>();
    private static final WebSocketService serviceInstance = new WebSocketService();
    private final Map<String, WSServer> sessions = new HashMap<>();
    private Queue<Bar> barQueue;

    public WebSocketService(){}

    public void setBarQueue(Queue<Bar> barQueue){
        this.barQueue = barQueue;
    }

    public static WebSocketService getInstance(){
        return serviceInstance;
    }

    public void registerSession(WSServer session){
        sessions.put(session.getId(), session);
    }

    public void removeSession(String sessionId){
        sessions.remove(sessionId);
    }

    public void subscribe(String user, String ticker){
        logger.log(Level.INFO, "New subscription for {0} by user {1}",
                new Object[]{ticker, user});
        // add to tickerToUser map
        Set<String> userSet = tickerToUsers.getOrDefault(ticker, new HashSet<>());
        userSet.add(user);
        tickerToUsers.put(ticker, userSet);

        // add to userToTicker map
        Set<String> tickerSet = userToTickers.getOrDefault(user, new HashSet<>());
        tickerSet.add(ticker);
        userToTickers.put(user, tickerSet);
    }

    // remove single ticker
    public void unsubscribe(String user, String ticker){
        logger.log(Level.INFO, "Remove subscription for {0} by user {1}",
                new Object[]{ticker, user});
        tickerToUsers.get(ticker).remove(user);
        userToTickers.get(user).remove(ticker);
    }

    // user closed his connection remove all
    public void unregister(String user){
        logger.log(Level.INFO, "Connection closed by user {1}", user);
        for(Set<String> users: tickerToUsers.values()){
            users.remove(user);
        }
        userToTickers.remove(user);
    }

    @Override
    public void run() {
        while (true){
            while (barQueue.isEmpty());
            Bar bar = barQueue.remove();
            String symbol = bar.getSymbol();
            // all users subscribed to this ticker
            Set<String> users = tickerToUsers.getOrDefault(symbol, new HashSet<>());
            for (String user : users) {
                sessions.get(user).onBar(bar);  // send message
            }
        }
    }
}
