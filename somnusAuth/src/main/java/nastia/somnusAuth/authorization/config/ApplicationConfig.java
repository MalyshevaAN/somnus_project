package nastia.somnusAuth.authorization.config;


import com.google.common.hash.Hashing;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

@Configuration
public class ApplicationConfig {

    public String HashPassword(String password) {
        return Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
    }
}
