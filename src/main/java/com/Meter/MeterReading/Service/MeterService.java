package com.Meter.MeterReading.Service;

import com.Meter.MeterReading.Model.Meter;
import com.Meter.MeterReading.Model.User;
import com.Meter.MeterReading.Repository.MeterRepository;
import com.Meter.MeterReading.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeterService {

    private final MeterRepository meterRepository;
    private final UserRepository userRepository;

    public MeterService(
            MeterRepository meterRepository,
            UserRepository userRepository) {

        this.meterRepository = meterRepository;
        this.userRepository = userRepository;
    }

    public List<Meter> getAllMeters() {
        return meterRepository.findAll();
    }

    public List<Meter> getMetersByUser(Integer userId) {

        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        return meterRepository.findByUserUserId(userId);
    }

    public Meter getMeterById(Integer id) {

        return meterRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Meter not found with id: " + id
                        )
                );
    }

    public Meter addMeter(Meter meter) {

        // Make sure a user was provided
        if (meter.getUser() == null ||
                meter.getUser().getUserId() == null) {

            throw new RuntimeException("User ID is required");
        }

        Integer userId = meter.getUser().getUserId();

        // Find the actual user from database
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + userId
                        )
                );

        // Attach the existing user to the meter
        meter.setUser(user);

        return meterRepository.save(meter);
    }

    public Meter updateMeter(Integer id, Meter updatedMeter) {

        Meter existingMeter =
                meterRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Meter not found with id: " + id
                                )
                        );

        existingMeter.setMeterNumber(
                updatedMeter.getMeterNumber()
        );

        existingMeter.setMeterType(
                updatedMeter.getMeterType()
        );

        existingMeter.setInstallationDate(
                updatedMeter.getInstallationDate()
        );

        // Update user only if provided
        if (updatedMeter.getUser() != null &&
                updatedMeter.getUser().getUserId() != null) {

            Integer userId =
                    updatedMeter.getUser().getUserId();

            User user = userRepository.findById(userId)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "User not found with id: " + userId
                            )
                    );

            existingMeter.setUser(user);
        }

        return meterRepository.save(existingMeter);
    }

    public void deleteMeter(Integer id) {

        if (!meterRepository.existsById(id)) {

            throw new RuntimeException(
                    "Meter not found with id: " + id
            );
        }

        meterRepository.deleteById(id);
    }
}