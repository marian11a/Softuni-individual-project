package bg.softuni.carsHeaven.model.dtos.cars;

import com.google.gson.annotations.SerializedName;

public class ReadCarDataDTO {
    private Long id;
    private Long modelId;
    @SerializedName("engine")
    private ReadEngineDTO engine;

    @SerializedName("performance")
    private ReadPerformanceDTO performance;

    @SerializedName("transmission")
    private ReadTransmissionDTO transmission;

    public ReadEngineDTO getEngine() {
        return engine;
    }

    public void setEngine(ReadEngineDTO engine) {
        this.engine = engine;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public ReadTransmissionDTO getTransmission() {
        return transmission;
    }

    public void setTransmission(ReadTransmissionDTO transmission) {
        this.transmission = transmission;
    }

    public ReadPerformanceDTO getPerformance() {
        return performance;
    }

    public void setPerformance(ReadPerformanceDTO performance) {
        this.performance = performance;
    }
}
