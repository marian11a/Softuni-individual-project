package bg.softuni.carsHeaven.web.controllers;

import bg.softuni.carsHeaven.model.dtos.cars.ReadBrandsDTO;
import bg.softuni.carsHeaven.model.dtos.cars.ReadModelsDTO;
import bg.softuni.carsHeaven.model.enums.CarCategory;
import bg.softuni.carsHeaven.model.enums.DriveType;
import bg.softuni.carsHeaven.model.enums.FuelType;
import bg.softuni.carsHeaven.model.enums.TransmissionType;
import bg.softuni.carsHeaven.security.LoggedUser;
import bg.softuni.carsHeaven.service.BrandService;
import bg.softuni.carsHeaven.service.ModelService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/models")
public class ModelController {

    private final LoggedUser loggedUser;

    private final ModelService modelService;
    private final BrandService brandService;


    public ModelController(LoggedUser loggedUser,
                           ModelService modelService,
                           BrandService brandService) {
        this.loggedUser = loggedUser;
        this.modelService = modelService;
        this.brandService = brandService;
    }


    @GetMapping("/{brandId}")
    public ModelAndView modelsByBrand(@PathVariable("brandId") Long brandId) {
        if (!loggedUser.isLogged()) {
            return new ModelAndView("redirect:/");
        }
        ModelAndView modelAndView = new ModelAndView("all-brand-models");


        //todo ako brand nqma modeli da go opravq da pishe che nqma

        List<ReadModelsDTO> allModelsByBrand = this.modelService.getAllModelsByBrand(brandId);
        ReadBrandsDTO brandById = this.brandService.getBrandById(brandId);

        modelAndView.addObject("allModelsByBrand" , allModelsByBrand);
        modelAndView.addObject("brand" , brandById);

        return modelAndView;
    }

    @GetMapping("/add-car")
    public ModelAndView addCar(@ModelAttribute("readModelsDTO") ReadModelsDTO readModelsDTO) {
        if (!loggedUser.isLogged()) {
            return new ModelAndView("redirect:/");
        }
        return getParametersForAddingACar();
    }

    @PostMapping("/add-car")
    public ModelAndView addCar(
            @ModelAttribute("readModelsDTO") @Valid ReadModelsDTO readModelsDTO,
            BindingResult bindingResult) {
        if (!loggedUser.isLogged()) {
            return new ModelAndView("redirect:/");
        }
        if (bindingResult.hasErrors()) {
            return getParametersForAddingACar();
        }
        boolean addedSuccessfully = modelService.add(readModelsDTO);
        if (!addedSuccessfully) {
            ModelAndView modelAndView = getParametersForAddingACar();
            modelAndView.addObject("hasAddError", true);
            return modelAndView;
        }
        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/remove/{brandId}/{modelId}")
    public ModelAndView remove(@PathVariable("modelId") Long modelId) {
        if (!loggedUser.isLogged()) {
            return new ModelAndView("redirect:/");
        }
        this.modelService.removeModel(modelId);
        return new ModelAndView("redirect:/models/{brandId}");
    }


    @GetMapping("/edit/{brandId}/{modelId}")
    public ModelAndView editModel(@PathVariable("modelId") Long modelId,
                                  @PathVariable("brandId") Long brandId) {
        if (!loggedUser.isLogged()) {
            return new ModelAndView("redirect:/");
        }
        ModelAndView modelAndView = new ModelAndView("edit-model");
        ReadModelsDTO readModelsDTO = this.modelService.getModelById(modelId);

        modelAndView.addObject("readModelsDTO", readModelsDTO);
        modelAndView.addObject("allCategories", CarCategory.values());
        modelAndView.addObject("brandId1" , brandId);
        return modelAndView;
    }

    //todo fix the edit model kat dvaputi cukna save changes bez da populvam nishto gurmi vij shto taka

    @PostMapping("/edit/{brandId}/{modelId}")
    public ModelAndView editModel(
            @PathVariable("modelId") Long modelId,
            @PathVariable("brandId") Long brandId,
            @ModelAttribute("readModelsDTO") @Valid ReadModelsDTO readModelsDTO,
            BindingResult bindingResult) {
        if (!loggedUser.isLogged()) {
            return new ModelAndView("redirect:/");
        }

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("edit-model");
            modelAndView.addObject("modelId", modelId);
            modelAndView.addObject("brandId1" , brandId);
            modelAndView.addObject("allCategories", CarCategory.values());
            return modelAndView;
        }

        readModelsDTO.setId(modelId);
        boolean addedSuccessfully = this.modelService.edit(readModelsDTO);
        if (!addedSuccessfully) {
            ModelAndView modelAndView = new ModelAndView("edit-model");
            modelAndView.addObject("hasEditErrors", true);
            modelAndView.addObject("modelId", modelId);
            modelAndView.addObject("brandId1" , brandId);
            modelAndView.addObject("allCategories", CarCategory.values());
            return modelAndView;
        }

        return new ModelAndView("redirect:/models/{brandId}");
    }

    private ModelAndView getParametersForAddingACar() {
        ModelAndView modelAndView = new ModelAndView("add-car");
        List<ReadBrandsDTO> allBrands = this.brandService.getAllBrands();

        modelAndView.addObject("allBrands", allBrands);
        modelAndView.addObject("allCategories", CarCategory.values());
        modelAndView.addObject("allFuels", FuelType.values());
        modelAndView.addObject("allTransmissionTypes", TransmissionType.values());
        modelAndView.addObject("allDriveTypes", DriveType.values());
        return modelAndView;
    }
}
