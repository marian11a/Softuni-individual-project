package bg.softuni.carsHeaven.model.enums;

public enum CarCategory {
    SUV("SUV"),
    Sedan("Sedan"),
    Compact("Compact"),
    Van("Van"),
    Minivan("Minivan"),
    Convertible("Convertible"),
    SportsCar("Sports Car"),
    Truck("Truck"),
    Wagon("Wagon"),
    Hatchback("Hatchback"),
    Pickup("Pickup"),
    MPV("MPV"),
    Coupe("Coupe"),
    Estate("Estate"),
    CompactMPV("Compact MPV");

    private final String displayName;

    CarCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName.trim();
    }
}
