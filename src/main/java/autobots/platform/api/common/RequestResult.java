package autobots.platform.api.common;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestResult {

    public static final String RESULT_ERROR = "error";
    public static final String RESULT_OK    = "ok";

    public String result;
    public String message;
    public Object data;

    public RequestResult(String result) {

        this.result = result;

    }

    public RequestResult(String result, String message) {

        this.result = result;
        this.message = message;

    }

    public RequestResult(String result, Object data) {

        this.result = result;
        this.data = data;

    }

    public RequestResult(String result, String message, Object data) {

        this.result = result;
        this.message = message;
        this.data = data;

    }

}
