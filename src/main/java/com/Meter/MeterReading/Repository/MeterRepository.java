package com.Meter.MeterReading.Repository;

import com.Meter.MeterReading.Model.Meter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeterRepository extends JpaRepository<Meter, Integer> {

    Optional<Meter> findByMeterNumber(String meterNumber);

    boolean existsByMeterNumber(String meterNumber);

    List<Meter> findByUserUserId(Integer userId);
}

