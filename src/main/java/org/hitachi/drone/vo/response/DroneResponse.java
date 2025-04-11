package org.hitachi.drone.vo.response;

import lombok.Data;
import org.hitachi.drone.db.model.Drone;

import java.util.List;

@Data
public class DroneResponse {

    private List<Drone> availDroneList;

}
