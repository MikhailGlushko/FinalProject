package ua.glushko.model.entity.tmp;

public enum DevicePhisicalState {

    NEW("NEW"),
    OLD("OLD"),
    BROKEN("BROKEN");

    DevicePhisicalState(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue(){
        return value;
    }
}
