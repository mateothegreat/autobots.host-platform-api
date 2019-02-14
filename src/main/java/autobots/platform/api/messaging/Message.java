package autobots.platform.api.messaging;

import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
public class Message<T> {

    private UUID uuid;

    private T payload;

}
