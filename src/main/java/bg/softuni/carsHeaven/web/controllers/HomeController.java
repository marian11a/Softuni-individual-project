package bg.softuni.carsHeaven.web.controllers;

import bg.softuni.carsHeaven.model.dtos.cars.ReadCarDataDTO;
import bg.softuni.carsHeaven.model.dtos.cars.ReadModelsDTO;
import bg.softuni.carsHeaven.service.CarDataService;
import bg.softuni.carsHeaven.service.ModelService;
import bg.softuni.carsHeaven.service.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class HomeController {

    private final UserService userService;
    private final ModelService modelService;
    private final CarDataService carDataService;

    public HomeController(UserService userService,
                          ModelService modelService,
                          CarDataService carDataService) {
        this.userService = userService;
        this.modelService = modelService;
        this.carDataService = carDataService;
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

    @GetMapping("/compare/{detail1Id}/compare-to/{detail2Id}")
    public ModelAndView comparingTwoCars(@PathVariable("detail1Id") Long detail1Id,
                                         @PathVariable("detail2Id") Long detail2Id) {
        ModelAndView modelAndView = new ModelAndView("comparing-two-cars");
        ReadCarDataDTO details1 = this.carDataService.getDetailsById(detail1Id);
        ReadCarDataDTO details2 = this.carDataService.getDetailsById(detail2Id);

        ReadModelsDTO model1 = this.modelService.getModelById(details1.getModelId());
        ReadModelsDTO model2 = this.modelService.getModelById(details2.getModelId());

        modelAndView.addObject("details1", details1);
        modelAndView.addObject("model1", model1);
        modelAndView.addObject("details2", details2);
        modelAndView.addObject("model2", model2);
        return modelAndView;
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
