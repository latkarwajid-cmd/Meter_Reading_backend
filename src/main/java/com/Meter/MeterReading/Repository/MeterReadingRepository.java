package com.Meter.MeterReading.Repository;

import com.Meter.MeterReading.Model.MeterReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeterReadingRepository
        extends JpaRepository<MeterReading, Integer> {

    List<MeterReading> findByMeterMeterId(Integer meterId);
}