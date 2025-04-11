package org.hitachi.drone.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.hitachi.drone.db.model.Medication;
import org.hitachi.drone.db.model.Transaction;
import org.hitachi.drone.db.repository.MedicationRepository;
import org.hitachi.drone.db.repository.TransactionRepository;
import org.hitachi.drone.vo.request.DroneRequest;
import org.hitachi.drone.enums.DroneStateEnum;
import org.hitachi.drone.enums.DroneWeightEnum;
import org.hitachi.drone.db.model.Drone;
import org.hitachi.drone.db.repository.DroneRepository;
import org.hitachi.drone.service.DroneService;
import org.hitachi.drone.vo.request.MedicationRequest;
import org.hitachi.drone.vo.response.DroneResponse;
import org.hitachi.drone.vo.response.MedicationResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class DroneServiceImpl implements DroneService {

    @Value("${api.hitachi.battery-capacity-limit}")
    private int batteryCapacityLimit;

    @Autowired
    private DroneRepository droneRepository;
    @Autowired
    private MedicationRepository medicationRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    @Transactional
    public ResponseEntity<Object> registerDrone(DroneRequest droneRequest) {
        droneRequest.setSerialNumber(droneRequest.getSerialNumber().toUpperCase());
        droneRequest.setModel(droneRequest.getModel().toUpperCase());
        droneRequest.setState(droneRequest.getState().toUpperCase());

        try{
            Drone drone  = mappedDroneDetailsToDatabase(droneRequest);
            droneRepository.save(drone);
        }catch(Exception e){
            log.info("An error encountered in registerDroneDetails: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        }
        Map<String, Boolean> response = new HashMap<>();
        response.put("Drone registered successfully", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<DroneResponse> getAvailDroneList() {
        DroneResponse droneResponse = new DroneResponse();
        droneResponse.setAvailDroneList(droneRepository.findAllByState(DroneStateEnum.IDLE));
        return ResponseEntity.ok(droneResponse);
    }

    @Override
    public List<Drone> getDroneList() {
        return droneRepository.findAll();
    }

    @Override
    public Transaction getDroneTransaction(Long droneId) {
        return transactionRepository.findByDroneId(droneId);
    }

    @Override
    public void updateDrone(Drone drone) {
        droneRepository.save(drone);
    }

    @Override
    public void updateTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public ResponseEntity<Object> loadDroneBySerialNumber(MedicationRequest medicationRequest) {
        medicationRequest.setSerialNumber(medicationRequest.getSerialNumber().toUpperCase());

        try {
            Optional<Drone> drone = droneRepository.findBySerialNumber(medicationRequest.getSerialNumber());
            if (drone.isPresent()) {

                if (drone.get().getBatteryCapacity().compareTo((BigDecimal.valueOf(25))) < 0){
                    throw new IllegalArgumentException("Battery capacity is less than 25%. Loading was unsuccessful.");
                }
                if (medicationRequest.getWeight() > drone.get().getWeightEnum().getCapacity()) {
                    throw new IllegalArgumentException(String.format("%s can't accommodate medication's weight. Please select different drone.", drone.get().getSerialNumber()));
                }

                Medication medication = mappedMedicationDetailsToDatabase(medicationRequest, drone.get());
                medicationRepository.save(medication);

                Transaction transaction = mappedTransactionToDatabase(drone.get().getId(), medication.getId());
                transactionRepository.save(transaction);

                Drone selected = drone.get();
                selected.setState(DroneStateEnum.LOADING);
                selected.setMedication(medication);
                droneRepository.save(selected);

                Map<String, Boolean> response = new HashMap<>();
                response.put("Medication loaded successfully", Boolean.TRUE);
                return ResponseEntity.ok(response);
            }
            throw new RuntimeException("Serial number not found");
        } catch(Exception e){
            log.info("An error encountered in loadDroneBySerialNumber: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> getMedicationInfo(Map<String, String> payLoad) {
        if (CollectionUtils.isEmpty(payLoad)) {
            return ResponseEntity.ok(new MedicationResponse());
        }

        payLoad.put("serialNumber", payLoad.get("serialNumber").toUpperCase());

        try {
            Optional<Drone> data = droneRepository.findBySerialNumber(payLoad.get("serialNumber"));
            if(data.isPresent()){
                Medication source = data.get().getMedication();
                MedicationResponse response = new MedicationResponse();
                BeanUtils.copyProperties(source, response);
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Drone doesn't exist.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error in getMedicationInfo: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> getDroneBatteryBySerialNumber(Map<String, String> payLoad) {
        if (CollectionUtils.isEmpty(payLoad)) {
            return ResponseEntity.ok(new MedicationResponse());
        }

        payLoad.put("serialNumber", payLoad.get("serialNumber").toUpperCase());

        try {
            Optional<Drone> data = droneRepository.findBySerialNumber(payLoad.get("serialNumber"));
            if(data.isPresent()){
                Map<String, String> response = new HashMap<>();
                response.put("Serial Number: ", data.get().getSerialNumber());
                response.put("Battery: ", data.get().getBatteryCapacity().toString() + "%");
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Drone doesn't exist.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error in getDroneBatteryBySerialNumber: " + e.getMessage());
        }
    }


    private Drone mappedDroneDetailsToDatabase(DroneRequest droneRequest) {
        DroneWeightEnum weightEnum = DroneWeightEnum.valueOf(droneRequest.getModel().toUpperCase());
        if(Float.parseFloat(droneRequest.getBatteryCapacity()) <= batteryCapacityLimit){
            throw new IllegalArgumentException("Battery capacity is less than 25%. Drone registration was unsuccessful.");
        }
        return Drone.builder()
                .serialNumber(droneRequest.getSerialNumber())
                .weightEnum(weightEnum)
                .batteryCapacity(new BigDecimal(droneRequest.getBatteryCapacity()))
                .state(DroneStateEnum.valueOf(droneRequest.getState()))
                .createDate(new Date(System.currentTimeMillis()))
                .build();
    }

    private Medication mappedMedicationDetailsToDatabase(MedicationRequest dronePayload, Drone drone) {
        return Medication.builder()
                .name(dronePayload.getName())
                .image(dronePayload.getImage())
                .code(dronePayload.getCode())
                .weight(dronePayload.getWeight())
                .drone(drone)
                .createDate(Date.from(Instant.now()))
                .build();
    }

    private Transaction mappedTransactionToDatabase(Long droneId, Long medicationId) {
        return Transaction.builder()
                .droneId(droneId)
                .medicationId(medicationId)
                .loadingStartTime(LocalDateTime.now())
                .build();
    }
}
