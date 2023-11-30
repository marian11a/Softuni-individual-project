package bg.softuni.carsHeaven.service.impl;

import bg.softuni.carsHeaven.model.dtos.cars.ReadModelsDTO;
import bg.softuni.carsHeaven.model.dtos.users.PasswordDTO;
import bg.softuni.carsHeaven.model.dtos.users.UserDTO;
import bg.softuni.carsHeaven.model.dtos.users.UserRegisterDTO;
import bg.softuni.carsHeaven.model.entity.Brand;
import bg.softuni.carsHeaven.model.entity.Model;
import bg.softuni.carsHeaven.model.entity.Role;
import bg.softuni.carsHeaven.model.entity.User;
import bg.softuni.carsHeaven.model.enums.RoleEnum;
import bg.softuni.carsHeaven.repository.ModelRepository;
import bg.softuni.carsHeaven.repository.RolesRepository;
import bg.softuni.carsHeaven.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RolesRepository rolesRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ModelRepository modelRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    public UserServiceImplTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRegister() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("username", "password", "password", "email");
        when(userRepository.existsByUsernameOrEmail("username", "email")).thenReturn(false);
        when(modelMapper.map(userRegisterDTO, User.class)).thenReturn(new User());
        when(rolesRepository.findByRole(RoleEnum.USER)).thenReturn(new Role());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        boolean result = userService.register(userRegisterDTO);
        assertTrue(result);
        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void testRegisterWithMismatchedPasswords() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("username", "password", "differentPassword", "email");
        boolean result = userService.register(userRegisterDTO);

        assertFalse(result);
        verify(userRepository, never()).save(any());
    }

    @Test
    public void testRegisterWithExistingUsernameOrEmail() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("existingUsername", "password", "password", "existingEmail");
        when(userRepository.existsByUsernameOrEmail("existingUsername", "existingEmail")).thenReturn(true);

        boolean result = userService.register(userRegisterDTO);
        assertFalse(result);
        verify(userRepository, never()).save(any());
    }

    @Test
    public void testAddToFavorites() {
        String username = "user";
        Long modelId = 1L;
        User user = new User();
        user.setUsername(username);
        user.setFavoriteCars(new ArrayList<>());
        Model model = new Model();
        model.setId(modelId);

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(modelRepository.findById(modelId)).thenReturn(Optional.of(model));

        userService.addToFavorites(username, modelId);
        verify(userRepository, times(1)).save(user);
        assertEquals(Collections.singletonList(model), user.getFavoriteCars());
    }

    @Test
    public void testGetFavorites() {
        String username = "user";
        User user = new User();
        user.setUsername(username);
        user.setFavoriteCars(new ArrayList<>());

        Brand brand = new Brand();
        brand.setName("brand");

        Model model1 = new Model();
        model1.setId(1L);
        model1.setName("Model1");
        model1.setImageUrl("image1");
        model1.setBrand(brand);

        Model model2 = new Model();
        model2.setId(2L);
        model2.setName("Model2");
        model2.setImageUrl("image2");
        model2.setBrand(brand);

        user.setFavoriteCars(Arrays.asList(model1, model2));
        when(userRepository.findByUsername(username)).thenReturn(user);

        List<ReadModelsDTO> result = userService.getFavorites(username);

        assertEquals(2, result.size());
        assertEquals("Model1", result.get(0).getName());
        assertEquals("Model2", result.get(1).getName());
    }

    @Test
    public void testMakeAdmin() {
        User user = new User();
        user.setId(1L);
        user.setRoles(Collections.singletonList(new Role()));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(rolesRepository.findAll()).thenReturn(Arrays.asList(new Role(), new Role()));
        userService.makeAdmin(1L);

        verify(userRepository, times(1)).save(user);
        assertEquals(2, user.getRoles().size());
    }

    @Test
    public void testRemoveAdmin() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setRoles(Arrays.asList(new Role(), new Role()));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(this.rolesRepository.findByRole(RoleEnum.USER)).thenReturn(new Role());

        userService.removeAdmin(userId);
        verify(userRepository, times(1)).save(user);
        assertEquals(1, user.getRoles().size());
    }

    @Test
    public void testFindByName() {
        String username = "username";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class))
                .thenReturn(new UserDTO(username));

        UserDTO result = userService.findByName(username);
        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

    @Test
    public void testChangePassword() {
        String username = "testUser";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, oldPassword);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        User currentUser = new User();
        currentUser.setUsername(username);
        currentUser.setPassword("$2a$10$mockedHashedPassword");

        when(userRepository.findByUsername(username)).thenReturn(currentUser);
        when(passwordEncoder.matches(oldPassword, currentUser.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("$2a$10$mockedNewHashedPassword");

        boolean result = userService.changePassword(new PasswordDTO(oldPassword, newPassword, newPassword));
        assertTrue(result);
        verify(userRepository, times(1)).save(currentUser);
        assertEquals("$2a$10$mockedNewHashedPassword", currentUser.getPassword());
    }

    @Test
    public void testRemoveFromFavorites() {
        String username = "testUser";
        Long modelIdToRemove = 1L;

        User user = new User();
        user.setUsername(username);

        Model model1 = new Model();
        model1.setId(1L);

        Model model2 = new Model();
        model2.setId(2L);

        List<Model> favoriteCars = new ArrayList<>();
        favoriteCars.add(model1);
        favoriteCars.add(model2);
        user.setFavoriteCars(favoriteCars);

        when(userRepository.findByUsername(username)).thenReturn(user);
        userService.removeFromFavorites(username, modelIdToRemove);

        verify(userRepository, times(1)).save(user);
        assertEquals(1, user.getFavoriteCars().size());
        assertTrue(user.getFavoriteCars().stream().noneMatch(model -> Objects.equals(model.getId(), modelIdToRemove)));
    }
}
