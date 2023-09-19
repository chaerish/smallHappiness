package smu.likelion.smallHappiness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SmallHappinessApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmallHappinessApplication.class, args);
	}

}
