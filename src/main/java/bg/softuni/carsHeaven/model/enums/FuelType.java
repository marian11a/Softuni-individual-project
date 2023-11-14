package bg.softuni.carsHeaven.model.enums;

public enum FuelType {
    GASOLINE("Gasoline"),
    DIESEL("Diesel");
    private final String displayName;
    FuelType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName.trim();
    }
}
