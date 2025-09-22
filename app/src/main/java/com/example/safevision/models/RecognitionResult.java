package com.example.safevision.models;

public class RecognitionResult {
    private String identity;
    private double confidence;
    private String status; // KNOWN, UNKNOWN
    private String timestamp;
    private Location location;

    public static class Location {
        private int x;
        private int y;
        private int width;
        private int height;

        public Location(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        // Getters y Setters
        public int getX() { return x; }
        public void setX(int x) { this.x = x; }

        public int getY() { return y; }
        public void setY(int y) { this.y = y; }

        public int getWidth() { return width; }
        public void setWidth(int width) { this.width = width; }

        public int getHeight() { return height; }
        public void setHeight(int height) { this.height = height; }
    }

    // Getters y Setters
    public String getIdentity() { return identity; }
    public void setIdentity(String identity) { this.identity = identity; }

    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
}