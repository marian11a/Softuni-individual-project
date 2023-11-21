package bg.softuni.carsHeaven.web.controllers;

import bg.softuni.carsHeaven.model.dtos.cars.ReadCarDataDTO;
import bg.softuni.carsHeaven.model.dtos.cars.ReadModelsDTO;
import bg.softuni.carsHeaven.model.enums.DriveType;
import bg.softuni.carsHeaven.model.enums.FuelType;
import bg.softuni.carsHeaven.model.enums.TransmissionType;
import bg.softuni.carsHeaven.service.CarDataService;
import bg.softuni.carsHeaven.service.ModelService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/details")
public class DetailsController {

    //todo da ne moga da add detailvam bez dvigatel da sloja !!!!!!!<-------- VAJNO

    //todo compare butona da go centriram i eventualno po golqm da go napravq

    //todo compare stranicata da q napravq

    //todo kato imam favorite car i otida v detailite da ne mi pokazva add to favorites a da mi pokazva remove from favorites

    //todo oshte edno todo da dobavq removeFromFavorites buton

    //todo da napravq compare-a

    //todo da moga da si smenqm parolata v saita
    private final CarDataService carDataService;
    private final ModelService modelService;

    public DetailsController(CarDataService carDataService, ModelService modelService) {
        this.carDataService = carDataService;
        this.modelService = modelService;
    }

    @GetMapping("/{modelId}")
    public ModelAndView allDetailsForModel(@PathVariable("modelId") Long modelId) {

        ModelAndView modelAndView = new ModelAndView("all-details-for-model");
        List<ReadCarDataDTO> allDetailsForModel = this.carDataService.getAllDetailsForModel(modelId);

        ReadModelsDTO modelById = this.modelService.getModelById(modelId);
        Long brandId = this.modelService.getBrandIdByModelId(modelId);
        modelAndView.addObject("model", modelById);
        modelAndView.addObject("brandId1", brandId);
        modelAndView.addObject("details", allDetailsForModel);

        return modelAndView;
    }

    @GetMapping("/{modelId}/add-detail")
    public ModelAndView addDetailForAModel(@ModelAttribute("readCarDataDTO") ReadCarDataDTO readCarDataDTO) {

        ModelAndView modelAndView = new ModelAndView("add-details-for-model");

        modelAndView.addObject("allFuels", FuelType.values());
        modelAndView.addObject("allTransmissionTypes", TransmissionType.values());
        modelAndView.addObject("allDriveTypes", DriveType.values());
        return modelAndView;
    }

    @PostMapping("/{modelId}/add-detail")
    public ModelAndView addDetailForAModel(@PathVariable("modelId") Long modelId,
            @ModelAttribute("readCarDataDTO") @Valid ReadCarDataDTO readCarDataDTO,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("add-details-for-model");
            modelAndView.addObject("modelId", modelId);
            return modelAndView;
        }
        boolean addedSuccessfully = this.carDataService.addDetailsForModel(modelId, readCarDataDTO);
        if (!addedSuccessfully) {
            ModelAndView modelAndView = new ModelAndView("add-details-for-model");
            modelAndView.addObject("hasAddError", true);
            return modelAndView;
        }
        return new ModelAndView("redirect:/details/{modelId}");
    }

    @GetMapping("/{modelId}/{detailId}")
    public ModelAndView detail(@PathVariable("modelId") Long modelId,
                               @PathVariable("detailId") Long detailId) {

        ModelAndView modelAndView = new ModelAndView("details");
        ReadCarDataDTO details = this.carDataService.getDetailsForModelByDetailId(modelId, detailId);

        ReadModelsDTO readModelsDTO = this.modelService.getModelById(modelId);
        modelAndView.addObject("model", readModelsDTO);
        modelAndView.addObject("details", details);

        return modelAndView;
    }

    @GetMapping("/remove/{modelId}/{detailId}")
    public ModelAndView removeDetail(@PathVariable("detailId") Long detailId) {
        this.carDataService.removeById(detailId);
        return new ModelAndView("redirect:/details/{modelId}");
    }

    @GetMapping("/{modelId}/edit-detail/{detailId}")
    public ModelAndView editDetail(@PathVariable("detailId") Long detailId,
                                   @PathVariable("modelId") Long modelId) {

        ModelAndView modelAndView = new ModelAndView("edit-detail");
        ReadCarDataDTO readCarDataDTO = this.carDataService.getDetailsForModelByDetailId(modelId, detailId);

        modelAndView.addObject("readCarDataDTO", readCarDataDTO);
        modelAndView.addObject("allFuels", FuelType.values());
        modelAndView.addObject("allTransmissionTypes", TransmissionType.values());
        modelAndView.addObject("allDriveTypes", DriveType.values());
        return modelAndView;
    }

    @PostMapping("/{modelId}/edit-detail/{detailId}")
    public ModelAndView editDetail(@PathVariable("modelId") Long modelId,
                                   @PathVariable("detailId") Long detailId,
                                   @ModelAttribute("readCarDataDTO") @Valid ReadCarDataDTO readCarDataDTO,
                                   BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("edit-detail");
            modelAndView.addObject("modelId", modelId);
            modelAndView.addObject("detailId", detailId);
            return modelAndView;
        }
        boolean editedSuccessfully = this.carDataService.editDetail(detailId, readCarDataDTO);
        if (!editedSuccessfully) {
            ModelAndView modelAndView = new ModelAndView("edit-detail");
            modelAndView.addObject("modelId", modelId);
            modelAndView.addObject("detailId", detailId);
            modelAndView.addObject("hasEditErrors", true);
            return modelAndView;
        }
        return new ModelAndView("redirect:/details/{modelId}/{detailId}");
    }
}
