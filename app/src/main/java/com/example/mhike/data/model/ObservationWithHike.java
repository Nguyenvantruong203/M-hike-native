package com.example.mhike.data.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ObservationWithHike {
    @Embedded
    public Hike hike;

    @Relation(
            parentColumn = "id",
            entityColumn = "hike_id"
    )
    public List<Observation> observations;

    public Hike getHike() {
        return hike;
    }

    public List<Observation> getObservations() {
        return observations;
    }
}