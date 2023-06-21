package nastia.somnusAuth;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan
@EnableWebMvc
public class SomnusAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(SomnusAuthApplication.class, args);
    }

}
