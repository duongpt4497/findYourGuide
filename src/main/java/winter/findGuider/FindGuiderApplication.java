package winter.findGuider;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@ComponentScan({"entities",  "services", "winter.findGuider", "winter.findGuider.web.api", "configuration", "security"})
public class FindGuiderApplication {

    public static void main(String[] args) {
        SpringApplication.run(FindGuiderApplication.class, args);
    }
    @Bean
        public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
