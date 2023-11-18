package bg.softuni.carsHeaven.repository;

import bg.softuni.carsHeaven.model.entity.Role;
import bg.softuni.carsHeaven.model.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Role, Long> {


    Role findByRole(RoleEnum user);
}