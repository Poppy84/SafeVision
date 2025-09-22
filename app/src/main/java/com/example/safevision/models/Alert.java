package com.example.safevision.models;

import java.util.Date;

public class Alert {
    private int id;
    private String alertType; // unknown_face, suspicious_behavior, motion_detection
    private String severity; // low, medium, high, critical
    private String description;
    private double confidenceScore;
    private String location;
    private String imagePath;
    private boolean isFalsePositive;
    private boolean isResolved;
    private Date resolvedAt;
    private String resolvedNotes;
    private Date createdAt;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(double confidenceScore) { this.confidenceScore = confidenceScore; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public boolean isFalsePositive() { return isFalsePositive; }
    public void setFalsePositive(boolean falsePositive) { isFalsePositive = falsePositive; }

    public boolean isResolved() { return isResolved; }
    public void setResolved(boolean resolved) { isResolved = resolved; }

    public Date getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(Date resolvedAt) { this.resolvedAt = resolvedAt; }

    public String getResolvedNotes() { return resolvedNotes; }
    public void setResolvedNotes(String resolvedNotes) { this.resolvedNotes = resolvedNotes; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}