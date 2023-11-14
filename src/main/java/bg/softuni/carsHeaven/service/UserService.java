package bg.softuni.carsHeaven.service;


import bg.softuni.carsHeaven.model.dtos.users.UserLoginDTO;
import bg.softuni.carsHeaven.model.dtos.users.UserRegisterDTO;

public interface UserService {
    boolean register(UserRegisterDTO userRegisterDTO);

    boolean login(UserLoginDTO userLoginDTO);

    void logout();

}
