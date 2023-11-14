package bg.softuni.carsHeaven.model.entity;

import bg.softuni.carsHeaven.model.enums.CarCategory;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "models")
public class Model extends BaseEntity{

    @ManyToOne
    private User user;

    @ManyToOne
    private Brand brand;

    @Column(nullable = false)
    private String name;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private CarCategory category;

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL)
    private List<CarData> carData;

    public Model() {
        this.carData = new ArrayList<>();
    }

    public Model(Brand brand, String name) {
        this.brand = brand;
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CarCategory getCategory() {
        return category;
    }

    public void setCategory(CarCategory category) {
        this.category = category;
    }

    public List<CarData> getCarData() {
        return carData;
    }

    public void setCarData(List<CarData> carData) {
        this.carData = carData;
    }
}
