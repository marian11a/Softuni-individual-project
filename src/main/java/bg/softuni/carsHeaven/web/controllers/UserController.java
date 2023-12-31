package bg.softuni.carsHeaven.web.controllers;

import bg.softuni.carsHeaven.model.dtos.users.PasswordDTO;
import bg.softuni.carsHeaven.model.dtos.users.UserDTO;
import bg.softuni.carsHeaven.model.dtos.users.UserRegisterDTO;
import bg.softuni.carsHeaven.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        Authentication authentication = getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return new ModelAndView("login");
        }
        return new ModelAndView("redirect:/home");
    }

    @PostMapping("/login-error")
    public ModelAndView loginError() {
        Authentication authentication = getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            ModelAndView modelAndView = new ModelAndView("login");
            modelAndView.addObject("hasLoginError", true);
            return modelAndView;
        }
        return new ModelAndView("redirect:/home");
    }


    @GetMapping("/register")
    public ModelAndView register(@ModelAttribute("userRegisterDTO") UserRegisterDTO userRegisterDTO) {
        Authentication authentication = getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return new ModelAndView("register");
        }
        return new ModelAndView("redirect:/home");
    }

    @PostMapping("/register")
    public ModelAndView register(
            @ModelAttribute("userRegisterDTO") @Valid UserRegisterDTO userRegisterDTO,
            BindingResult bindingResult) {
        Authentication authentication = getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            if (bindingResult.hasErrors()) {
                return new ModelAndView("register");
            }

            boolean hasSuccessfulRegistration = userService.register(userRegisterDTO);
            if (!hasSuccessfulRegistration) {
                ModelAndView modelAndView = new ModelAndView("register");
                modelAndView.addObject("hasRegistrationError", true);
                return modelAndView;
            }

            return new ModelAndView("redirect:/login");
        }
        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/users/all-users")
    public ModelAndView allUsers() {
        List<UserDTO> allUsers = this.userService.getAll();
        ModelAndView modelAndView = new ModelAndView("all-users");
        modelAndView.addObject("allUsers", allUsers);
        return modelAndView;
    }

    @GetMapping("/users/profile")
    public ModelAndView profile() {
        Authentication authentication = getAuthentication();
        UserDTO userDTO = this.userService.findByName(authentication.getName());
        ModelAndView modelAndView = new ModelAndView("profile");
        modelAndView.addObject("userDTO", userDTO);
        return modelAndView;
    }

    @GetMapping("/users/change-password")
    public ModelAndView changePass(@ModelAttribute("passwordDTO") PasswordDTO passwordDTO) {
        return new ModelAndView("change-password");
    }

    @PostMapping("/users/change-password")
    public ModelAndView changePass(
            @ModelAttribute("passwordDTO") @Valid PasswordDTO passwordDTO,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("change-password");
            modelAndView.addObject("hasPasswordError", true);
            return modelAndView;
        }

        boolean hasSuccessfulChangeOfPassword = userService.changePassword(passwordDTO);
        if (!hasSuccessfulChangeOfPassword) {
            ModelAndView modelAndView = new ModelAndView("change-password");
            modelAndView.addObject("hasPasswordError", true);
            return modelAndView;
        }

        return new ModelAndView("redirect:/users/profile");
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
