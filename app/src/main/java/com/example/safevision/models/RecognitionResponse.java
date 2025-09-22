package com.example.safevision.models;

import java.util.List;

public class RecognitionResponse {
    private List<RecognitionResult> results;
    private List<String> suspiciousPatterns;
    private String timestamp;

    public List<RecognitionResult> getResults() { return results; }
    public void setResults(List<RecognitionResult> results) { this.results = results; }

    public List<String> getSuspiciousPatterns() { return suspiciousPatterns; }
    public void setSuspiciousPatterns(List<String> suspiciousPatterns) { this.suspiciousPatterns = suspiciousPatterns; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}