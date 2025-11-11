package com.example.mhike.ui.observation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mhike.data.model.Observation;
import com.example.mhike.repository.ObservationRepository;

import java.util.List;

public class ObservationViewModel extends AndroidViewModel {
    private ObservationRepository repository;
    private LiveData<Integer> observationCount;

    public ObservationViewModel(@NonNull Application application) {
        super(application);
        repository = new ObservationRepository(application);
        observationCount = repository.getObservationCount();
    }

    public void insert(Observation observation, ObservationRepository.OnInsertCompleteListener listener) {
        repository.insert(observation, listener);
    }

    public void insert(Observation observation) {
        repository.insert(observation);
    }

    public void update(Observation observation, ObservationRepository.OnInsertCompleteListener listener) {
        repository.update(observation, listener);
    }

    public void update(Observation observation) {
        repository.update(observation);
    }

    public void delete(Observation observation) {
        repository.delete(observation);
    }

    public void deleteByHikeId(int hikeId) {
        repository.deleteByHikeId(hikeId);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public LiveData<List<Observation>> getObservationsByHikeId(int hikeId) {
        return repository.getObservationsByHikeId(hikeId);
    }

    public LiveData<Observation> getObservationById(int observationId) {
        return repository.getObservationById(observationId);
    }

    public LiveData<Integer> getObservationCount() {
        return observationCount;
    }
}