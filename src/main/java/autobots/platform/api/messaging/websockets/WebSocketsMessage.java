package autobots.platform.api.messaging.websockets;

import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
public class WebSocketsMessage {

    private MessageType type;
    private String      payload;
    private UUID        uuid;

    public enum MessageType {

        CONNECT, DISCONNECT, TEST, CAMERA_CONNECT, CAMERA_DISCONNECt, CAMERA_RECORDING_START, CAMERA_RECORDING_STOP

    }

}
