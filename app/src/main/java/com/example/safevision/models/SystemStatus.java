package com.example.safevision.models;

public class SystemStatus {
    private boolean facialRecognitionLoaded;
    private int knownFacesCount;
    private boolean modelTrained;
    private Settings settings;

    public boolean isFacialRecognitionLoaded() { return facialRecognitionLoaded; }
    public void setFacialRecognitionLoaded(boolean facialRecognitionLoaded) { this.facialRecognitionLoaded = facialRecognitionLoaded; }

    public int getKnownFacesCount() { return knownFacesCount; }
    public void setKnownFacesCount(int knownFacesCount) { this.knownFacesCount = knownFacesCount; }

    public boolean isModelTrained() { return modelTrained; }
    public void setModelTrained(boolean modelTrained) { this.modelTrained = modelTrained; }

    public Settings getSettings() { return settings; }
    public void setSettings(Settings settings) { this.settings = settings; }
}