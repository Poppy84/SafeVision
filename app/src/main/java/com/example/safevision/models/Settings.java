package com.example.safevision.models;

import java.util.Date;

public class Settings {
    private boolean facialRecognitionEnabled;
    private boolean motionDetectionEnabled;
    private boolean notificationsEnabled;
    private int alertSensitivity; // 0-100
    private Date updatedAt;

    public Settings() {
        // Valores por defecto
        this.facialRecognitionEnabled = true;
        this.motionDetectionEnabled = true;
        this.notificationsEnabled = true;
        this.alertSensitivity = 70;
    }

    // Getters y Setters
    public boolean isFacialRecognitionEnabled() { return facialRecognitionEnabled; }
    public void setFacialRecognitionEnabled(boolean facialRecognitionEnabled) { this.facialRecognitionEnabled = facialRecognitionEnabled; }

    public boolean isMotionDetectionEnabled() { return motionDetectionEnabled; }
    public void setMotionDetectionEnabled(boolean motionDetectionEnabled) { this.motionDetectionEnabled = motionDetectionEnabled; }

    public boolean isNotificationsEnabled() { return notificationsEnabled; }
    public void setNotificationsEnabled(boolean notificationsEnabled) { this.notificationsEnabled = notificationsEnabled; }

    public int getAlertSensitivity() { return alertSensitivity; }
    public void setAlertSensitivity(int alertSensitivity) { this.alertSensitivity = alertSensitivity; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}