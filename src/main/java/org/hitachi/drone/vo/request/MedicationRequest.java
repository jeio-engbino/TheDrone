package org.hitachi.drone.vo.request;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;

@Data
public class MedicationRequest {

    @Pattern(regexp = "^[\\w-]+$", message = "Name can only contain letters, numbers, hyphens (-), and underscores (_)!")
    private String name;

    @Max(value = 1000, message = "Weight cannot exceed 1000!")
    private Float weight;

    @Pattern(regexp = "^\\w+$", message = "Code can only contain letters, numbers, and underscores (_)!")
    private String code;

    private String image;

    private String serialNumber;
}
