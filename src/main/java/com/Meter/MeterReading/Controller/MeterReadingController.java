package com.Meter.MeterReading.Controller;

import com.Meter.MeterReading.Model.MeterReading;
import com.Meter.MeterReading.Service.GeminiService;
import com.Meter.MeterReading.Service.MeterReadingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reading")
public class MeterReadingController {

    private final MeterReadingService readingService;
    private final GeminiService geminiService;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    @Autowired
    public MeterReadingController(
            MeterReadingService readingService,
            GeminiService geminiService) {

        this.readingService = readingService;
        this.geminiService = geminiService;
    }


    // =========================================================
    // 1. ADD MANUAL / VERIFIED CAMERA READING
    // =========================================================
    //
    // URL:
    //
    // POST
    // http://localhost:8080/reading/meter/{meterId}
    //
    // Example:
    //
    // POST
    // http://localhost:8080/reading/meter/1
    //
    // Body:
    //
    // {
    //     "readingValue": 785,
    //     "inputMethod": "MANUAL",
    //     "verified": true
    // }
    //
    // This method:
    //
    // 1. Finds the meter
    // 2. Finds its user
    // 3. Saves the reading
    // 4. Gets user's phone number
    // 5. Sends WhatsApp
    //
    // =========================================================

    @PostMapping("/meter/{meterId}")
    public ResponseEntity<?> addReading(

            @PathVariable Integer meterId,

            @RequestBody MeterReading reading) {

        try {

            MeterReadingService.AddReadingResult result =
                    readingService.addReading(
                            meterId,
                            reading
                    );


            java.util.Map<String, Object> responseData = new java.util.HashMap<>();
            responseData.put("success", true);
            responseData.put("saved", true);
            responseData.put("reading", result.getReading());
            responseData.put("whatsapp", result.isWhatsappSent());
            if (!result.isWhatsappSent() && result.getWhatsappError() != null) {
                responseData.put("whatsappError", result.getWhatsappError());
            }

            return ResponseEntity.ok(responseData);


        } catch (Exception e) {

            e.printStackTrace();


            return ResponseEntity
                    .badRequest()
                    .body(

                            Map.of(

                                    "success",
                                    false,

                                    "saved",
                                    false,

                                    "error",
                                    e.getMessage()
                            )
                    );
        }
    }


    // =========================================================
    // 2. GET ALL READINGS
    // =========================================================
    //
    // GET:
    //
    // http://localhost:8080/reading
    //
    // =========================================================

    @GetMapping
    public ResponseEntity<?> getAllReadings() {

        try {

            List<MeterReading> readings =
                    readingService.getAllReadings();

            List<Map<String, Object>> response =
                    readings.stream()
                            .map(reading -> {

                                Map<String, Object> data =
                                        new java.util.HashMap<>();

                                data.put(
                                        "readingId",
                                        reading.getReadingId()
                                );

                                data.put(
                                        "readingValue",
                                        reading.getReadingValue()
                                );

                                data.put(
                                        "readingDate",
                                        reading.getReadingDate()
                                );

                                data.put(
                                        "inputMethod",
                                        reading.getInputMethod()
                                );

                                data.put(
                                        "imagePath",
                                        reading.getImagePath()
                                );

                                data.put(
                                        "verified",
                                        reading.isVerified()
                                );


                                // =====================================
                                // METER INFORMATION
                                // =====================================

                                if (reading.getMeter() != null) {

                                    data.put(
                                            "meterId",
                                            reading.getMeter().getMeterId()
                                    );

                                    data.put(
                                            "meterNumber",
                                            reading.getMeter().getMeterNumber()
                                    );

                                    data.put(
                                            "meter",
                                            Map.of(
                                                    "meterId", reading.getMeter().getMeterId(),
                                                    "meterNumber", reading.getMeter().getMeterNumber()
                                            )
                                    );

                                } else {

                                    data.put(
                                            "meterId",
                                            null
                                    );

                                    data.put(
                                            "meterNumber",
                                            null
                                    );

                                    data.put(
                                            "meter",
                                            null
                                    );
                                }


                                return data;
                            })
                            .toList();


            return ResponseEntity.ok(response);


        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .body(
                            Map.of(
                                    "success",
                                    false,
                                    "error",
                                    e.getMessage()
                            )
                    );
        }
    }

    // =========================================================
    // 3. GET READING BY ID
    // =========================================================
    //
    // GET:
    //
    // http://localhost:8080/reading/admin/meterreading/10
    //
    // =========================================================

    @GetMapping("/admin/meterreading/{id}")
    public ResponseEntity<?> getReadingById(

            @PathVariable Integer id) {

        try {

            MeterReading reading =
                    readingService.getReadingById(
                            id
                    );


            return ResponseEntity.ok(
                    reading
            );


        } catch (Exception e) {

            e.printStackTrace();


            return ResponseEntity
                    .notFound()
                    .build();
        }
    }


