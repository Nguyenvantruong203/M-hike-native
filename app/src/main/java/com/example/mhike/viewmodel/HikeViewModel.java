package com.example.mhike.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mhike.data.model.Hike;
import com.example.mhike.data.model.ObservationWithHike;
import com.example.mhike.repository.HikeRepository;

import java.util.List;

public class HikeViewModel extends AndroidViewModel {
    private HikeRepository repository;
    private LiveData<List<Hike>> allHikes;
    private LiveData<Integer> hikeCount;

    public HikeViewModel(@NonNull Application application) {
        super(application);
        repository = new HikeRepository(application);
        allHikes = repository.getAllHikes();
        hikeCount = repository.getHikeCount();
    }

    public void insert(Hike hike) {
        repository.insert(hike);
    }

    public void update(Hike hike) {
        repository.update(hike);
    }

    public void delete(Hike hike) {
        repository.delete(hike);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public LiveData<List<Hike>> getAllHikes() {
        return allHikes;
    }

    public LiveData<Hike> getHikeById(int hikeId) {
        return repository.getHikeById(hikeId);
    }

    public LiveData<List<Hike>> getHikesByType(String type) {
        return repository.getHikesByType(type);
    }

    public LiveData<List<Hike>> searchHikes(String query) {
        return repository.searchHikes(query);
    }

    public LiveData<Integer> getHikeCount() {
        return hikeCount;
    }

    public LiveData<List<ObservationWithHike>> getHikesWithObservations() {
        return repository.getHikesWithObservations();
    }
}