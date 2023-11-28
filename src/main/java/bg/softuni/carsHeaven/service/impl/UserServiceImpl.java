package bg.softuni.carsHeaven.service.impl;

import bg.softuni.carsHeaven.model.dtos.cars.ReadModelsDTO;
import bg.softuni.carsHeaven.model.dtos.users.PasswordDTO;
import bg.softuni.carsHeaven.model.dtos.users.UserDTO;
import bg.softuni.carsHeaven.model.dtos.users.UserRegisterDTO;
import bg.softuni.carsHeaven.model.entity.Model;
import bg.softuni.carsHeaven.model.entity.Role;
import bg.softuni.carsHeaven.model.entity.User;
import bg.softuni.carsHeaven.model.enums.RoleEnum;
import bg.softuni.carsHeaven.repository.ModelRepository;
import bg.softuni.carsHeaven.repository.RolesRepository;
import bg.softuni.carsHeaven.repository.UserRepository;
import bg.softuni.carsHeaven.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final ModelMapper modelMapper;
    private final ModelRepository modelRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RolesRepository rolesRepository,
                           ModelMapper modelMapper,
                           ModelRepository modelRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.modelMapper = modelMapper;
        this.modelRepository = modelRepository;
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

    @Override
    public void addToFavorites(String username, Long modelId) {
        User user = this.userRepository.findByUsername(username);
        Optional<Model> byId = this.modelRepository.findById(modelId);
        if (byId.isPresent()) {
            List<Model> favoriteCars = new ArrayList<>();
            if (!user.getFavoriteCars().isEmpty() && user.getFavoriteCars() != null) {
                favoriteCars.addAll(user.getFavoriteCars());
            }

            boolean exist = false;
            for (Model model : favoriteCars) {
                if (Objects.equals(model.getId(), modelId)) {
                    exist = true;
                    break;
                }
            }

            if (!exist) {
                favoriteCars.add(byId.get());
            }

            user.setFavoriteCars(favoriteCars);
            this.userRepository.save(user);
        }
    }

    @Override
    public List<ReadModelsDTO> getFavorites(String username) {
        List<ReadModelsDTO> allModelsDTO = new ArrayList<>();
        this.userRepository.findByUsername(username).getFavoriteCars().forEach(model -> {
            ReadModelsDTO readModelsDTO = new ReadModelsDTO();
            readModelsDTO.setName(model.getName());
            readModelsDTO.setId(model.getId());
            readModelsDTO.setImageUrl(model.getImageUrl());
            readModelsDTO.setBrandName(model.getBrand().getName());
            allModelsDTO.add(readModelsDTO);
        });

        allModelsDTO.sort(Comparator.comparing(ReadModelsDTO::getName));
        return allModelsDTO;
    }

    @Override
    public void removeFromFavorites(String username, Long modelId) {
        User user = this.userRepository.findByUsername(username);
        List<Model> favoriteCars = user.getFavoriteCars();
        favoriteCars.removeIf(model -> Objects.equals(model.getId(), modelId));
        user.setFavoriteCars(favoriteCars);
        this.userRepository.save(user);
    }

    @Override
    public List<UserDTO> getAll() {
        List<UserDTO> userDTOS = Arrays
                .stream(this.modelMapper.map(this.userRepository.findAll(), UserDTO[].class))
                .toList();

        populateRolesColumn(userDTOS);
        return userDTOS;
    }

    @Override
    public void makeAdmin(Long userId) {
        Optional<User> byId = this.userRepository.findById(userId);
        if (byId.isPresent()) {
            List<Role> roles = byId.get().getRoles();
            if (roles.size() <= 1) {
                User user = this.userRepository.findById(userId)
                        .get();
                user.setRoles(this.rolesRepository.findAll());
                this.userRepository.save(user);
            }
        }
    }

    @Override
    public void removeAdmin(Long userId) {
        Optional<User> byId = this.userRepository.findById(userId);
        if (byId.isPresent()) {
            User user = this.userRepository.findById(userId).get();
            user.setRoles(List.of(this.rolesRepository.findByRole(RoleEnum.USER)));
            this.userRepository.save(user);

        }
    }

    @Override
    public UserDTO findByName(String name) {
        return this.modelMapper.map(this.userRepository.findByUsername(name), UserDTO.class);
    }

    @Override
    public boolean changePassword(PasswordDTO passwordDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = this.userRepository.findByUsername(username);

        if (!this.passwordEncoder.matches(passwordDTO.getOldPassword(), currentUser.getPassword())) {
            return false;
        }

        if (!passwordDTO.getPassword().equals(passwordDTO.getConfirmPassword())) {
            return false;
        }

        currentUser.setPassword(this.passwordEncoder.encode(passwordDTO.getPassword()));
        this.userRepository.save(currentUser);
        return true;
    }

    private void populateRolesColumn(List<UserDTO> userDTOS) {
        for (UserDTO userDTO : userDTOS) {
            List<RoleEnum> roles = new ArrayList<>();
            for (Role role : this.userRepository.findByUsername(userDTO.getUsername()).getRoles()) {
                roles.add(role.getRole());
            }
            userDTO.setRoles(roles);
        }
    }
}
