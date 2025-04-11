package org.hitachi.drone.db.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "medication")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Float weight;

    private String code;

    private String image;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn (name="drone_id")
    private Drone drone;

    @Column(name = "create_dt")
    private Date createDate;

}
