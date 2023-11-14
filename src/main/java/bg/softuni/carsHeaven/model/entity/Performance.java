package bg.softuni.carsHeaven.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "performances")
public class Performance extends BaseEntity {

    private Double acceleration;

    private Integer topSpeed;

    public Double getAcceleration() {
        return acceleration;
    }

    public Performance() {
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
