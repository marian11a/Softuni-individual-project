package bg.softuni.carsHeaven.service.impl;

import bg.softuni.carsHeaven.model.dtos.users.UserLoginDTO;
import bg.softuni.carsHeaven.model.dtos.users.UserRegisterDTO;
import bg.softuni.carsHeaven.model.entity.User;
import bg.softuni.carsHeaven.repository.UserRepository;
import bg.softuni.carsHeaven.security.LoggedUser;
import bg.softuni.carsHeaven.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    private final LoggedUser loggedUser;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, LoggedUser loggedUser) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.loggedUser = loggedUser;
    }


    @Override
    public boolean register(UserRegisterDTO userRegisterDTO) {
        if (!userRegisterDTO.getPassword().equals(userRegisterDTO.getConfirmPassword())) {
            return false;
        }

        boolean existsByUsernameOrEmail = this.userRepository.existsByUsernameOrEmail(
                userRegisterDTO.getUsername(),
                userRegisterDTO.getEmail());

        if (existsByUsernameOrEmail) {
            return false;
        }

        User user = modelMapper.map(userRegisterDTO, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.userRepository.save(user);
        return true;
    }

    @Override
    public boolean login(UserLoginDTO userLoginDto) {
        User dbUser = this.userRepository.findByUsername(userLoginDto.getUsername());
        if (dbUser != null && passwordEncoder.matches(userLoginDto.getPassword(), dbUser.getPassword())) {
            this.loggedUser.login(userLoginDto.getUsername());
            return true;
        }
        return false;
    }

    @Override
    public void logout() {
        this.loggedUser.logout();
    }
}
