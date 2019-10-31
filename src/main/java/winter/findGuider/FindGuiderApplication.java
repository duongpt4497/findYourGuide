package winter.findGuider;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableScheduling
@ComponentScan({"entities",  "services", "winter.findGuider", "winter.findGuider.web.api"})
public class FindGuiderApplication {

    public static void main(String[] args) {
        SpringApplication.run(FindGuiderApplication.class, args);
    }


}
