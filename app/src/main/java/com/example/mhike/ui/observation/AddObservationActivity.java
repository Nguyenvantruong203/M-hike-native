package com.example.mhike.ui.observation;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.mhike.R;
import com.example.mhike.data.model.Observation;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddObservationActivity extends AppCompatActivity {

    private TextInputEditText etObservation, etTime, etComment;
    private Button btnSave;
    private ImageButton btnBack;
    private TextView tvTitle, tvAboutApp;

    private ObservationViewModel observationViewModel;
    private Observation currentObservation;
    private int hikeId;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_observation);

        initViews();
        initViewModel();
        checkEditMode();
        setupListeners();
    }

    private void initViews() {
        etObservation = findViewById(R.id.etObservation);
        etTime = findViewById(R.id.etTime);
        etComment = findViewById(R.id.etComment);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        tvTitle = findViewById(R.id.tvTitle);
    }

    private void initViewModel() {
        observationViewModel = new ViewModelProvider(this).get(ObservationViewModel.class);
    }

    private void checkEditMode() {
        hikeId = getIntent().getIntExtra("HIKE_ID", -1);
        int observationId = getIntent().getIntExtra("OBSERVATION_ID", -1);

        if (observationId != -1) {
            isEditMode = true;
            tvTitle.setText("Edit Observation");
            loadObservationData(observationId);
        }

        if (hikeId == -1 && !isEditMode) {
            Toast.makeText(this, "Error: No hike selected", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadObservationData(int observationId) {
        observationViewModel.getObservationById(observationId).observe(this, observation -> {
            if (observation != null) {
                currentObservation = observation;
                hikeId = observation.getHikeId();
                etObservation.setText(observation.getObservation());
                etTime.setText(observation.getTime());
                etComment.setText(observation.getComment());
            }
        });
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        etTime.setOnClickListener(v -> showTimePicker());

        btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                saveObservation();
            }
        });
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        new TimePickerDialog(this, (view, hourOfDay, minuteOfHour) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minuteOfHour);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            etTime.setText(sdf.format(calendar.getTime()));
        }, hour, minute, true).show();
    }

    private boolean validateInputs() {
        String observation = etObservation.getText().toString().trim();
        String time = etTime.getText().toString().trim();

        if (observation.isEmpty()) {
            etObservation.setError("Observation is required");
            etObservation.requestFocus();
            return false;
        }

        if (time.isEmpty()) {
            etTime.setError("Time is required");
            etTime.requestFocus();
            return false;
        }

        return true;
    }

    private void saveObservation() {
        String observationText = etObservation.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String comment = etComment.getText().toString().trim();

        if (isEditMode && currentObservation != null) {
            currentObservation.setObservation(observationText);
            currentObservation.setTime(time);
            currentObservation.setComment(comment);

            observationViewModel.update(currentObservation, () -> {
                Toast.makeText(AddObservationActivity.this, "Observation updated successfully!", Toast.LENGTH_SHORT).show();
                navigateToObservationList();
            });
        } else {
            Observation newObservation = new Observation(hikeId, observationText, time, comment);
            observationViewModel.insert(newObservation, () -> {
                Toast.makeText(AddObservationActivity.this, "Observation saved successfully!", Toast.LENGTH_SHORT).show();
                navigateToObservationList();
            });
        }
    }

    private void navigateToObservationList() {
        Intent intent = new Intent(AddObservationActivity.this, ObservationListActivity.class);
        intent.putExtra("HIKE_ID", hikeId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}