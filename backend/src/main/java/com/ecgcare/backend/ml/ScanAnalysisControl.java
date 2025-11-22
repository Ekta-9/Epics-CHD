package com.ecgcare.backend.ml;

import com.ecgcare.backend.supabase.SupabaseService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/scans")
public class ScanAnalysisControl {

    private final PredictionClient client;
    private final SupabaseService supabaseService;

    public ScanAnalysisControl(PredictionClient client, SupabaseService supabaseService) {
        this.client = client;
        this.supabaseService = supabaseService;
    }

    @PostMapping("/{scanId}/analyze")
    public Map<String, Object> analyzeScan(
            @PathVariable UUID scanId,
            @RequestParam UUID patientId,
            @RequestParam UUID doctorId
    ) {

        // 1️⃣ Call ML model
        PredictionResponse resp = client.getPrediction(scanId);

        // 2️⃣ Save ML result to Supabase table: ml_result
        Map<String, Object> savedRecord = supabaseService.insert(
                "ml_result",
                Map.of(
                        "patient_id", patientId,
                        "scan_id", scanId,
                        "predicted_label", resp.getPrediction(),
                        "class_probs", Map.of("confidence", resp.getConfidenceScore()),
                        "created_by", doctorId
                )
        );

        // 3️⃣ Return the saved record (or ML output)
        return savedRecord;
    }
}
