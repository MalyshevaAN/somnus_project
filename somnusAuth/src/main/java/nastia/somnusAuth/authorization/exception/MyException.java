package nastia.somnusAuth.authorization.exception;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;


@Getter
@Setter
public class MyException extends HttpStatusCodeException {
    public MyException(HttpStatusCode statusCode, String statusText) {
        super(statusCode, statusText);
    }
}
