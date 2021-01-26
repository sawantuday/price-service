package org.upstox.tickerService.websocket;

import com.google.gson.Gson;
import org.upstox.tickerService.model.Bar;
import org.upstox.tickerService.model.Message;
import org.upstox.tickerService.service.WebSocketService;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint(value = "/v1/ticker",
        decoders = WSDecoder.class,
        encoders = WSEncoder.class
)
public class WSServer {

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final WebSocketService wsService;
    private final Gson gson;
    private Session session;

    public WSServer(){
        wsService = WebSocketService.getInstance();
        gson = new Gson();
    }

    public String getId(){
        return session == null ? null : session.getId();
    }

    public void onBar(Bar bar){
        if(session != null) {
            this.session.getAsyncRemote().sendText(gson.toJson(bar));
            // TODO: add msg delivery verification
        }
    }

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        this.wsService.registerSession(this);
        logger.log(Level.INFO, "New session: {0}", session.getId());
    }

    @OnClose
    public void onClose(Session session){
        logger.log(Level.INFO, "Session closed for: {0}", session.getId());
        wsService.unregister(session.getId());
        wsService.removeSession(session.getId());
    }

    @OnMessage
    public void onMessage(Message message, Session session){
        logger.log(Level.INFO, "Message: {0} by session: {1}",
                new Object[]{message.message, session.getId()});
        if (message.getMessage().equals("subscribe")){
            wsService.subscribe(session.getId(), message.getSymbol());
        }else if(message.getMessage().equals("unsubscribe")){
            wsService.unsubscribe(session.getId(), message.getSymbol());
        }else {
            logger.log(Level.WARNING, "Unidentified message received: {0} by user: {1}",
                    new Object[]{message.getMessage(), session.getId()});
        }
    }

    @OnError
    public void onError(Throwable t, Session session){
        logger.log(Level.INFO, "Websocket error: {0} on session {1}",
                new Object[]{t.getMessage(), session.getId()});
    }

}
