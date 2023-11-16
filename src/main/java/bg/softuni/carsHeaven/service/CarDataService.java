package bg.softuni.carsHeaven.service;


import bg.softuni.carsHeaven.model.dtos.cars.ReadCarDataDTO;

import java.util.List;

public interface CarDataService {

    List<ReadCarDataDTO> getAllDetailsForModel(Long modelId);

    ReadCarDataDTO getDetailsForModelByDetailId(Long modelId, Long detailId);

    boolean addDetailsForModel(Long modelId, ReadCarDataDTO readCarDataDTO);

    void removeById(Long detailId);

    boolean editDetail(Long detailId, ReadCarDataDTO readCarDataDTO);
}
