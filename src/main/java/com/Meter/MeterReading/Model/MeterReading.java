package com.Meter.MeterReading.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeterReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer readingId;

    private Double readingValue;

    private LocalDateTime readingDate;

    private String inputMethod;

    private String imagePath;

    private boolean verified;


    @ManyToOne
    @JoinColumn(name = "meter_id", nullable = false)
    @JsonIgnore
    private Meter meter;

    public Integer getMeterId() {
        return meter != null ? meter.getMeterId() : null;
    }

    public String getMeterNumber() {
        return meter != null ? meter.getMeterNumber() : null;
    }
}