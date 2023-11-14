package bg.softuni.carsHeaven.service;


import bg.softuni.carsHeaven.model.dtos.cars.ReadModelsDTO;

import java.util.List;

public interface ModelService {

    List<ReadModelsDTO> getAllModelsByBrand(Long brandId);


    boolean add(ReadModelsDTO readModelsDTO);

    ReadModelsDTO getModelById(Long modelId);

    void removeModel(Long id);

    boolean edit(ReadModelsDTO readModelsDTO);

    Long getBrandIdByModelId(Long modelId);
}
