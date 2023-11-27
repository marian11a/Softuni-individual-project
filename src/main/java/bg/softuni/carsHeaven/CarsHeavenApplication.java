package bg.softuni.carsHeaven;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@OpenAPIDefinition(
		info = @Info(
				title = "Cars Heaven",
				version = "v.0.0.1",
				description = "The REST API of Cars Heaven"
		),
		servers = {
				@Server(
						url = "http://localhost:8080",
						description = "Local server"
				)
		}
)
@SpringBootApplication
@EnableScheduling
public class CarsHeavenApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarsHeavenApplication.class, args);
	}

}
