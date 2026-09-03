package com.Meter.MeterReading.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Meter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer meterId;

    @Column(unique = true, nullable = false)
    private String meterNumber;

    private String meterType;

    private LocalDate installationDate;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;


    @OneToMany(
            mappedBy = "meter",
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private List<MeterReading> readings;
}