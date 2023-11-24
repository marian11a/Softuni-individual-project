package bg.softuni.carsHeaven.service.impl;

import bg.softuni.carsHeaven.model.dtos.cars.ReadCarDataDTO;
import bg.softuni.carsHeaven.model.dtos.cars.ReadModelsDTO;
import bg.softuni.carsHeaven.model.entity.*;
import bg.softuni.carsHeaven.model.enums.CarCategory;
import bg.softuni.carsHeaven.model.enums.DriveType;
import bg.softuni.carsHeaven.model.enums.FuelType;
import bg.softuni.carsHeaven.model.enums.TransmissionType;
import bg.softuni.carsHeaven.repository.*;
import bg.softuni.carsHeaven.service.ModelService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ModelServiceImpl implements ModelService {

    private final ModelRepository modelRepository;
    private final UserRepository userRepository;
    private final BrandRepository brandRepository;
    private final TransmissionRepository transmissionRepository;
    private final EngineRepository engineRepository;
    private final CarDataRepository carDataRepository;
    private final PerformanceRepository performanceRepository;
    private final ModelMapper modelMapper;

    public ModelServiceImpl(ModelRepository modelRepository,
                            UserRepository userRepository,
                            BrandRepository brandRepository,
                            TransmissionRepository transmissionRepository,
                            EngineRepository engineRepository,
                            CarDataRepository carDataRepository,
                            PerformanceRepository performanceRepository,
                            ModelMapper modelMapper) {
        this.modelRepository = modelRepository;
        this.userRepository = userRepository;
        this.brandRepository = brandRepository;
        this.transmissionRepository = transmissionRepository;
        this.engineRepository = engineRepository;
        this.carDataRepository = carDataRepository;
        this.performanceRepository = performanceRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ReadModelsDTO> getAllModelsByBrand(Long brandId) {
        Optional<Brand> byId = this.brandRepository.findById(brandId);
        if (byId.isEmpty()) {
            return null;
        }
        Brand brand = byId.get();

        List<ReadModelsDTO> allModelsDTO = new ArrayList<>();
        this.modelRepository.findAllByBrand(brand).forEach(model -> {
            ReadModelsDTO readModelsDTO = new ReadModelsDTO();
            readModelsDTO.setName(model.getName());
            readModelsDTO.setId(model.getId());
            readModelsDTO.setImageUrl(model.getImageUrl());
            readModelsDTO.setBrandName(brand.getName());
            allModelsDTO.add(readModelsDTO);
        });

        allModelsDTO.sort(Comparator.comparing(ReadModelsDTO::getName));
        return allModelsDTO;
    }

    @Override
    public boolean add(ReadModelsDTO readModelsDTO) {
        if (this.brandRepository.findByName(readModelsDTO.getBrandName()).isEmpty()) {
            if (!readModelsDTO.getBrandName().isEmpty()) {
                Brand brand = new Brand();
                brand.setName(readModelsDTO.getBrandName());
                this.brandRepository.save(brand);
            } else {
                return false;
            }
        }

        Model model = new Model();
        model.setBrand(this.brandRepository.findByName(readModelsDTO.getBrandName()).get());
        model.setName(readModelsDTO.getName());
        model.setImageUrl(readModelsDTO.getImageUrl());
        if (readModelsDTO.getCategory() != null) {
            Arrays.stream(CarCategory.values())
                    .filter(value -> value.getDisplayName().toLowerCase().equals(readModelsDTO.getCategory().toLowerCase().trim()))
                    .forEach(model::setCategory);
        } else {
            model.setCategory(null);
        }

        if (readModelsDTO.getCarData() != null || !readModelsDTO.getCarData().isEmpty()) {
            if (readModelsDTO.getCarData().get(0).getEngine().getCylinders() == null &&
                    readModelsDTO.getCarData().get(0).getEngine().getSize() == null) {
                this.modelRepository.save(model);
            } else if (readModelsDTO.getCarData().get(0).getEngine().getCylinders() != null &&
                    readModelsDTO.getCarData().get(0).getEngine().getSize() != null) {
                this.modelRepository.save(model);
            } else {
                return false;
            }
        } else {
            this.modelRepository.save(model);
        }

        List<CarData> carDataList = mapDTODetailsToCarData(readModelsDTO, model);
        model.setCarData(carDataList);
        this.modelRepository.save(model);
        return true;
    }

    @Override
    public ReadModelsDTO getModelById(Long modelId) {
        Optional<Model> byId = this.modelRepository.findById(modelId);
        if (byId.isPresent()) {
            Model model = byId.get();
            ReadModelsDTO readModelsDTO = new ReadModelsDTO();
            readModelsDTO.setName(model.getName());
            readModelsDTO.setImageUrl(model.getImageUrl());
            readModelsDTO.setId(model.getId());
            if (model.getCategory() != null) {
                readModelsDTO.setCategory(model.getCategory().getDisplayName());
            }
            readModelsDTO.setBrandName(model.getBrand().getName());
            return readModelsDTO;
        }
        return null;
    }

    @Override
    public void removeModel(Long id) {
        List<User> users = this.userRepository.findByFavoriteCars_Id(id);
        for (User user : users) {
            user.getFavoriteCars().removeIf(model -> model.getId().equals(id));
            this.userRepository.save(user);
        }

        Optional<Model> byId = this.modelRepository.findById(id);
        if (byId.isPresent()) {
            Model model = byId.get();
            model.setCarData(null);
            model.setBrand(null);
            this.modelRepository.save(model);
            this.modelRepository.deleteById(id);
        }
    }

    @Override
    public boolean edit(ReadModelsDTO readModelsDTO) {
        Optional<Model> byId = this.modelRepository.findById(readModelsDTO.getId());
        if (byId.isEmpty()) {
            return false;
        }

        Model model = byId.get();
        model.setImageUrl(readModelsDTO.getImageUrl());
        if (readModelsDTO.getName() != null) {
            model.setName(readModelsDTO.getName());
        }

        Arrays.stream(CarCategory.values())
                .filter(value -> value.getDisplayName().equalsIgnoreCase(readModelsDTO.getCategory()))
                .forEach(model::setCategory);

        this.modelRepository.save(model);
        return true;
    }

    @Override
    public Long getBrandIdByModelId(Long modelId) {
        Optional<Model> byId = this.modelRepository.findById(modelId);
        if (byId.isEmpty()) {
            return null;
        }
        Model model = byId.get();
        return model.getBrand().getId();
    }

    @Override
    public List<Model> getAllModels() {
        return this.modelRepository.findAll();
    }

    @Override
    public List<ReadModelsDTO> getAllModelsWithDetailsByBrand(Long brandId) {
        List<ReadModelsDTO> allModelsByBrand = getAllModelsByBrand(brandId);

        allModelsByBrand.removeIf(
                readModelsDTO ->
                        this.modelRepository.findById(readModelsDTO.getId()).get().getCarData() == null
                                ||
                                this.modelRepository.findById(readModelsDTO.getId()).get().getCarData().isEmpty()
        );

        return allModelsByBrand;
    }

    private List<CarData> mapDTODetailsToCarData(ReadModelsDTO readModelsDTO, Model model) {
        List<CarData> carDataList = new ArrayList<>();
        for (ReadCarDataDTO data : readModelsDTO.getCarData()) {
            Engine engine = this.modelMapper.map(data.getEngine(), Engine.class);
            if (engine.getFuel() == null && engine.getSize() == null && engine.getCylinders() == null && engine.getHorsePower() == null) {
                break;
            } else {
                if (data.getEngine().getFuel() != null) {
                    Arrays.stream(FuelType.values())
                            .filter(fuel -> fuel.getDisplayName().equals(data.getEngine().getFuel()))
                            .forEach(engine::setFuel);
                }

                Performance performance = new Performance();
                if (data.getPerformance() != null) {
                    performance = this.modelMapper.map(data.getPerformance(), Performance.class);
                }

                Transmission transmission = new Transmission();
                if (data.getTransmission() != null) {
                    Arrays.stream(DriveType.values())
                            .filter(value -> value.toString().equals(data.getTransmission().getDriveType()))
                            .forEach(transmission::setDriveType);

                    Arrays.stream(TransmissionType.values())
                            .filter(value -> value.getDisplayName().equals(data.getTransmission().getTransmissionType()))
                            .forEach(transmission::setTransmissionType);

                    transmission.setNumberOfGears(data.getTransmission().getNumberOfGears());
                }

                CarData carData = new CarData(engine, performance, transmission, model);
                carDataList.add(carData);

                this.engineRepository.save(engine);
                this.performanceRepository.save(performance);
                this.transmissionRepository.save(transmission);
                this.carDataRepository.save(carData);
            }
        }
        return carDataList;
    }
}
