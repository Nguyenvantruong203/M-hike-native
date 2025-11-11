package com.example.mhike.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.mhike.data.model.Hike;
import com.example.mhike.data.model.ObservationWithHike;

import java.util.List;

@Dao
public interface HikeDao {
    @Insert
    long insert(Hike hike);

    @Update
    void update(Hike hike);

    @Delete
    void delete(Hike hike);

    @Query("DELETE FROM hikes")
    void deleteAll();

    @Query("SELECT * FROM hikes ORDER BY id DESC")
    LiveData<List<Hike>> getAllHikes();

    @Query("SELECT * FROM hikes WHERE id = :hikeId")
    LiveData<Hike> getHikeById(int hikeId);

    @Query("SELECT * FROM hikes WHERE type = :type ORDER BY id DESC")
    LiveData<List<Hike>> getHikesByType(String type);

    @Query("SELECT * FROM hikes WHERE name LIKE '%' || :query || '%' OR location LIKE '%' || :query || '%' ORDER BY id DESC")
    LiveData<List<Hike>> searchHikes(String query);

    @Query("SELECT COUNT(*) FROM hikes")
    LiveData<Integer> getHikeCount();

    @Transaction
    @Query("SELECT * FROM hikes WHERE id IN (SELECT DISTINCT hike_id FROM observations) ORDER BY id DESC")
    LiveData<List<ObservationWithHike>> getHikesWithObservations();
}