package bg.softuni.carsHeaven.model.dtos.cars;

import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.Positive;

public class ReadTransmissionDTO {

    @SerializedName("transmissionType")
    private String transmissionType;

    @Positive
    @SerializedName("numberOfGears")
    private Integer numberOfGears;

    @SerializedName("driveType")
    private String driveType;


    public String getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(String transmissionType) {
        this.transmissionType = transmissionType;
    }

    public Integer getNumberOfGears() {
        return numberOfGears;
    }

    public void setNumberOfGears(Integer numberOfGears) {
        this.numberOfGears = numberOfGears;
    }

    public String getDriveType() {
        return driveType;
    }

    public void setDriveType(String driveType) {
        this.driveType = driveType;
    }
}
