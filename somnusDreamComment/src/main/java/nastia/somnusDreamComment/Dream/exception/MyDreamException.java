package nastia.somnusDreamComment.Dream.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;

public class MyDreamException extends HttpStatusCodeException {

    public MyDreamException(HttpStatusCode statusCode, String statusText) {
        super(statusCode, statusText);
    }
}
