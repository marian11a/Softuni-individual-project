package bg.softuni.carsHeaven;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CarsHeavenApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarsHeavenApplication.class, args);
	}

}
