package bg.softuni.carsHeaven.repository;

import bg.softuni.carsHeaven.model.entity.CarData;
import bg.softuni.carsHeaven.model.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarDataRepository extends JpaRepository<CarData, Long> {

    List<CarData> findAllByModel(Model model);

}