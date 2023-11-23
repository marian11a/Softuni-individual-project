package bg.softuni.carsHeaven.repository;

import bg.softuni.carsHeaven.model.dtos.cars.ReadBrandsDTO;
import bg.softuni.carsHeaven.model.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    @Query("SELECT new bg.softuni.carsHeaven.model.dtos.cars.ReadBrandsDTO(b.id, b.name, b.imageUrl) FROM Brand b")
    List<ReadBrandsDTO> findAllBrands();
    Optional<Brand> findByName(String name);
}