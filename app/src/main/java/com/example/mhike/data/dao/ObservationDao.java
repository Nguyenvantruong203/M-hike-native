package com.example.mhike.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mhike.data.model.Observation;

import java.util.List;

@Dao
public interface ObservationDao {
    @Insert
    long insert(Observation observation);

    @Update
    void update(Observation observation);

    @Delete
    void delete(Observation observation);

    @Query("DELETE FROM observations WHERE hike_id = :hikeId")
    void deleteByHikeId(int hikeId);

    @Query("DELETE FROM observations")
    void deleteAll();

    @Query("SELECT * FROM observations WHERE hike_id = :hikeId ORDER BY id DESC")
    LiveData<List<Observation>> getObservationsByHikeId(int hikeId);

    @Query("SELECT * FROM observations WHERE id = :observationId")
    LiveData<Observation> getObservationById(int observationId);

    @Query("SELECT COUNT(*) FROM observations")
    LiveData<Integer> getObservationCount();
}