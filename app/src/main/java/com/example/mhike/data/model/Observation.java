package com.example.mhike.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "observations",
        foreignKeys = @ForeignKey(
                entity = Hike.class,
                parentColumns = "id",
                childColumns = "hike_id",
                onDelete = ForeignKey.CASCADE
        ))
public class Observation {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "hike_id", index = true)
    private int hikeId;

    private String observation;
    private String time;
    private String comment;

    // Constructor
    public Observation(int hikeId, String observation, String time, String comment) {
        this.hikeId = hikeId;
        this.observation = observation;
        this.time = time;
        this.comment = comment;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getHikeId() { return hikeId; }
    public void setHikeId(int hikeId) { this.hikeId = hikeId; }

    public String getObservation() { return observation; }
    public void setObservation(String observation) { this.observation = observation; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}