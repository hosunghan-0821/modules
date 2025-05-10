package module.database.dto;

public enum Boutique {

    JULIAN("JULIAN"),
    DOUBLE_F("DOUBLE_F"),
    BIFFI("BIFFI"),
    GNB("GNB"),
    STYLE("STYLE"),
    VIETTI("VIETTI"),
    ZILIO_NEW("ZILIO_NEW"),
    ZILIO_DISCOUNT("ZILIO_DISCOUNT");
    private final String name;

    Boutique(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
