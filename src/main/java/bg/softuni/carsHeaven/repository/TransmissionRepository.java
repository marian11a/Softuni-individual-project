package bg.softuni.carsHeaven.repository;

import bg.softuni.carsHeaven.model.entity.Transmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransmissionRepository extends JpaRepository<Transmission, Long> {


}