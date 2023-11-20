package bg.softuni.carsHeaven.repository;

import bg.softuni.carsHeaven.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsernameOrEmail(String username, String email);

    User findByUsername(String username);

    List<User> findByFavoriteCars_Id(Long id);
}