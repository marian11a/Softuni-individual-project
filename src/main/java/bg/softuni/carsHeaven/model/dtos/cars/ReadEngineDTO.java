package bg.softuni.carsHeaven.model.dtos.cars;

import com.google.gson.annotations.SerializedName;

public class ReadEngineDTO {

    @SerializedName("cylinders")
    private Integer cylinders;

    @SerializedName("size")
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
