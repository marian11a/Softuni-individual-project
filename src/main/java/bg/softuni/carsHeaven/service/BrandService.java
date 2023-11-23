package bg.softuni.carsHeaven.service;

import bg.softuni.carsHeaven.model.dtos.cars.ReadBrandsDTO;
import java.util.List;

public interface BrandService {
    List<ReadBrandsDTO> getAllBrands();

    ReadBrandsDTO getBrandById(Long brandId);

    boolean edit(ReadBrandsDTO readBrandsDTO);

    String getImageUrlById(Long brandId);

    void remove(Long brandId);

    boolean add(ReadBrandsDTO readBrandsDTO);


    List<ReadBrandsDTO> getAll();

}
