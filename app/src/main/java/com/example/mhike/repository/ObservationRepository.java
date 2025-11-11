package com.example.mhike.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.mhike.data.dao.ObservationDao;
import com.example.mhike.data.database.AppDatabase;
import com.example.mhike.data.model.Observation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ObservationRepository {
    private ObservationDao observationDao;
    private LiveData<Integer> observationCount;
    private ExecutorService executorService;

    public interface OnInsertCompleteListener {
        void onComplete();
    }

    public ObservationRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        observationDao = database.observationDao();
        observationCount = observationDao.getObservationCount();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Observation observation, OnInsertCompleteListener listener) {
        executorService.execute(() -> {
            observationDao.insert(observation);
            if (listener != null) {
                // Callback sau khi insert xong
                new android.os.Handler(android.os.Looper.getMainLooper()).post(listener::onComplete);
            }
        });
    }

    public void insert(Observation observation) {
        executorService.execute(() -> observationDao.insert(observation));
    }

    public void update(Observation observation, OnInsertCompleteListener listener) {
        executorService.execute(() -> {
            observationDao.update(observation);
            if (listener != null) {
                new android.os.Handler(android.os.Looper.getMainLooper()).post(listener::onComplete);
            }
        });
    }

    public void update(Observation observation) {
        executorService.execute(() -> observationDao.update(observation));
    }

    public void delete(Observation observation) {
        executorService.execute(() -> observationDao.delete(observation));
    }

    public void deleteByHikeId(int hikeId) {
        executorService.execute(() -> observationDao.deleteByHikeId(hikeId));
    }

    public void deleteAll() {
        executorService.execute(() -> observationDao.deleteAll());
    }

    public LiveData<List<Observation>> getObservationsByHikeId(int hikeId) {
        return observationDao.getObservationsByHikeId(hikeId);
    }

    public LiveData<Observation> getObservationById(int observationId) {
        return observationDao.getObservationById(observationId);
    }

    public LiveData<Integer> getObservationCount() {
        return observationCount;
    }
}