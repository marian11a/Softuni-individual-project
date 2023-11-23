package bg.softuni.carsHeaven.model.dtos.cars;

import com.google.gson.annotations.SerializedName;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public class ReadBrandsDTO {

    @SerializedName("brandName")
    private String name;

    private Long id;

    @SerializedName("models")
    private List<ReadModelsDTO> models;

    private String imageUrl;

    public ReadBrandsDTO() {
    }

    public ReadBrandsDTO(Long id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ReadModelsDTO> getModels() {
        return models;
    }

    public void setModels(List<ReadModelsDTO> models) {
        this.models = models;
    }
}
