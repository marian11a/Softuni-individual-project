package bg.softuni.carsHeaven.web.restControllers;

import bg.softuni.carsHeaven.model.dtos.cars.ReadBrandsDTO;
import bg.softuni.carsHeaven.model.dtos.cars.ReadCarDataDTO;
import bg.softuni.carsHeaven.model.dtos.cars.ReadModelsDTO;
import bg.softuni.carsHeaven.service.BrandService;
import bg.softuni.carsHeaven.service.CarDataService;
import bg.softuni.carsHeaven.service.ModelService;
import bg.softuni.carsHeaven.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
@WebMvcTest(ApiRestController.class)
public class ApiRestControllerTests {

    @MockBean
    private BrandService brandService;

    @MockBean
    private ModelService modelService;

    @MockBean
    private CarDataService carDataService;

    @MockBean
    private UserService userService;

    @Autowired
    private ApiRestController apiRestController;

    @Test
    public void testRemoveModel() {
        Long modelId = 1L;
        ResponseEntity<String> response = apiRestController.removeModel(modelId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(modelService, times(1)).removeModel(modelId);
    }

    @Test
    public void testMakeAdmin() {
        Long userId = 1L;
        ResponseEntity<String> response = apiRestController.makeAdmin(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).makeAdmin(userId);
    }

    @Test
    public void testRemoveAdmin() {
        Long userId = 1L;
        ResponseEntity<String> response = apiRestController.remove(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).removeAdmin(userId);
    }


    @Test
    public void testGetAllBrands() {
        List<ReadBrandsDTO> brands = new ArrayList<>();
        when(brandService.getAllBrands()).thenReturn(brands);
        ResponseEntity<List<ReadBrandsDTO>> response = apiRestController.getAllBrands();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(brands, response.getBody());
    }

    @Test
    public void testGetModelsByBrand() {
        Long brandId = 1L;
        List<ReadModelsDTO> models = new ArrayList<>();

        when(modelService.getAllModelsWithDetailsByBrand(brandId)).thenReturn(models);
        ResponseEntity<List<ReadModelsDTO>> response = apiRestController.getModelsByBrand(brandId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(models, response.getBody());
    }

    @Test
    public void testGetDetailsByModel() {
        Long modelId = 1L;
        List<ReadCarDataDTO> details = new ArrayList<>();

        when(carDataService.getAllDetailsForModel(modelId)).thenReturn(details);
        ResponseEntity<List<ReadCarDataDTO>> response = apiRestController.getDetailsByModel(modelId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(details, response.getBody());
    }
}
