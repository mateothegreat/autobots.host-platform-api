package autobots.platform.api.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
@AllArgsConstructor
public class Message<T> {

    private UUID uuid;

    private String action;

    private T payload;


}
