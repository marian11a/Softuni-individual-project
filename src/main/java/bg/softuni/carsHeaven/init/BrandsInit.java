package bg.softuni.carsHeaven.init;

import bg.softuni.carsHeaven.model.dtos.cars.ReadBrandsDTO;
import bg.softuni.carsHeaven.model.entity.*;
import bg.softuni.carsHeaven.model.enums.CarCategory;
import bg.softuni.carsHeaven.model.enums.DriveType;
import bg.softuni.carsHeaven.model.enums.TransmissionType;
import bg.softuni.carsHeaven.repository.*;
import com.google.gson.Gson;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class BrandsInit implements CommandLineRunner {

    private static final String BRANDS_FILE_PATH = "src/main/resources/json/car-list.json";

    private final BrandRepository brandRepository;
    private final EngineRepository engineRepository;
    private final CarDataRepository carDataRepository;
    private final TransmissionRepository transmissionRepository;
    private final PerformanceRepository performanceRepository;
    private final Gson gson;

    private final ModelRepository modelRepository;

    public BrandsInit(BrandRepository brandRepository,
                      EngineRepository engineRepository,
                      CarDataRepository carDataRepository,
                      TransmissionRepository transmissionRepository,
                      PerformanceRepository performanceRepository,
                      Gson gson,
                      ModelRepository modelRepository) {
        this.brandRepository = brandRepository;
        this.engineRepository = engineRepository;
        this.carDataRepository = carDataRepository;
        this.transmissionRepository = transmissionRepository;
        this.performanceRepository = performanceRepository;
        this.gson = gson;
        this.modelRepository = modelRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (modelRepository.count() <= 0 || brandRepository.count() <= 0) {
            String carsJson = Files.readString(Path.of(BRANDS_FILE_PATH));
            List<ReadBrandsDTO> brandsDTOS = Arrays
                    .stream(gson.fromJson(carsJson, ReadBrandsDTO[].class))
                    .toList();

            insertDataIntoDB(brandsDTOS);
        }
    }

    private void insertDataIntoDB(List<ReadBrandsDTO> brandsDTOS) {

        brandsDTOS.forEach(dto -> {
            Brand brand = new Brand();
            brand.setName(dto.getName());
            brandRepository.save(brand);

            List<Model> models = new ArrayList<>();
            dto.getModels().forEach(modelName -> {
                if (modelName.getName() != null) {
                    Model model = new Model();
                    model.setName(modelName.getName());
                    model.setBrand(brand);

                    if (modelName.getCategory() != null) {
                        Arrays.stream(CarCategory.values())
                                .filter(value -> value.getDisplayName().toLowerCase().equals(modelName.getCategory().toLowerCase().trim()))
                                .forEach(model::setCategory);
                    }

                    modelRepository.save(model);

                    List<CarData> carDataList = new ArrayList<>();
                    if (modelName.getCarData() != null) {
                        modelName.getCarData().forEach(readCarDataDTO -> {
                            Engine engine = new Engine();
                            engine.setCylinders(readCarDataDTO.getEngine().getCylinders());
                            engine.setHorsePower(readCarDataDTO.getEngine().getHorsePower());
                            engine.setSize(readCarDataDTO.getEngine().getSize());

                            Performance performance = new Performance();
                            if (readCarDataDTO.getPerformance() != null) {
                                performance.setAcceleration(readCarDataDTO.getPerformance().getAcceleration());
                                performance.setTopSpeed(readCarDataDTO.getPerformance().getTopSpeed());
                            }

                            Transmission transmission = new Transmission();
                            if (readCarDataDTO.getTransmission() != null) {
                                Arrays.stream(DriveType.values())
                                        .filter(value -> value.toString().equals(readCarDataDTO.getTransmission().getDriveType()))
                                        .forEach(transmission::setDriveType);

                                Arrays.stream(TransmissionType.values())
                                        .filter(value -> value.toString().equals(readCarDataDTO.getTransmission().getTransmissionType()))
                                        .forEach(transmission::setTransmissionType);

                                transmission.setNumberOfGears(readCarDataDTO.getTransmission().getNumberOfGears());
                            }

                            CarData carData = new CarData(engine, performance, transmission, model);
                            carDataList.add(carData);

                            this.engineRepository.save(engine);
                            this.performanceRepository.save(performance);
                            this.transmissionRepository.save(transmission);
                            this.carDataRepository.save(carData);
                        });
                    }
                    model.setCarData(carDataList);
                    models.add(model);
                }
            });
            brand.setModels(models);
            modelRepository.saveAll(models);
            brandRepository.save(brand);
        });
    }
}
