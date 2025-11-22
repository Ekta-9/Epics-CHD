package com.ecgcare.backend.ml;

import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PredictionClient {

    private final RestTemplate restTemplate;
    private final String ML_API_URL = "http://localhost:8001/predict";

    public PredictionClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PredictionResponse getPrediction(UUID scanId) {
        Map<String, Object> requestBody = Map.of("mri_scan_id", scanId.toString());

        return restTemplate.postForObject(
                ML_API_URL,
                requestBody,
                PredictionResponse.class
        );
    }
}
