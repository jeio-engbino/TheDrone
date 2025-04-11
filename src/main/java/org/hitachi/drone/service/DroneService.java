package org.hitachi.drone.service;

import org.hitachi.drone.db.model.Drone;
import org.hitachi.drone.db.model.Transaction;
import org.hitachi.drone.vo.request.DroneRequest;
import org.hitachi.drone.vo.request.MedicationRequest;
import org.hitachi.drone.vo.response.DroneResponse;
import org.hitachi.drone.vo.response.MedicationResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface DroneService {
    ResponseEntity<Object> registerDrone(DroneRequest droneRequest);

    ResponseEntity<DroneResponse> getAvailDroneList();

    List<Drone> getDroneList();

    Transaction getDroneTransaction(Long droneId);

    void updateDrone(Drone drone);

    void updateTransaction(Transaction transaction);

    ResponseEntity<Object> loadDroneBySerialNumber(MedicationRequest medicationRequest);

    ResponseEntity<Object> getMedicationInfo(Map<String, String> payLoad);

    ResponseEntity<Object> getDroneBatteryBySerialNumber(Map<String, String> payLoad);
}
