package bg.softuni.carsHeaven.service.impl;

import bg.softuni.carsHeaven.model.entity.Role;
import bg.softuni.carsHeaven.model.entity.User;
import bg.softuni.carsHeaven.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class CarsHeavenUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CarsHeavenUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username);
        return map(user);
    }

    private static UserDetails map(User user) {
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRoles().stream().map(CarsHeavenUserDetailsService::map).toList())
                .build();
    }

    private static GrantedAuthority map(Role roles) {
        return new SimpleGrantedAuthority("ROLE_" + roles.getRole().name());
    }
}
