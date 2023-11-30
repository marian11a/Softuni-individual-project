package bg.softuni.carsHeaven.service.impl;

import bg.softuni.carsHeaven.model.entity.Role;
import bg.softuni.carsHeaven.model.entity.User;
import bg.softuni.carsHeaven.model.enums.RoleEnum;
import bg.softuni.carsHeaven.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CarsHeavenUserDetailsServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CarsHeavenUserDetailsService carsHeavenUserDetailsService;

    @Test
    public void testLoadUserByUsername() {
        User user = new User();
        user.setId(1L);
        user.setUsername("username1");
        user.setPassword("1234");
        Role role = new Role();
        role.setRole(RoleEnum.USER);
        user.setRoles(List.of(role));

        when(this.userRepository.findByUsername("username1")).thenReturn(user);
        UserDetails userDetails = carsHeavenUserDetailsService.loadUserByUsername("username1");

        Assertions.assertNotNull(userDetails);
    }

}