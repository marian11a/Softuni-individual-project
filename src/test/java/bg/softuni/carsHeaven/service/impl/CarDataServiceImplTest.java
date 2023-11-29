package bg.softuni.carsHeaven.service.impl;

import bg.softuni.carsHeaven.model.dtos.cars.ReadCarDataDTO;
import bg.softuni.carsHeaven.model.dtos.cars.ReadEngineDTO;
import bg.softuni.carsHeaven.model.dtos.cars.ReadPerformanceDTO;
import bg.softuni.carsHeaven.model.dtos.cars.ReadTransmissionDTO;
import bg.softuni.carsHeaven.model.entity.*;
import bg.softuni.carsHeaven.model.enums.DriveType;
import bg.softuni.carsHeaven.model.enums.FuelType;
import bg.softuni.carsHeaven.model.enums.TransmissionType;
import bg.softuni.carsHeaven.repository.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CarDataServiceImplTest {

    @Mock
    private CarDataRepository carDataRepository;

    @Mock
    private ModelRepository modelRepository;

    @Mock
    private EngineRepository engineRepository;

    @Mock
    private PerformanceRepository performanceRepository;

    @Mock
    private TransmissionRepository transmissionRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CarDataServiceImpl carDataService;

    public CarDataServiceImplTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRemoveById() {
        Long detailId = 1L;
        CarData carData = createTestCarData(detailId);
        when(carDataRepository.findById(detailId)).thenReturn(Optional.of(carData));

        carDataService.removeById(detailId);
        verify(carDataRepository, times(1)).deleteById(detailId);
    }

    @Test
    public void testRemoveByIdWithNonExistingDetail() {
        Long nonExistingDetailId = 999L;
        when(carDataRepository.findById(nonExistingDetailId)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> carDataService.removeById(nonExistingDetailId));
    }

    @Test
    public void testGetNoDetailsForModel() {
        Long modelId = 1L;
        Model model = createTestModel(modelId);
        when(modelRepository.findById(modelId)).thenReturn(Optional.of(model));

        List<ReadCarDataDTO> result = carDataService.getAllDetailsForModel(modelId);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetAllDetailsForModelWithNonExistingModel() {
        Long nonExistingModelId = 999L;
        when(modelRepository.findById(nonExistingModelId)).thenReturn(Optional.empty());

        List<ReadCarDataDTO> result = carDataService.getAllDetailsForModel(nonExistingModelId);
        assertNull(result);
    }

    @Test
    public void testAddDetailsForModel() {
        Long modelId = 1L;
        Model model = createTestModel(modelId);
        ReadCarDataDTO readCarDataDTO = createTestReadCarDataDTO(1L);
        when(modelRepository.findById(modelId)).thenReturn(Optional.of(model));

        boolean result = carDataService.addDetailsForModel(modelId, readCarDataDTO);
        assertTrue(result);
    }

    @Test
    public void testAddDetailsForModelWithNonExistingModel() {
        Long nonExistingModelId = 999L;
        ReadCarDataDTO readCarDataDTO = createTestReadCarDataDTO(1L);
        when(modelRepository.findById(nonExistingModelId)).thenReturn(Optional.empty());

        boolean result = carDataService.addDetailsForModel(nonExistingModelId, readCarDataDTO);
        assertFalse(result);
    }

    @Test
    public void testEditDetail() {
        Long detailId = 1L;
        CarData carData = createTestCarData(detailId);
        ReadCarDataDTO readCarDataDTO = createTestReadCarDataDTO(detailId);
        when(carDataRepository.findById(detailId)).thenReturn(Optional.of(carData));

        boolean result = carDataService.editDetail(detailId, readCarDataDTO);
        assertTrue(result);
    }

    @Test
    public void testEditDetailWithNonExistingDetail() {
        Long nonExistingDetailId = 999L;
        ReadCarDataDTO readCarDataDTO = createTestReadCarDataDTO(nonExistingDetailId);
        when(carDataRepository.findById(nonExistingDetailId)).thenReturn(Optional.empty());

        boolean result = carDataService.editDetail(nonExistingDetailId, readCarDataDTO);
        assertFalse(result);
    }

    @Test
    public void testGetDetailsByIdWithNonExistingDetail() {
        Long nonExistingDetailId = 999L;
        when(carDataRepository.findById(nonExistingDetailId)).thenReturn(Optional.empty());

        ReadCarDataDTO result = carDataService.getDetailsById(nonExistingDetailId);
        assertNull(result);
    }

    private CarData createTestCarData(Long id) {
        CarData carData = new CarData();
        carData.setId(id);

        Engine engine = new Engine();
        engine.setId(1L);
        engine.setFuel(FuelType.GASOLINE);
        engine.setSize(2.0);
        engine.setCylinders(4);
        engine.setHorsePower(200);

        Performance performance = new Performance();
        performance.setId(1L);
        performance.setTopSpeed(220);
        performance.setAcceleration(7.5);

        Transmission transmission = new Transmission();
        transmission.setId(1L);
        transmission.setDriveType(DriveType.FWD);
        transmission.setTransmissionType(TransmissionType.AUTOMATIC);
        transmission.setNumberOfGears(8);

        carData.setEngine(engine);
        carData.setPerformance(performance);
        carData.setTransmission(transmission);
        carData.setModel(createTestModel(1L));

        return carData;
    }

    private Model createTestModel(Long id) {
        Model model = new Model();
        model.setId(id);
        model.setName("Test Model");
        model.setBrand(new Brand());
        return model;
    }

    private ReadCarDataDTO createTestReadCarDataDTO(Long id) {
        ReadCarDataDTO readCarDataDTO = new ReadCarDataDTO();
        readCarDataDTO.setId(id);
        readCarDataDTO.setModelId(1L);

        ReadEngineDTO engineDTO = new ReadEngineDTO();
        engineDTO.setFuel(FuelType.GASOLINE.getDisplayName());
        engineDTO.setSize(2.0);
        engineDTO.setCylinders(4);
        engineDTO.setHorsePower(200);

        ReadPerformanceDTO performanceDTO = new ReadPerformanceDTO();
        performanceDTO.setTopSpeed(220);
        performanceDTO.setAcceleration(7.5);

        ReadTransmissionDTO transmissionDTO = new ReadTransmissionDTO();
        transmissionDTO.setDriveType(DriveType.FWD.toString());
        transmissionDTO.setTransmissionType(TransmissionType.AUTOMATIC.getDisplayName());
        transmissionDTO.setNumberOfGears(8);

        readCarDataDTO.setEngine(engineDTO);
        readCarDataDTO.setPerformance(performanceDTO);
        readCarDataDTO.setTransmission(transmissionDTO);

        return readCarDataDTO;
    }
}
