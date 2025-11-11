package com.example.mhike.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "hikes")
public class Hike {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String location;
    private String date;
    private String parking;
    private String length;
    private String difficulty;
    private String type;
    private String description;
    private String imageUri;

    // Constructor
    public Hike(String name, String location, String date, String parking,
                String length, String difficulty, String type, String description, String imageUri) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.parking = parking;
        this.length = length;
        this.difficulty = difficulty;
        this.type = type;
        this.description = description;
        this.imageUri = imageUri;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getParking() { return parking; }
    public void setParking(String parking) { this.parking = parking; }

    public String getLength() { return length; }
    public void setLength(String length) { this.length = length; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUri() { return imageUri; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }
}