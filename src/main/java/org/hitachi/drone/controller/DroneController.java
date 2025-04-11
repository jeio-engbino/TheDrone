package org.hitachi.drone.controller;

import org.hitachi.drone.service.DroneService;
import org.hitachi.drone.vo.request.DroneRequest;
import org.hitachi.drone.vo.request.MedicationRequest;
import org.hitachi.drone.vo.response.DroneResponse;
import org.hitachi.drone.vo.response.MedicationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/drone")
public class DroneController {

    @Autowired
    DroneService droneService;

    @PostMapping("/register")
    public ResponseEntity<Object> registerDroneDetails(@RequestBody DroneRequest droneRequest){
        return droneService.registerDrone(droneRequest);
    }

    @GetMapping("/avail")
    public ResponseEntity<DroneResponse> getAllDronesFreeForLoading(){
        return droneService.getAvailDroneList();
    }

    @PostMapping("/load")
    public ResponseEntity<Object> loadDroneBySerialNumber(@RequestBody MedicationRequest medicationRequest){
        return droneService.loadDroneBySerialNumber(medicationRequest);
    }

    @PostMapping("/medication")
    public ResponseEntity<Object> getMedicationInfo(@RequestBody Map<String, String> payLoad){
        return droneService.getMedicationInfo(payLoad);
    }

    @PostMapping("/battery")
    public ResponseEntity<Object> getBatteryInformation(@RequestBody Map<String, String> payLoad){
        return droneService.getDroneBatteryBySerialNumber(payLoad);
    }
}
