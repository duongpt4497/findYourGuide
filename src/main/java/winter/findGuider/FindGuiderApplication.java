package winter.findGuider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"entiy","model","winter.findGuider","winter.findGuider.web.api"})
public class FindGuiderApplication {

	public static void main(String[] args) {
		SpringApplication.run(FindGuiderApplication.class, args);
	}

}
