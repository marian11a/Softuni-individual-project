package bg.softuni.carsHeaven.model.dtos.cars;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReadModelsDTO {

    @SerializedName("name")
    private String name;

    private Long id;
    private String brandName;

    @SerializedName("category")
    private String category;

    private String imageUrl;

    @SerializedName("carData")
    private List<ReadCarDataDTO> carData;


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ReadCarDataDTO> getCarData() {
        return carData;
    }

    public void setCarData(List<ReadCarDataDTO> carData) {
        this.carData = carData;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
