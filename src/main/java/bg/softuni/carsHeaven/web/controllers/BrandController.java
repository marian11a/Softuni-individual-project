package bg.softuni.carsHeaven.web.controllers;

import bg.softuni.carsHeaven.model.dtos.cars.ReadBrandsDTO;
import bg.softuni.carsHeaven.service.BrandService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/brands")
public class BrandController {  //todo alt-enter create test

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }


    @GetMapping("/all-brands")
    public ModelAndView allBrands() {


        List<ReadBrandsDTO> allBrands = this.brandService.getAllBrands();
        return new ModelAndView("all-brands", "allBrands", allBrands);
    }


    @GetMapping("/edit/{brandId}")
    public ModelAndView edit(@PathVariable("brandId") Long brandId) {

        ModelAndView modelAndView = new ModelAndView("edit-brand");
        ReadBrandsDTO readBrandsDTO = this.brandService.getBrandById(brandId);

        modelAndView.addObject("readBrandsDTO", readBrandsDTO);
        return modelAndView;
    }


    @PostMapping("/edit/{brandId}")
    public ModelAndView edit(
            @PathVariable("brandId") Long brandId,
            @ModelAttribute("readBrandsDTO") @Valid ReadBrandsDTO readBrandsDTO,
            BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("edit-brand");
            modelAndView.addObject("brandId", brandId);
            return modelAndView;
        }

        readBrandsDTO.setId(brandId);
        boolean addedSuccessfully = brandService.edit(readBrandsDTO);
        if (!addedSuccessfully) {
            ModelAndView modelAndView = new ModelAndView("edit-brand");
            modelAndView.addObject("hasEditErrors", true);
            modelAndView.addObject("brandId", brandId);
            return modelAndView;
        }

        return new ModelAndView("redirect:/brands/all-brands");
    }

    @GetMapping("/remove/{brandId}")
    public ModelAndView remove(@PathVariable("brandId") Long brandId) {


        this.brandService.remove(brandId);
        return new ModelAndView("redirect:/brands/all-brands");
    }

    @GetMapping("/add")
    public ModelAndView add(@ModelAttribute("readBrandsDTO") ReadBrandsDTO readBrandsDTO) {

        return new ModelAndView("add-brand");
    }


    @PostMapping("/add")
    public ModelAndView add(
            @ModelAttribute("readBrandsDTO") @Valid ReadBrandsDTO readBrandsDTO,
            BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            return new ModelAndView("add-brand");
        }

        boolean addedSuccessfully = brandService.add(readBrandsDTO);
        if (!addedSuccessfully) {
            ModelAndView modelAndView = new ModelAndView("add-brand");
            modelAndView.addObject("hasEditErrors", true);
            return modelAndView;
        }

        return new ModelAndView("redirect:/models/add-car");
    }
}
