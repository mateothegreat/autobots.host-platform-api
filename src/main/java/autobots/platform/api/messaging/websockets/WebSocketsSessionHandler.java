package autobots.platform.api.messaging.websockets;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
public class WebSocketsSessionHandler {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    public WebSocketsSessionHandler() {

//        scheduler.scheduleAtFixedRate(() -> sessionMap.keySet().forEach(k -> {
//
//            try {
//
//                sessionMap.get(k).close();
//                sessionMap.remove(k);
//
//            } catch (IOException e) {
//
//                System.out.println(e);
//
//            }
//
//        }), 10, 10, TimeUnit.SECONDS);

    }

    public void register(WebSocketSession session) {

        sessionMap.put(session.getId(), session);

    }

    public WebSocketSession getSessionById(String sessionId) {

        return sessionMap.get(sessionId);

    }

}
