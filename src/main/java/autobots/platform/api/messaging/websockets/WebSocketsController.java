package autobots.platform.api.messaging.websockets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;

@RestController
public class WebSocketsController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private SimpUserRegistry         simpUserRegistry;
    @Autowired
    private WebSocketsSessionHandler webSocketsSessionHandler;

    @MessageMapping("/message")
    @SendTo("/topic/reply")
    public WebSocketsMessage processMessageFromClient(@Payload WebSocketsMessage message, Principal principal, @Header("simpSessionId") String sessionId) throws IOException {

//        template.convertAndSendToUser("test@test.com", "/user/test@test.com/test", 1);
//        template.convertAndSendToUser("test@test.com", "/user/test@test.com", 2);
//        template.convertAndSendToUser("test@test.com", "/user", 3);
//        template.convertAndSendToUser("test@test.com", "test@test.com", 4);
        template.convertAndSend("/user/test2@test.com", 123);
        System.out.println("WebSocketsMessage: " + message.toString());

        if (message.getType() == WebSocketsMessage.MessageType.DISCONNECT) {

            webSocketsSessionHandler.getSessionById(sessionId).close();

        } else {


        }

        return message;

    }

    @MessageExceptionHandler
    @SendToUser("/topic/errors")
    public String handleException(Throwable exception) {

        return exception.getMessage();

    }

}
