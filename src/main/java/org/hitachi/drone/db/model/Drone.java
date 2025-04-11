package org.hitachi.drone.db.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hitachi.drone.enums.DroneStateEnum;
import org.hitachi.drone.enums.DroneWeightEnum;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Builder
@Entity
@Table(name = "drone")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Drone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "serial_number")
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "model")
    private DroneWeightEnum weightEnum;

    @Column(name = "battery_capacity", precision = 10, scale = 2)
    private BigDecimal batteryCapacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DroneStateEnum state;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medication_id")
    private Medication medication;

    @Column(name = "create_dt")
    private Date createDate;


}
