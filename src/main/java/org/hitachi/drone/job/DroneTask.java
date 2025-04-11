package org.hitachi.drone.job;


import lombok.extern.slf4j.Slf4j;
import org.hitachi.drone.db.model.Drone;
import org.hitachi.drone.db.model.Transaction;
import org.hitachi.drone.enums.DroneStateEnum;
import org.hitachi.drone.service.DroneService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Service
@EnableScheduling
@Slf4j
public class DroneTask {

    private final DroneService droneService;

    public DroneTask(DroneService droneService) {
        this.droneService = droneService;
    }

    @Scheduled(cron = "*/1 * * * * ?")
    @Transactional
    public void updateDroneStates() {
        log.info("===============TASK STARTED===============");
        List<Drone> drones = droneService.getDroneList();
        LocalDateTime now = LocalDateTime.now();
        drones.forEach(x -> {
            Transaction transaction = droneService.getDroneTransaction(x.getId()); //
            switch (x.getState()) {
                case LOADING: { //3s
                    log.info("===============LOADING::::{}",x.getSerialNumber());
                    if (transaction.getLoadingStartTime().plusSeconds(15).isBefore(now)) {
                        transaction.setLoadingEndTime(now);
                        x.setState(DroneStateEnum.LOADED);
                    } else {
                        x.setBatteryCapacity(x.getBatteryCapacity().subtract(BigDecimal.valueOf(0.2)));
                    }
                    break;
                }
                case LOADED: { //1.5s
                    log.info("===============LOADED::::{}",x.getSerialNumber());
                    if (transaction.getLoadingEndTime().plusSeconds(5).isBefore(now)) {
                        transaction.setDeliverStartTime(now);
                        x.setState(DroneStateEnum.DELIVERING);
                    } else {
                        x.setBatteryCapacity(x.getBatteryCapacity().subtract(BigDecimal.valueOf(0.3)));
                    }
                    break;
                }
                case DELIVERING: { //12s
                    log.info("===============DELIVERING::::{}",x.getSerialNumber());
                    if (transaction.getDeliverStartTime().plusSeconds(30).isBefore(now)) {
                        transaction.setDeliverEndTime(now);
                        x.setState(DroneStateEnum.DELIVERED);
                    } else {
                        x.setBatteryCapacity(x.getBatteryCapacity().subtract(BigDecimal.valueOf(0.4)));
                    }
                    break;
                }
                case DELIVERED: { //1.5s
                    log.info("===============DELIVERED::::{}",x.getSerialNumber());
                    if (transaction.getDeliverEndTime().plusSeconds(5).isBefore(now)) {
                        transaction.setReturnStartTime(now);
                        x.setState(DroneStateEnum.RETURNING);
                    } else {
                        x.setBatteryCapacity(x.getBatteryCapacity().subtract(BigDecimal.valueOf(0.03)));
                    }
                    break;
                }
                case RETURNING: { //7s
                    log.info("===============RETURNING::::{}",x.getSerialNumber());
                    if (transaction.getReturnStartTime().plusSeconds(20).isBefore(now)) {
                        transaction.setReturnEndTime(now);
                        x.setState(DroneStateEnum.IDLE);
                    }else {
                        x.setBatteryCapacity(x.getBatteryCapacity().subtract(BigDecimal.valueOf(0.35)));
                    }
                    break;
                }
                default: {
                    log.info("===============IDLE::::{}",x.getSerialNumber());
                }
            }
            if (!ObjectUtils.isEmpty(transaction)) {
                droneService.updateTransaction(transaction);
            }
            if (x.getState() != DroneStateEnum.IDLE) {
                droneService.updateDrone(x);
            }
        });
        log.info("===============TASK ENDED===============");
    }
}
