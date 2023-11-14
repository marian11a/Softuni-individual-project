package bg.softuni.carsHeaven.web.controllers;

import bg.softuni.carsHeaven.security.LoggedUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    private final LoggedUser loggedUser;

    public HomeController(LoggedUser loggedUser) {
        this.loggedUser = loggedUser;
    }

    //todo home controller fav.  compare. and profile!!!!!! remake database
    //todo make users have a relation to models so it can display all fav cars for each user

    @GetMapping("/")
    public ModelAndView index() {
        if (loggedUser.isLogged()) {
            return new ModelAndView("redirect:/home");
        }
        return new ModelAndView("index");
    }

    @GetMapping("/home")
    public ModelAndView home() {
        if (!loggedUser.isLogged()) {
            return new ModelAndView("redirect:/");
        }

//        HomeDTO homeData = this.wordService.getHomeData();
//        return new ModelAndView("home", "homeDTO" , homeData);

        return new ModelAndView("home");
    }
}
