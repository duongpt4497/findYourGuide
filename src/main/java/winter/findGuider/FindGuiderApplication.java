package winter.findGuider;

import entity.TokenProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@ComponentScan({"entiy", "model", "winter.findGuider", "winter.findGuider.web.api", "security"})
public class FindGuiderApplication {

    public static void main(String[] args) {
        SpringApplication.run(FindGuiderApplication.class, args);
    }

//    @Bean
//    @ConfigurationProperties(prefix = "security.token")
//    public TokenProperties tokenProperties() {
//        return new TokenProperties();
//    }
//    @Bean
//    public PasswordEncoder passwordEncoder(@Value("${security.password.strength}") int strength) {
//        return new BCryptPasswordEncoder(strength);
//    }

}
