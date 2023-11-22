package bg.softuni.carsHeaven.model.entity;

import bg.softuni.carsHeaven.model.enums.FuelType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "engines")
public class Engine extends BaseEntity {

    @Positive
    private Integer cylinders;

    @Positive
    private Double size;

    @Enumerated(EnumType.STRING)
    private FuelType fuel;

    @Positive
    private Integer horsePower;

    public Engine() {
    }

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

    public Integer getHorsePower() {
        return horsePower;
    }

    public void setHorsePower(Integer horsePower) {
        this.horsePower = horsePower;
    }

    public FuelType getFuel() {
        return fuel;
    }

    public void setFuel(FuelType fuel) {
        this.fuel = fuel;
    }
}
