package bg.softuni.carsHeaven.service;


import bg.softuni.carsHeaven.model.dtos.cars.ReadModelsDTO;
import bg.softuni.carsHeaven.model.dtos.users.PasswordDTO;
import bg.softuni.carsHeaven.model.dtos.users.UserDTO;
import bg.softuni.carsHeaven.model.dtos.users.UserRegisterDTO;

import java.util.List;

public interface UserService {
    boolean register(UserRegisterDTO userRegisterDTO);

    void addToFavorites(String username, Long modelId);

    List<ReadModelsDTO> getFavorites(String username);

    void removeFromFavorites(String username, Long modelId);

    List<UserDTO> getAll();

    void makeAdmin(Long userId);

    void removeAdmin(Long userId);

    UserDTO findByName(String name);

    boolean changePassword(PasswordDTO passwordDTO);
}
