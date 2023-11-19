package bg.softuni.carsHeaven.web.controllers;

import bg.softuni.carsHeaven.model.dtos.cars.ReadModelsDTO;
import bg.softuni.carsHeaven.service.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ModelAndView index() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return new ModelAndView("index");
        }
        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/home")
    public ModelAndView home() {
        return new ModelAndView("home");
    }

    @GetMapping("/compare")
    public ModelAndView compare() {
        return new ModelAndView("compare");
    }

    @GetMapping("/favorites")
    public ModelAndView favorites() {
        ModelAndView modelAndView = new ModelAndView("favorites");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ReadModelsDTO> userFavoriteModels = this.userService.getFavorites(username);
        modelAndView.addObject("userFavoriteModels", userFavoriteModels);
        return modelAndView;
    }
}
