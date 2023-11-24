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
    public void removeById(Long detailId) {
        CarData carData = this.carDataRepository.findById(detailId).orElse(null);

        if (carData != null) {
            Long performanceId = carData.getPerformance().getId();
            Long engineId = carData.getEngine().getId();
            Long transmissionId = carData.getTransmission().getId();

            carData.setPerformance(null);
            carData.setTransmission(null);
            carData.setEngine(null);
            carData.setModel(null);

            this.carDataRepository.save(carData);

            deleteAssociatedEntities(performanceId, engineId, transmissionId);
            this.carDataRepository.deleteById(detailId);
        }
    }

    private void deleteAssociatedEntities(Long performanceId, Long engineId, Long transmissionId) {
        if (transmissionId != null) { this.transmissionRepository.deleteById(transmissionId);}
        if (performanceId != null) { this.performanceRepository.deleteById(performanceId);}
        if (engineId != null) {this.engineRepository.deleteById(engineId);}
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
            if (d.getEngine() != null) {
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

        if (readCarDataDTO.getEngine().getFuel() == null
                && readCarDataDTO.getEngine().getSize() == null
                && readCarDataDTO.getEngine().getCylinders() == null
                && readCarDataDTO.getEngine().getHorsePower() == null) {
            return false;
        } else {
            Engine engine = new Engine();
            mapToEngine(readCarDataDTO, engine);

            Performance performance = new Performance();
            if (readCarDataDTO.getPerformance() != null) {
                mapToPerformance(readCarDataDTO, performance);
            }

            Transmission transmission = new Transmission();
            if (readCarDataDTO.getTransmission() != null) {
                mapToTransmission(readCarDataDTO, transmission);
            }

            CarData carData = new CarData(engine, performance, transmission, model);
            saveAll(engine, performance, transmission, carData);
            return true;
        }
    }

    private void saveAll(Engine engine,
                         Performance performance,
                         Transmission transmission,
                         CarData carData) {
        this.engineRepository.save(engine);
        this.performanceRepository.save(performance);
        this.transmissionRepository.save(transmission);
        this.carDataRepository.save(carData);
    }

    @Override
    public boolean editDetail(Long detailId, ReadCarDataDTO readCarDataDTO) {
        Optional<CarData> byId = this.carDataRepository.findById(detailId);
        if (byId.isEmpty()) {
            return false;
        }
        CarData carData = byId.get();

        if (readCarDataDTO.getEngine().getFuel() == null
                && readCarDataDTO.getEngine().getSize() == null
                && readCarDataDTO.getEngine().getCylinders() == null
                && readCarDataDTO.getEngine().getHorsePower() == null) {
            return false;
        } else {
            Engine engine = carData.getEngine();
            mapToEngine(readCarDataDTO, engine);

            Performance performance = carData.getPerformance();
            if (readCarDataDTO.getPerformance() != null) {
                mapToPerformance(readCarDataDTO, performance);
            }

            Transmission transmission = carData.getTransmission();
            if (readCarDataDTO.getTransmission() != null) {
                mapToTransmission(readCarDataDTO, transmission);
            }

            carData.setEngine(engine);
            carData.setPerformance(performance);
            carData.setTransmission(transmission);
            saveAll(engine, performance, transmission, carData);
            return true;
        }
    }

    @Override
    public ReadCarDataDTO getDetailsById(Long detail1Id) {
        Optional<CarData> byId = this.carDataRepository.findById(detail1Id);
        if (byId.isEmpty()) {
            return null;
        }
        CarData carData = byId.get();
        return getDetailsForModelByDetailId(carData.getModel().getId(), detail1Id);
    }

    private void mapToEngine(ReadCarDataDTO readCarDataDTO, Engine engine) {
        if (readCarDataDTO.getEngine().getFuel() != null) {
            Arrays.stream(FuelType.values())
                    .filter(fuel -> fuel.getDisplayName().equals(readCarDataDTO.getEngine().getFuel()))
                    .forEach(engine::setFuel);
        } else {
            engine.setFuel(null);
        }
        engine.setCylinders(readCarDataDTO.getEngine().getCylinders());
        engine.setSize(readCarDataDTO.getEngine().getSize());
        engine.setHorsePower(readCarDataDTO.getEngine().getHorsePower());
    }

    private void mapToPerformance(ReadCarDataDTO readCarDataDTO, Performance performance) {
        performance.setTopSpeed(readCarDataDTO.getPerformance().getTopSpeed());
        performance.setAcceleration(readCarDataDTO.getPerformance().getAcceleration());
    }

    private void mapToTransmission(ReadCarDataDTO readCarDataDTO, Transmission transmission) {
        Arrays.stream(DriveType.values())
                .filter(value -> value
                        .toString()
                        .equals(readCarDataDTO.getTransmission().getDriveType()))
                .forEach(transmission::setDriveType);
        if (readCarDataDTO.getTransmission().getDriveType().isEmpty() ||
                readCarDataDTO.getTransmission().getDriveType() == null) {
            transmission.setDriveType(null);
        }

        Arrays.stream(TransmissionType.values())
                .filter(value -> value
                        .getDisplayName()
                        .equals(readCarDataDTO.getTransmission().getTransmissionType()))
                .forEach(transmission::setTransmissionType);
        if (readCarDataDTO.getTransmission().getTransmissionType().isEmpty() ||
                readCarDataDTO.getTransmission().getTransmissionType() == null) {
            transmission.setTransmissionType(null);
        }

        transmission.setNumberOfGears(readCarDataDTO.getTransmission().getNumberOfGears());
    }
}
