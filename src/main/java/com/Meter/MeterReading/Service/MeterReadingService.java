package com.Meter.MeterReading.Service;

import com.Meter.MeterReading.Model.Meter;
import com.Meter.MeterReading.Model.MeterReading;
import com.Meter.MeterReading.Model.User;
import com.Meter.MeterReading.Repository.MeterReadingRepository;
import com.Meter.MeterReading.Repository.MeterRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeterReadingService {

    private final MeterReadingRepository readingRepository;
    private final MeterRepository meterRepository;
    private final WhatsAppService whatsAppService;


    public MeterReadingService(
            MeterReadingRepository readingRepository,
            MeterRepository meterRepository,
            WhatsAppService whatsAppService) {

        this.readingRepository = readingRepository;
        this.meterRepository = meterRepository;
        this.whatsAppService = whatsAppService;
    }


    // =========================================================
    // ADD READING + SEND WHATSAPP
    // =========================================================

    public static class AddReadingResult {
        private final MeterReading reading;
        private final boolean whatsappSent;
        private final String whatsappError;

        public AddReadingResult(MeterReading reading, boolean whatsappSent, String whatsappError) {
            this.reading = reading;
            this.whatsappSent = whatsappSent;
            this.whatsappError = whatsappError;
        }

        public MeterReading getReading() { return reading; }
        public boolean isWhatsappSent() { return whatsappSent; }
        public String getWhatsappError() { return whatsappError; }
    }

    public AddReadingResult addReading(
            Integer meterId,
            MeterReading reading) {


        // -----------------------------------------------------
        // FIND METER
        // -----------------------------------------------------

        Meter meter =
                meterRepository.findById(meterId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Meter not found with id: "
                                                + meterId
                                )
                        );


        // -----------------------------------------------------
        // GET USER
        // -----------------------------------------------------

        User user = meter.getUser();


        if (user == null) {

            throw new RuntimeException(
                    "No user is associated with this meter."
            );
        }


        // -----------------------------------------------------
        // GET PHONE NUMBER
        // -----------------------------------------------------

        String phoneNumber =
                user.getPhoneNumber();


        if (phoneNumber == null ||
                phoneNumber.isBlank()) {

            throw new RuntimeException(
                    "User does not have a WhatsApp phone number."
            );
        }


        // -----------------------------------------------------
        // CONNECT READING TO METER
        // -----------------------------------------------------

        reading.setMeter(meter);


        // -----------------------------------------------------
        // SET DATE
        // -----------------------------------------------------

        reading.setReadingDate(
                LocalDateTime.now()
        );


        // -----------------------------------------------------
        // SAVE DATABASE
        // -----------------------------------------------------

        MeterReading savedReading =
                readingRepository.save(reading);


        // -----------------------------------------------------
        // WHATSAPP MESSAGE
        // -----------------------------------------------------

        boolean whatsappSent = false;
        String whatsappError = null;

        try {

            String response =
                    whatsAppService.sendMeterReadingTemplate(

                            phoneNumber,

                            meter.getMeterNumber(),

                            String.valueOf(
                                    reading.getReadingValue()
                            ),

                            reading.getInputMethod()
                    );


            System.out.println(
                    "WhatsApp response: "
                            + response
            );

            whatsappSent = true;

        } catch (Exception e) {

            e.printStackTrace();

            whatsappError = e.getMessage();

            /*
             * Reading is already saved.
             *
             * We don't delete it if WhatsApp fails.
             */

            System.out.println(
                    "Reading saved, but WhatsApp failed."
            );

            System.out.println(
                    e.getMessage()
            );
        }


        return new AddReadingResult(savedReading, whatsappSent, whatsappError);
    }


    // =========================================================
    // GET ALL READINGS
    // =========================================================

    public List<MeterReading> getAllReadings() {

        return readingRepository.findAll();
    }


    // =========================================================
    // GET READING BY ID
    // =========================================================

    public MeterReading getReadingById(
            Integer id) {

        return readingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Reading not found with id: "
                                        + id
                        )
                );
    }


    // =========================================================
    // GET READINGS BY METER
    // =========================================================

    public List<MeterReading> getReadingsByMeter(
            Integer meterId) {

        if (!meterRepository.existsById(meterId)) {

            throw new RuntimeException(
                    "Meter not found with id: "
                            + meterId
            );
        }


        return readingRepository
                .findByMeterMeterId(meterId);
    }


    // =========================================================
    // UPDATE
    // =========================================================

    public MeterReading updateReading(
            Integer id,
            MeterReading updatedReading) {

        MeterReading existingReading =
                readingRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Reading not found with id: "
                                                + id
                                )
                        );


        existingReading.setReadingValue(
                updatedReading.getReadingValue()
        );


        existingReading.setInputMethod(
                updatedReading.getInputMethod()
        );


        existingReading.setImagePath(
                updatedReading.getImagePath()
        );


        existingReading.setVerified(
                updatedReading.isVerified()
        );


        return readingRepository.save(
                existingReading
        );
    }


    // =========================================================
    // DELETE
    // =========================================================

    public void deleteReading(
            Integer id) {

        if (!readingRepository.existsById(id)) {

            throw new RuntimeException(
                    "Reading not found with id: "
                            + id
            );
        }


        readingRepository.deleteById(id);
    }
}