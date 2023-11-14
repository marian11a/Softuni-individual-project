package bg.softuni.carsHeaven.model.dtos.cars;

import com.google.gson.annotations.SerializedName;

public class ReadPerformanceDTO {

    @SerializedName("acceleration")
    private Double acceleration;

    @SerializedName("topSpeed")
    private Integer topSpeed;

    public Double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Double acceleration) {
        this.acceleration = acceleration;
    }

    public Integer getTopSpeed() {
        return topSpeed;
    }

    public void setTopSpeed(Integer topSpeed) {
        this.topSpeed = topSpeed;
    }
}
