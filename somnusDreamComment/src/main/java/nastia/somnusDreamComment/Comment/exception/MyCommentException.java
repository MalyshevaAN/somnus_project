package nastia.somnusDreamComment.Comment.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;

public class MyCommentException extends HttpStatusCodeException {

    public MyCommentException(HttpStatusCode statusCode, String statusText) {
        super(statusCode, statusText);
    }
}
