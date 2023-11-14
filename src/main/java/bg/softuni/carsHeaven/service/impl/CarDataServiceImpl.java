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
import bg.softuni.carsHeaven.service.CarDataService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CarDataServiceImpl implements CarDataService {

    private final CarDataRepository carDataRepository;
    private final ModelRepository modelRepository;
    private final EngineRepository engineRepository;
    private final PerformanceRepository performanceRepository;
    private final TransmissionRepository transmissionRepository;
    private final ModelMapper modelMapper;

    public CarDataServiceImpl(CarDataRepository carDataRepository,
                              ModelRepository modelRepository,
                              EngineRepository engineRepository,
                              PerformanceRepository performanceRepository,
                              TransmissionRepository transmissionRepository,
                              ModelMapper modelMapper) {
        this.carDataRepository = carDataRepository;
        this.modelRepository = modelRepository;
        this.engineRepository = engineRepository;
        this.performanceRepository = performanceRepository;
        this.transmissionRepository = transmissionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ReadCarDataDTO> getAllDetailsForModel(Long modelId) {
        Optional<Model> byId = this.modelRepository.findById(modelId);
        if (byId.isEmpty()) {
            return null;
        }
        Model model = byId.get();

        List<ReadCarDataDTO> details = new ArrayList<>();
        this.carDataRepository.findAllByModel(model).forEach(d -> {
            ReadCarDataDTO detailsDTO = new ReadCarDataDTO();
            detailsDTO.setId(d.getId());
            detailsDTO.setModelId(d.getModel().getId());
            detailsDTO.setEngine(this.modelMapper.map(d.getEngine(), ReadEngineDTO.class));
            if (detailsDTO.getEngine() != null) {
                if (detailsDTO.getEngine().getFuel() != null) {
                    detailsDTO.getEngine().setFuel(d.getEngine().getFuel().getDisplayName());
                }
            }
            detailsDTO.setPerformance(this.modelMapper.map(d.getPerformance(), ReadPerformanceDTO.class));
            detailsDTO.setTransmission(this.modelMapper.map(d.getTransmission(), ReadTransmissionDTO.class));

            if (d.getTransmission() != null) {
                if (d.getTransmission().getTransmissionType() != null) {
                    ReadTransmissionDTO transmission = detailsDTO.getTransmission();
                    transmission.setTransmissionType(d.getTransmission().getTransmissionType().getDisplayName());
                }
            }

            details.add(detailsDTO);
        });

        return details;
    }

    @Override
    public ReadCarDataDTO getDetailsForModelByDetailId(Long modelId, Long detailId) {
        List<ReadCarDataDTO> allDetailsForModel = getAllDetailsForModel(modelId);

        return allDetailsForModel
                .stream()
                .filter(readCarDataDTO -> readCarDataDTO.getId().equals(detailId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean addDetailsForModel(Long modelId, ReadCarDataDTO readCarDataDTO) {
        Optional<Model> byId = this.modelRepository.findById(modelId);
        if (byId.isEmpty()) {
            return false;
        }
        Model model = byId.get();

        Engine engine = this.modelMapper.map(readCarDataDTO.getEngine(), Engine.class);
        if (engine.getFuel() == null && engine.getSize() == null && engine.getCylinders() == null && engine.getHorsePower() == null) {
            return false;
        } else {
            if (readCarDataDTO.getEngine().getFuel() != null) {
                Arrays.stream(FuelType.values())
                        .filter(fuel -> fuel.getDisplayName().equals(readCarDataDTO.getEngine().getFuel()))
                        .forEach(engine::setFuel);
            }

            Performance performance = new Performance();
            if (readCarDataDTO.getPerformance() != null) {
                performance = this.modelMapper.map(readCarDataDTO.getPerformance(), Performance.class);
            }

            Transmission transmission = new Transmission();
            if (readCarDataDTO.getTransmission() != null) {
                Arrays.stream(DriveType.values())
                        .filter(value -> value.toString().equals(readCarDataDTO.getTransmission().getDriveType()))
                        .forEach(transmission::setDriveType);

                Arrays.stream(TransmissionType.values())
                        .filter(value -> value.getDisplayName().equals(readCarDataDTO.getTransmission().getTransmissionType()))
                        .forEach(transmission::setTransmissionType);

                transmission.setNumberOfGears(readCarDataDTO.getTransmission().getNumberOfGears());
            }

            CarData carData = new CarData(engine, performance, transmission, model);
            this.engineRepository.save(engine);
            this.performanceRepository.save(performance);
            this.transmissionRepository.save(transmission);
            this.carDataRepository.save(carData);
            return true;
        }
    }

    @Override
    public void removeById(Long detailId) {
        this.carDataRepository.deleteById(detailId);
    }
}
