package com.Meter.MeterReading.Controller;

import com.Meter.MeterReading.Model.WhatsAppRequest;
import com.Meter.MeterReading.Service.WhatsAppService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/whatsapp")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://mymeterreading.netlify.app"
})public class WhatsAppController {

    private final WhatsAppService whatsappService;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public WhatsAppController(
            WhatsAppService whatsappService
    ) {

        this.whatsappService =
                whatsappService;
    }


    // =========================================================
    // /hello
    //
    // TEST MESSAGE
    // =========================================================

    @PostMapping("/hello")
    public ResponseEntity<?> hello() {

        try {

            String response =
                    whatsappService.sendHelloTemplate(
                            "918421412549"
                    );

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "whatsappResponse", response
                    )
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "success", false,
                                    "error", e.getMessage()
                            )
                    );
        }
    }

    // =========================================================
    // /send
    //
    // CUSTOM MESSAGE
    // =========================================================

    @PostMapping("/send")
    public ResponseEntity<?> send(
            @RequestBody WhatsAppRequest request
    ) {

        try {

            String response =
                    whatsappService.sendMessage(

                            request.getPhone(),

                            request.getMessage()

                    );


            return ResponseEntity.ok(
                    Map.of(

                            "success",
                            true,

                            "message",
                            "WhatsApp message sent successfully.",

                            "response",
                            response

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

    @PostMapping("/meter-test")
    public ResponseEntity<?> meterTest() {

        try {

            String response =
                    whatsappService.sendMeterReadingTemplate(

                            "918421412549",

                            "MTR001",

                            "785",

                            "MANUAL"
                    );


            return ResponseEntity.ok(
                    Map.of(

                            "success",
                            true,

                            "whatsappResponse",
                            response
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
}