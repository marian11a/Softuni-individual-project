package bg.softuni.carsHeaven.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {


    //todo home controller fav.  compare. and profile!!!!!! remake database
    //todo make users have a relation to models so it can display all fav cars for each user

    @GetMapping("/")
    public ModelAndView index() {

        return new ModelAndView("index");
    }

    @GetMapping("/home")
    public ModelAndView home() {

        return new ModelAndView("home");
    }

    @GetMapping("/compare")
    public ModelAndView compare() {

        return new ModelAndView("compare");
    }
}
