package bg.softuni.carsHeaven.web.restControllers;

import bg.softuni.carsHeaven.service.ModelService;
import bg.softuni.carsHeaven.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost")
public class ModelRestController {
    private final ModelService modelService;

    private final UserService userService;

    public ModelRestController(ModelService modelService, UserService userService) {
        this.modelService = modelService;
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
}