    // =========================================================
    // 4. GET READINGS OF PARTICULAR METER
    // =========================================================
    //
    // GET:
    //
    // http://localhost:8080/reading/meter/1
    //
    // =========================================================

    @GetMapping("/meter/{meterId}")
    public ResponseEntity<?> getReadingsByMeter(

            @PathVariable Integer meterId) {

        try {

            List<MeterReading> readings =
                    readingService.getReadingsByMeter(
                            meterId
                    );


            return ResponseEntity.ok(
                    readings
            );


        } catch (Exception e) {

            e.printStackTrace();


            return ResponseEntity
                    .badRequest()
                    .body(

                            Map.of(

                                    "success",
                                    false,

                                    "error",
                                    e.getMessage()
                            )
                    );
        }
    }


    // =========================================================
    // 5. UPDATE READING
    // =========================================================
    //
    // PUT:
    //
    // http://localhost:8080/reading/admin/updatemeterreading/10
    //
    // =========================================================

    @PutMapping("/admin/updatemeterreading/{id}")
    public ResponseEntity<?> updateReading(

            @PathVariable Integer id,

            @RequestBody MeterReading reading) {

        try {

            MeterReading updatedReading =
                    readingService.updateReading(
                            id,
                            reading
                    );


            return ResponseEntity.ok(

                    Map.of(

                            "success",
                            true,

                            "reading",
                            updatedReading
                    )
            );


        } catch (Exception e) {

            e.printStackTrace();


            return ResponseEntity
                    .badRequest()
                    .body(

                            Map.of(

                                    "success",
                                    false,

                                    "error",
                                    e.getMessage()
                            )
                    );
        }
    }


    // =========================================================
    // 6. DELETE READING
    // =========================================================
    //
    // DELETE:
    //
    // http://localhost:8080/reading/admin/deletemeterreading/10
    //
    // =========================================================

    @DeleteMapping("/admin/deletemeterreading/{id}")
    public ResponseEntity<?> deleteReading(

            @PathVariable Integer id) {

        try {

            readingService.deleteReading(
                    id
            );


            return ResponseEntity.ok(

                    Map.of(

                            "success",
                            true,

                            "message",
                            "Reading deleted successfully"
                    )
            );


        } catch (Exception e) {

            e.printStackTrace();


            return ResponseEntity
                    .badRequest()
                    .body(

                            Map.of(

                                    "success",
                                    false,

                                    "error",
                                    e.getMessage()
                            )
                    );
        }
    }


    // =========================================================
    // 7. SCAN METER USING GEMINI
    // =========================================================
    //
    // IMPORTANT:
    //
    // This endpoint ONLY reads the image.
    //
    // It DOES NOT:
    //
    // ❌ save to database
    // ❌ send WhatsApp
    //
    // The user must first verify the reading.
    //
    // URL:
    //
    // POST
    // http://localhost:8080/reading/scan
    //
    // Form-data:
    //
    // image = meter.jpg
    //
    // Response:
    //
    // {
    //     "success": true,
    //     "reading": "785"
    // }
    //
    // =========================================================

    @PostMapping("/scan")
    public ResponseEntity<?> scanMeter(

            @RequestParam("image")
            MultipartFile image) {

        try {

            // -------------------------------------------------
            // CHECK IMAGE
            // -------------------------------------------------

            if (image == null ||
                    image.isEmpty()) {

                return ResponseEntity
                        .badRequest()
                        .body(

                                Map.of(

                                        "success",
                                        false,

                                        "error",
                                        "Image is empty."
                                )
                        );
            }


            // -------------------------------------------------
            // GEMINI OCR
            // -------------------------------------------------

            String reading =
                    geminiService.readMeter(
                            image
                    );


            // -------------------------------------------------
            // CHECK RESULT
            // -------------------------------------------------

            if (reading == null ||
                    reading.isBlank()) {

                return ResponseEntity
                        .badRequest()
                        .body(

                                Map.of(

                                        "success",
                                        false,

                                        "error",
                                        "Could not detect meter reading."
                                )
                        );
            }


            System.out.println(
                    "Gemini detected reading: "
                            + reading
            );


            // -------------------------------------------------
            // RETURN ONLY
            // -------------------------------------------------
            //
            // DO NOT SAVE
            // DO NOT SEND WHATSAPP
            //
            // User will verify first.
            //
            // -------------------------------------------------

            return ResponseEntity.ok(

                    Map.of(

                            "success",
                            true,

                            "reading",
                            reading
                    )
            );


        } catch (Exception e) {

            e.printStackTrace();


            return ResponseEntity
                    .internalServerError()
                    .body(

                            Map.of(

                                    "success",
                                    false,

                                    "error",
                                    e.getMessage()
                            )
                    );
        }
    }
}