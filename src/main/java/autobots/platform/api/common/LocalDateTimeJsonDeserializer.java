package autobots.platform.api.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;

public class LocalDateTimeJsonDeserializer extends StdDeserializer<LocalDateTime> {

    public LocalDateTimeJsonDeserializer() {

        super(LocalDateTime.class);

    }

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        System.out.println(p.getValueAsString());

        return LocalDateTime.parse(p.getValueAsString()); // or overloaded with an appropriate format

    }

}
