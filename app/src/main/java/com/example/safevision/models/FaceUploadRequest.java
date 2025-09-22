package com.example.safevision.models;

public class FaceUploadRequest {
    private String image; // Base64
    private String personName;
    private String relationship; // family, friend, guest, other

    public FaceUploadRequest(String image, String personName, String relationship) {
        this.image = image;
        this.personName = personName;
        this.relationship = relationship;
    }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getPersonName() { return personName; }
    public void setPersonName(String personName) { this.personName = personName; }

    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }
}