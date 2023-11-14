package bg.softuni.carsHeaven.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cars_data")
public class CarData extends BaseEntity {

    @ManyToOne
    private Model model;

    @OneToOne
    private Engine engine;

    @OneToOne
    private Performance performance;

    @OneToOne
    private Transmission transmission;

    public CarData(Engine engine,
                   Performance performance ,
                   Transmission transmission,
                   Model model) {
        this.engine = engine;
        this.model = model;
        this.transmission = transmission;
        this.performance = performance;
    }

    public CarData() {
    }

    public Model getModel() {
        return model;
    }
    public void setModel(Model model) {
        this.model = model;
    }


    public Transmission getTransmission() {
        return transmission;
    }

    public void setTransmission(Transmission transmission) {
        this.transmission = transmission;
    }


    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public Performance getPerformance() {
        return performance;
    }

    public void setPerformance(Performance performance) {
        this.performance = performance;
    }
}
