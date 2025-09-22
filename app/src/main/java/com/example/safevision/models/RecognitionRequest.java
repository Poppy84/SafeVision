package com.example.safevision.models;

public class RecognitionRequest {
    private String image; // Base64

    public RecognitionRequest(String image) {
        this.image = image;
    }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}