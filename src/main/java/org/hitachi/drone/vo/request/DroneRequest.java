package org.hitachi.drone.vo.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;

@Data
@Builder
public class DroneRequest {

    @Max(value = 100, message = "Serial number is 100 characters max.")
    private String serialNumber;

    @Pattern(regexp = "LIGHTWEIGHT|MIDDLEWEIGHT|CRUISERWEIGHT|HEAVYWEIGHT", message = "Model should be LIGHTWEIGHT, MIDDLEWEIGHT, CRUISERWEIGHT or HEAVYWEIGHT")
    private String model;

    private Float weight;

    private String batteryCapacity;

    @Pattern(regexp = "IDLE|LOADING|LOADED|DELIVERING|DELIVERED|RETURNING", message = "State should be IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING")
    private String state;
}
