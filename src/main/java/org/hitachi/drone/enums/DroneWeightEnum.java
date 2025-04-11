package org.hitachi.drone.enums;

public enum DroneWeightEnum {
    LIGHTWEIGHT(250.0f),
    MIDDLEWEIGHT(500.0f),
    CRUISERWEIGHT(750.0f),
    HEAVYWEIGHT(1000.0f);

    private final Float capacity;

    DroneWeightEnum(Float capacity) {
        this.capacity = capacity;
    }

    public Float getCapacity() {
        return capacity;
    }
}

