package bg.softuni.carsHeaven.service.impl;

import bg.softuni.carsHeaven.model.dtos.users.UserRegisterDTO;
import bg.softuni.carsHeaven.model.entity.Role;
import bg.softuni.carsHeaven.model.entity.User;
import bg.softuni.carsHeaven.model.enums.RoleEnum;
import bg.softuni.carsHeaven.repository.RolesRepository;
import bg.softuni.carsHeaven.repository.UserRepository;
import bg.softuni.carsHeaven.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RolesRepository rolesRepository,
                           ModelMapper modelMapper,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
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
        user.setRoles(List.of(this.rolesRepository.findByRole(RoleEnum.USER)));
        this.userRepository.save(user);
        return true;
    }
}
