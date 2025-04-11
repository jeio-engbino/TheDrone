package org.hitachi.drone.db.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "drone_id")
    private Long droneId;
    @Column(name = "medication_id")
    private Long medicationId;
    @Column(name = "loading_start")
    private LocalDateTime loadingStartTime;
    @Column(name = "loading_end")
    private LocalDateTime loadingEndTime;
    @Column(name = "deliver_start")
    private LocalDateTime deliverStartTime;
    @Column(name = "deliver_end")
    private LocalDateTime deliverEndTime;
    @Column(name = "return_start")
    private LocalDateTime returnStartTime;
    @Column(name = "return_end")
    private LocalDateTime returnEndTime;

}
