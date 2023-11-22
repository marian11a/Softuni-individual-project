package bg.softuni.carsHeaven.service.schedulers;

import bg.softuni.carsHeaven.model.dtos.cars.ReadBrandsDTO;
import bg.softuni.carsHeaven.model.dtos.users.UserDTO;
import bg.softuni.carsHeaven.service.BrandService;
import bg.softuni.carsHeaven.service.UserService;
import com.google.gson.Gson;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class writeToJsonScheduler {

    private final Gson gson;
    private final UserService userService;
    private final BrandService brandService;

    public writeToJsonScheduler(Gson gson,
                                UserService userService,
                                BrandService brandService) {
        this.gson = gson;
        this.userService = userService;
        this.brandService = brandService;
    }

    @Scheduled(cron = "0 30 13,21 * * *")
    public void userScheduler() {
        List<UserDTO> users = this.userService.getAll();
        try {
            Path jsonFolderPath = Paths.get("src", "main", "resources", "json");

            if (!jsonFolderPath.toFile().exists()) {
                jsonFolderPath.toFile().mkdir();
            }

            Path filePath = jsonFolderPath.resolve("scheduled-users-list.json");
            try (FileWriter writer = new FileWriter(filePath.toFile())) {
                this.gson.toJson(users, writer);
                System.out.println("JSON data written to file at: " + System.currentTimeMillis());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 30 13,21 * * *")
    public void carsScheduler() {
        List<ReadBrandsDTO> brands = this.brandService.getAll();
        try {
            Path jsonFolderPath = Paths.get("src", "main", "resources", "json");

            if (!jsonFolderPath.toFile().exists()) {
                jsonFolderPath.toFile().mkdir();
            }

            Path filePath = jsonFolderPath.resolve("scheduled-cars-list.json");
            try (FileWriter writer = new FileWriter(filePath.toFile())) {
                this.gson.toJson(brands, writer);
                System.out.println("JSON data written to file at: " + System.currentTimeMillis());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
