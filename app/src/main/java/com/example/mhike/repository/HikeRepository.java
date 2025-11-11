package com.example.mhike.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.mhike.data.dao.HikeDao;
import com.example.mhike.data.database.AppDatabase;
import com.example.mhike.data.model.Hike;
import com.example.mhike.data.model.ObservationWithHike;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HikeRepository {
    private HikeDao hikeDao;
    private LiveData<List<Hike>> allHikes;
    private LiveData<Integer> hikeCount;
    private ExecutorService executorService;

    public HikeRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        hikeDao = database.hikeDao();
        allHikes = hikeDao.getAllHikes();
        hikeCount = hikeDao.getHikeCount();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Hike hike) {
        executorService.execute(() -> hikeDao.insert(hike));
    }

    public void update(Hike hike) {
        executorService.execute(() -> hikeDao.update(hike));
    }

    public void delete(Hike hike) {
        executorService.execute(() -> hikeDao.delete(hike));
    }

    public void deleteAll() {
        executorService.execute(() -> hikeDao.deleteAll());
    }

    public LiveData<List<Hike>> getAllHikes() {
        return allHikes;
    }

    public LiveData<Hike> getHikeById(int hikeId) {
        return hikeDao.getHikeById(hikeId);
    }

    public LiveData<List<Hike>> getHikesByType(String type) {
        return hikeDao.getHikesByType(type);
    }

    public LiveData<List<Hike>> searchHikes(String query) {
        return hikeDao.searchHikes(query);
    }

    public LiveData<Integer> getHikeCount() {
        return hikeCount;
    }

    public LiveData<List<ObservationWithHike>> getHikesWithObservations() {
        return hikeDao.getHikesWithObservations();
    }
}