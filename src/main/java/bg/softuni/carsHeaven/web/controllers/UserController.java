package bg.softuni.carsHeaven.web.controllers;

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

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return new ModelAndView("login");
        }
        return new ModelAndView("redirect:/home");
    }

    @PostMapping("/login-error")
    public ModelAndView loginError() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            ModelAndView modelAndView = new ModelAndView("login");
            modelAndView.addObject("hasLoginError", true);
            return modelAndView;
        }
        return new ModelAndView("redirect:/home");
    }


    @GetMapping("/register")
    public ModelAndView register(@ModelAttribute("userRegisterDTO") UserRegisterDTO userRegisterDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return new ModelAndView("register");
        }
        return new ModelAndView("redirect:/home");
    }

    @PostMapping("/register")
    public ModelAndView register(
            @ModelAttribute("userRegisterDTO") @Valid UserRegisterDTO userRegisterDTO,
            BindingResult bindingResult) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
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
}
