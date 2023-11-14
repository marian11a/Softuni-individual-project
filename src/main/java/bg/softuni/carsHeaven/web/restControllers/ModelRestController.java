package bg.softuni.carsHeaven.web.restControllers;

import bg.softuni.carsHeaven.service.ModelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ModelRestController {

    private final ModelService modelService;

    public ModelRestController(ModelService modelService) {
        this.modelService = modelService;
    }

    @PostMapping("/remove-model")
    public ResponseEntity<String> removeModel(@RequestParam Long modelId) {
        modelService.removeModel(modelId);
        return ResponseEntity.ok("Model removed successfully");
    }
}
