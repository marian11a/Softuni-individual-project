package bg.softuni.carsHeaven.service.impl;

import bg.softuni.carsHeaven.model.dtos.cars.ReadModelsDTO;
import bg.softuni.carsHeaven.model.entity.Brand;
import bg.softuni.carsHeaven.model.entity.Model;
import bg.softuni.carsHeaven.model.entity.User;
import bg.softuni.carsHeaven.model.enums.CarCategory;
import bg.softuni.carsHeaven.repository.BrandRepository;
import bg.softuni.carsHeaven.repository.ModelRepository;
import bg.softuni.carsHeaven.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ModelServiceImplTest {

    @Mock
    private ModelRepository modelRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BrandRepository brandRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ModelServiceImpl modelService;

    public ModelServiceImplTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllModelsByBrand() {
        Long brandId = 1L;

        Brand brand = new Brand();
        brand.setId(brandId);
        brand.setName("Brand");

        Model model1 = new Model();
        model1.setId(1L);
        model1.setName("Model1");
        model1.setImageUrl("image1");
        model1.setBrand(brand);

        Model model2 = new Model();
        model2.setId(2L);
        model2.setName("Model2");
        model2.setImageUrl("image2");
        model2.setBrand(brand);

        List<Model> models = Arrays.asList(model1, model2);

        when(brandRepository.findById(brandId)).thenReturn(Optional.of(brand));
        when(modelRepository.findAllByBrand(brand)).thenReturn(models);

        List<ReadModelsDTO> result = modelService.getAllModelsByBrand(brandId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Model1", result.get(0).getName());
        assertEquals("Model2", result.get(1).getName());
    }

    @Test
    public void testGetAllModelsByBrandWithInvalidBrandId() {
        Long invalidBrandId = 99L;
        when(brandRepository.findById(invalidBrandId)).thenReturn(Optional.empty());

        List<ReadModelsDTO> result = modelService.getAllModelsByBrand(invalidBrandId);
        assertNull(result);
    }

    @Test
    public void testGetModelById() {
        Long modelId = 1L;
        Model model = new Model();
        model.setId(modelId);
        model.setName("Model");
        model.setImageUrl("image");
        model.setCategory(CarCategory.Sedan);
        Brand brand = new Brand();
        brand.setName("Brand");
        model.setBrand(brand);

        when(modelRepository.findById(modelId)).thenReturn(Optional.of(model));
        when(modelMapper.map(model, ReadModelsDTO.class)).thenReturn(createSampleReadModelsDTO());

        ReadModelsDTO result = modelService.getModelById(modelId);

        assertNotNull(result);
        assertEquals("Model", result.getName());
        assertEquals("image", result.getImageUrl());
        assertEquals("Brand", result.getBrandName());
        assertEquals("Sedan", result.getCategory());
    }

    @Test
    public void testGetModelByIdWithInvalidModelId() {
        Long invalidModelId = 99L;
        when(modelRepository.findById(invalidModelId)).thenReturn(Optional.empty());

        ReadModelsDTO result = modelService.getModelById(invalidModelId);
        assertNull(result);
    }

    @Test
    public void testRemoveModel() {
        Long modelId = 1L;
        Model model = new Model();
        model.setId(modelId);
        User user = new User();
        user.setFavoriteCars(Collections.singletonList(model));

        when(userRepository.findByFavoriteCars_Id(modelId)).thenReturn(new ArrayList<>());
        when(modelRepository.findById(modelId)).thenReturn(Optional.of(model));

        modelService.removeModel(modelId);
        verify(modelRepository, times(1)).deleteById(modelId);
    }

    @Test
    public void testRemoveModelWithInvalidModelId() {
        Long invalidModelId = 99L;
        when(modelRepository.findById(invalidModelId)).thenReturn(Optional.empty());

        verify(userRepository, never()).save(any());
        verify(modelRepository, never()).deleteById(any());
    }

    @Test
    public void testEditModel() {
        ReadModelsDTO readModelsDTO = createSampleReadModelsDTO();
        Model model = new Model();
        model.setId(1L);

        when(modelRepository.findById(readModelsDTO.getId())).thenReturn(Optional.of(model));
        when(modelRepository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);

        boolean result = modelService.edit(readModelsDTO);
        assertTrue(result);
    }

    @Test
    public void testEditModelWithInvalidModelId() {
        ReadModelsDTO readModelsDTO = createSampleReadModelsDTO();
        when(modelRepository.findById(readModelsDTO.getId())).thenReturn(Optional.empty());

        boolean result = modelService.edit(readModelsDTO);
        assertFalse(result);
    }

    @Test
    public void testGetBrandIdByModelId() {
        Long modelId = 1L;
        Model model = new Model();
        model.setId(modelId);
        Brand brand = new Brand();
        brand.setId(2L);
        model.setBrand(brand);

        when(modelRepository.findById(modelId)).thenReturn(Optional.of(model));
        Long result = modelService.getBrandIdByModelId(modelId);

        assertNotNull(result);
        assertEquals(2L, result);
    }

    @Test
    public void testGetBrandIdByModelIdWithInvalidModelId() {
        Long invalidModelId = 99L;
        when(modelRepository.findById(invalidModelId)).thenReturn(Optional.empty());

        Long result = modelService.getBrandIdByModelId(invalidModelId);
        assertNull(result);
    }

    @Test
    public void testGetAllModels() {
        Model model1 = new Model();
        model1.setId(1L);
        model1.setName("Model1");

        Model model2 = new Model();
        model2.setId(2L);
        model2.setName("Model2");

        when(modelRepository.findAll()).thenReturn(Arrays.asList(model1, model2));
        List<Model> result = modelService.getAllModels();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Model1", result.get(0).getName());
        assertEquals("Model2", result.get(1).getName());
    }

    private ReadModelsDTO createSampleReadModelsDTO() {
        ReadModelsDTO readModelsDTO = new ReadModelsDTO();
        readModelsDTO.setId(1L);
        readModelsDTO.setName("Model");
        readModelsDTO.setImageUrl("image");
        readModelsDTO.setBrandName("Brand");
        readModelsDTO.setCategory("Sedan");
        return readModelsDTO;
    }
}
