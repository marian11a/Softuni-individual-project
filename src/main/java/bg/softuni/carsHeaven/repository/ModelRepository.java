package bg.softuni.carsHeaven.repository;

import bg.softuni.carsHeaven.model.entity.Brand;
import bg.softuni.carsHeaven.model.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {

    List<Model> findAllByBrand(Brand brand);

    Model findByName(String name);

}