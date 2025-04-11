package org.hitachi.drone.db.repository;

import org.hitachi.drone.enums.DroneStateEnum;
import org.hitachi.drone.db.model.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {
    Optional<Drone> findBySerialNumber(String serialNumber);
    List<Drone> findAllByState(DroneStateEnum state);
}
