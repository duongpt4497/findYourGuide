package winter.findGuider;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import services.contributionPoint.ContributionPointServiceImpl;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableScheduling
@ComponentScan({"entities",  "services", "winter.findGuider", "winter.findGuider.web.api"})
public class FindGuiderApplication {
    private static final Logger log = LoggerFactory.getLogger(FindGuiderApplication.class);
    public static void main(String[] args) {
        
        ApplicationContext context = SpringApplication.run(FindGuiderApplication.class, args);
        ContributionPointServiceImpl con = context.getBean(ContributionPointServiceImpl.class);
        con.updateContributionbyDay();
        con.updateContributionbyMonth();
        log.warn("Started");
    }


}
