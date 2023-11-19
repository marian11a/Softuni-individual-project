package bg.softuni.carsHeaven.service;


import bg.softuni.carsHeaven.model.dtos.cars.ReadModelsDTO;
import bg.softuni.carsHeaven.model.dtos.users.UserRegisterDTO;

import java.util.List;

public interface UserService {
    boolean register(UserRegisterDTO userRegisterDTO);

    void addToFavorites(String username, Long modelId);

    List<ReadModelsDTO> getFavorites(String username);
}
