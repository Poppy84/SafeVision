package com.example.safevision.models;

import java.util.Date;

public class KnownFace {
    private int id;
    private String personName;
    private String relationship; // family, friend, guest, other
    private int imagesCount;
    private boolean isActive;
    private Date createdAt;

    public KnownFace(String personName, String relationship) {
        this.personName = personName;
        this.relationship = relationship;
        this.imagesCount = 0;
        this.isActive = true;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPersonName() { return personName; }
    public void setPersonName(String personName) { this.personName = personName; }

    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }

    public int getImagesCount() { return imagesCount; }
    public void setImagesCount(int imagesCount) { this.imagesCount = imagesCount; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}