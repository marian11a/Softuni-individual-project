package bg.softuni.carsHeaven.repository;

import bg.softuni.carsHeaven.model.entity.Engine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EngineRepository extends JpaRepository<Engine, Long> {
    
}