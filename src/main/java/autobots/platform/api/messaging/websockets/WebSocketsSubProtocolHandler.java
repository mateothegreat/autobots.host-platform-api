package autobots.platform.api.messaging.websockets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SubProtocolWebSocketHandler;

public class WebSocketsSubProtocolHandler extends SubProtocolWebSocketHandler {

    @Autowired
    private WebSocketsSessionHandler sessionHandler;

    public WebSocketsSubProtocolHandler(MessageChannel clientInboundChannel, SubscribableChannel clientOutboundChannel) {

        super(clientInboundChannel, clientOutboundChannel);

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        System.out.println("New websocket connection was established");

        sessionHandler.register(session);

        super.afterConnectionEstablished(session);

    }

}
