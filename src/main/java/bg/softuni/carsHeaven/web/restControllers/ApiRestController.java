package bg.softuni.carsHeaven.web.restControllers;

import bg.softuni.carsHeaven.model.dtos.cars.ReadBrandsDTO;
import bg.softuni.carsHeaven.model.dtos.cars.ReadCarDataDTO;
import bg.softuni.carsHeaven.model.dtos.cars.ReadModelsDTO;
import bg.softuni.carsHeaven.service.BrandService;
import bg.softuni.carsHeaven.service.CarDataService;
import bg.softuni.carsHeaven.service.ModelService;
import bg.softuni.carsHeaven.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost")
public class ApiRestController {
    private final BrandService brandService;
    private final ModelService modelService;
    private final CarDataService carDataService;
    private final UserService userService;

    public ApiRestController(BrandService brandService,
                             ModelService modelService,
                             CarDataService carDataService,
                             UserService userService) {
        this.brandService = brandService;
        this.modelService = modelService;
        this.carDataService = carDataService;
        this.userService = userService;
    }

    @PostMapping("/remove-model")
    public ResponseEntity<String> removeModel(@RequestParam Long modelId) {
        modelService.removeModel(modelId);
        return ResponseEntity.ok("Model removed successfully");
    }

    @PostMapping("/add-to-favorites")
    public ResponseEntity<String> addToFavorites(@RequestParam Long modelId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        this.userService.addToFavorites(username, modelId);
        return ResponseEntity.ok("Model added to favorites successfully");
    }

    @PostMapping("/remove-from-favorites")
    public ResponseEntity<String> removeFromFavorites(@RequestParam Long modelId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        this.userService.removeFromFavorites(username, modelId);
        return ResponseEntity.ok("Model removed from favorites successfully");
    }

    @GetMapping("/brands")
    @ResponseBody
    public ResponseEntity<List<ReadBrandsDTO>> getAllBrands() {
        List<ReadBrandsDTO> brands = brandService.getAllBrands();
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/brands/{brandId}/models")
    @ResponseBody
    public ResponseEntity<List<ReadModelsDTO>> getModelsByBrand(@PathVariable Long brandId) {
        List<ReadModelsDTO> models = modelService.getAllModelsWithDetailsByBrand(brandId);
        return ResponseEntity.ok(models);
    }


    @GetMapping("/models/{modelId}/details")
    @ResponseBody
    public ResponseEntity<List<ReadCarDataDTO>> getDetailsByModel(@PathVariable Long modelId) {
        List<ReadCarDataDTO> details = this.carDataService.getAllDetailsForModel(modelId);
        return ResponseEntity.ok(details);
    }

}
