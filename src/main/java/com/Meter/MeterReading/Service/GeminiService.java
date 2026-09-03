
package com.Meter.MeterReading.Service;

import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class GeminiService {

    private final Client client;

    public GeminiService(
            @Value("${gemini.api.key}") String apiKey) {

        this.client = Client.builder()
                .apiKey(apiKey)
                .build();
    }

    public String readMeter(MultipartFile image)
            throws Exception {

        // Convert uploaded image to bytes
        byte[] imageBytes =
                image.getBytes();

        // Prompt
        String prompt = """
                This image contains an electricity meter.

                Read ONLY the main electricity meter
                reading shown on the large digital display.

                Ignore:
                - voltage
                - units
                - serial numbers
                - manufacturer text
                - labels
                - barcode numbers
                - all other numbers outside the main display

                Return ONLY the meter reading digits.

                For example:
                34545

                Do not return:
                - explanations
                - markdown
                - units
                - extra text
                """;

        // Create content containing
        // text + image
        Content content =
                Content.fromParts(
                        Part.fromText(prompt),
                        Part.fromBytes(
                                imageBytes,
                                image.getContentType()
                        )
                );

        // Call Gemini
        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-3.6-flash",
                        content,
                        null
                );

        // Get response
        String result =
                response.text();

        System.out.println(
                "Gemini raw response: "
                        + result
        );

        // Keep only numbers
        String reading =
                result
                        .replaceAll(
                                "[^0-9]",
                                ""
                        );

        System.out.println(
                "Meter reading: "
                        + reading
        );

        return reading;
    }
}