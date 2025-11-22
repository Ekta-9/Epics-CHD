package com.ecgcare.backend.ml;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PredictionResponse {

    @JsonProperty("mri_scan_id")
    private String mriScanId;  // <-- IMPORTANT: string now!

    private String prediction;

    @JsonProperty("confidence_score")
    private double confidenceScore;

    private String status;

    public String getMriScanId() {
        return mriScanId;
    }

    public void setMriScanId(String mriScanId) {
        this.mriScanId = mriScanId;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public double getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
