package winter.findGuider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@ComponentScan({"entity", "services", "winter.findGuider", "winter.findGuider.web.api", "configuration", "repository"})
public class FindGuiderApplication {

    public static void main(String[] args) {
        SpringApplication.run(FindGuiderApplication.class, args);
    }

    @Bean
        public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
