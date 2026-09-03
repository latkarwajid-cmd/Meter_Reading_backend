package com.Meter.MeterReading.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class WhatsAppService {

    private final RestClient restClient;

    // =========================================================
    // META WHATSAPP CONFIGURATION (Loaded from properties)
    // =========================================================

    private final String accessToken;
    private final String phoneNumberId;
    private final String graphUrl;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public WhatsAppService(
            @Value("${whatsapp.access-token:${whatsapp_token:}}") String accessToken,
            @Value("${whatsapp.phone-number-id:1278590508671882}") String phoneNumberId,
            @Value("${whatsapp.graph.url:https://graph.facebook.com/v23.0}") String graphUrl
    ) {
        this.accessToken = accessToken;
        this.phoneNumberId = phoneNumberId;
        this.graphUrl = graphUrl;

        this.restClient =
                RestClient.builder()
                        .build();
    }


    // =========================================================
    // SEND NORMAL TEXT MESSAGE
    // =========================================================

    public String sendMessage(
            String phone,
            String message
    ) {

        if (phone == null ||
                phone.isBlank()) {

            throw new IllegalArgumentException(
                    "Phone number is required."
            );
        }


        if (message == null ||
                message.isBlank()) {

            throw new IllegalArgumentException(
                    "Message is required."
            );
        }


        // Remove +, spaces, -, etc.
        phone =
                phone.replaceAll(
                        "[^0-9]",
                        ""
                );


        System.out.println(
                "======================================"
        );

        System.out.println(
                "WHATSAPP SEND"
        );

        System.out.println(
                "Phone: " + phone
        );

        System.out.println(
                "Message: " + message
        );

        System.out.println(
                "======================================"
        );


        // =====================================================
        // TEXT
        // =====================================================

        Map<String, Object> text =
                new HashMap<>();

        text.put(
                "body",
                message
        );


        // =====================================================
        // REQUEST BODY
        // =====================================================

        Map<String, Object> body =
                new HashMap<>();

        body.put(
                "messaging_product",
                "whatsapp"
        );

        body.put(
                "recipient_type",
                "individual"
        );

        body.put(
                "to",
                phone
        );

        body.put(
                "type",
                "text"
        );

        body.put(
                "text",
                text
        );


        // =====================================================
        // SEND TO META
        // =====================================================

        try {

            String response =
                    restClient.post()

                            .uri(
                                    graphUrl
                                            + "/"
                                            + phoneNumberId
                                            + "/messages"
                            )

                            .header(
                                    "Authorization",
                                    "Bearer "
                                            + accessToken
                            )

                            .contentType(
                                    MediaType.APPLICATION_JSON
                            )

                            .body(body)

                            .retrieve()

                            .body(String.class);


            System.out.println(
                    "WHATSAPP SUCCESS:"
            );

            System.out.println(response);


            return response;


        } catch (Exception e) {

            System.out.println(
                    "WHATSAPP ERROR:"
            );

            System.out.println(
                    e.getMessage()
            );

            throw new RuntimeException(
                    "WhatsApp message failed: "
                            + e.getMessage()
            );
        }
    }


    // =========================================================
    // SEND HELLO WORLD TEMPLATE
    //
    // Use this ONLY for testing Meta delivery.
    // =========================================================

    public String sendHelloTemplate(
            String phone
    ) {

        if (phone == null ||
                phone.isBlank()) {

            throw new IllegalArgumentException(
                    "Phone number is required."
            );
        }


        phone =
                phone.replaceAll(
                        "[^0-9]",
                        ""
                );


        // =====================================================
        // LANGUAGE
        // =====================================================

        Map<String, Object> language =
                new HashMap<>();

        language.put(
                "code",
                "en_US"
        );


        // =====================================================
        // TEMPLATE
        // =====================================================

        Map<String, Object> template =
                new HashMap<>();

        template.put(
                "name",
                "hello_world"
        );

        template.put(
                "language",
                language
        );


        // =====================================================
        // REQUEST BODY
        // =====================================================

        Map<String, Object> body =
                new HashMap<>();

        body.put(
                "messaging_product",
                "whatsapp"
        );

        body.put(
                "to",
                phone
        );

        body.put(
                "type",
                "template"
        );

        body.put(
                "template",
                template
        );


        // =====================================================
        // SEND TO META
        // =====================================================

        try {

            String response =
                    restClient.post()

                            .uri(
                                    graphUrl
                                            + "/"
                                            + phoneNumberId
                                            + "/messages"
                            )

                            .header(
                                    "Authorization",
                                    "Bearer "
                                            + accessToken
                            )

                            .contentType(
                                    MediaType.APPLICATION_JSON
                            )

                            .body(body)

                            .retrieve()

                            .body(String.class);


            System.out.println(
                    "WHATSAPP TEMPLATE SUCCESS:"
            );

            System.out.println(response);


            return response;


        } catch (Exception e) {

            System.out.println(
                    "WHATSAPP TEMPLATE ERROR:"
            );

            System.out.println(
                    e.getMessage()
            );

            throw new RuntimeException(
                    "WhatsApp template failed: "
                            + e.getMessage()
            );
        }
    }

    public String sendMeterReadingTemplate(
            String phone,
            String meterNumber,
            String reading,
            String inputMethod
    ) {

        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException(
                    "Phone number is required."
            );
        }

        phone = phone.replaceAll("[^0-9]", "");


        // =====================================================
        // LANGUAGE
        // =====================================================

        Map<String, Object> language =
                new HashMap<>();

        language.put(
                "code",
                "en_US"
        );


        // =====================================================
        // TEMPLATE PARAMETERS
        // =====================================================

        Map<String, Object> parameter1 =
                new HashMap<>();

        parameter1.put(
                "type",
                "text"
        );

        parameter1.put(
                "text",
                meterNumber
        );


        Map<String, Object> parameter2 =
                new HashMap<>();

        parameter2.put(
                "type",
                "text"
        );

        parameter2.put(
                "text",
                reading
        );


        Map<String, Object> parameter3 =
                new HashMap<>();

        parameter3.put(
                "type",
                "text"
        );

        parameter3.put(
                "text",
                inputMethod
        );


        // =====================================================
        // PARAMETERS LIST
        // =====================================================

        Map<String, Object> parameters =
                new HashMap<>();

        parameters.put(
                "type",
                "body"
        );

        parameters.put(
                "parameters",
                java.util.List.of(
                        parameter1,
                        parameter2,
                        parameter3
                )
        );


        // =====================================================
        // TEMPLATE
        // =====================================================

        Map<String, Object> template =
                new HashMap<>();

        template.put(
                "name",
                "meter_reading"
        );

        template.put(
                "language",
                language
        );

        template.put(
                "components",
                java.util.List.of(
                        parameters
                )
        );


        // =====================================================
        // REQUEST BODY
        // =====================================================

        Map<String, Object> body =
                new HashMap<>();

        body.put(
                "messaging_product",
                "whatsapp"
        );

        body.put(
                "to",
                phone
        );

        body.put(
                "type",
                "template"
        );

        body.put(
                "template",
                template
        );


        // =====================================================
        // SEND
        // =====================================================

        try {

            String response =
                    restClient.post()

                            .uri(
                                    graphUrl
                                            + "/"
                                            + phoneNumberId
                                            + "/messages"
                            )

                            .header(
                                    "Authorization",
                                    "Bearer "
                                            + accessToken
                            )

                            .contentType(
                                    MediaType.APPLICATION_JSON
                            )

                            .body(body)

                            .retrieve()

                            .body(String.class);


            System.out.println(
                    "METER READING WHATSAPP SUCCESS:"
            );

            System.out.println(response);


            return response;


        } catch (Exception e) {

            System.out.println(
                    "METER READING WHATSAPP ERROR:"
            );

            System.out.println(
                    e.getMessage()
            );

            throw new RuntimeException(
                    "Meter reading WhatsApp message failed: "
                            + e.getMessage()
            );
        }
    }
}