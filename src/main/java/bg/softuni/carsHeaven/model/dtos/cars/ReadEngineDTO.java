package bg.softuni.carsHeaven.model.dtos.cars;

import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class ReadEngineDTO {

    @SerializedName("cylinders")
    @DecimalMin(value = "1.0", message = "This field is required")
    @NotNull(message = "This field is required")
    private Integer cylinders;

    @SerializedName("size")
    @DecimalMin(value = "0.1", message = "This field is required")
    @NotNull(message = "This field is required")
    private Double size;

    @SerializedName("fuel")
    private String fuel;

    @SerializedName("horsePower")
    private Integer horsePower;

    public Integer getCylinders() {
        return cylinders;
    }

    public void setCylinders(Integer cylinders) {
        this.cylinders = cylinders;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public Integer getHorsePower() {
        return horsePower;
    }

    public void setHorsePower(Integer horsePower) {
        this.horsePower = horsePower;
    }
}
