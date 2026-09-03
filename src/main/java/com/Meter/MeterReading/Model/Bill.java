package com.Meter.MeterReading.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billId;

    private Double previousReading;

    private Double currentReading;

    private Double unitsConsumed;

    private Double amount;

    private LocalDate billDate;

    @OneToOne
    @JoinColumn(name = "reading_id", nullable = false)
    private MeterReading reading;
}