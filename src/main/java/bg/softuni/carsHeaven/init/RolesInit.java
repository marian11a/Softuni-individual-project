package bg.softuni.carsHeaven.init;

import bg.softuni.carsHeaven.model.entity.Role;
import bg.softuni.carsHeaven.model.enums.RoleEnum;
import bg.softuni.carsHeaven.repository.RolesRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class RolesInit implements CommandLineRunner {
    private final RolesRepository rolesRepository;

    public RolesInit(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        if (this.rolesRepository.count() <= 0) {
            Arrays.stream(RoleEnum.values()).forEach(value -> {
                Role role = new Role(value);
                role.setRole(value);
                this.rolesRepository.save(role);
            });
        }
    }
}
