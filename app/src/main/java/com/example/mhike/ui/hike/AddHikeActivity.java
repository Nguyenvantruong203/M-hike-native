package com.example.mhike.ui.hike;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.mhike.R;
import com.example.mhike.data.model.Hike;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddHikeActivity extends AppCompatActivity {

    private TextInputEditText etName, etLocation, etDate, etLength, etDescription;
    private AutoCompleteTextView actvParking, actvDifficulty, actvType;
    private Button btnSave;
    private ImageButton btnBack;
    private TextView tvTitle;
    private HikeViewModel hikeViewModel;
    private Hike currentHike;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hike);

        initViews();
        initViewModel();
        setupDropdowns();
        checkEditMode();
        setupListeners();
    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        etLocation = findViewById(R.id.etLocation);
        etDate = findViewById(R.id.etDate);
        etLength = findViewById(R.id.etLength);
        etDescription = findViewById(R.id.etDescription);

        actvParking = findViewById(R.id.actvParking);
        actvDifficulty = findViewById(R.id.actvDifficulty);
        actvType = findViewById(R.id.actvType);

        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        tvTitle = findViewById(R.id.tvTitle);
    }

    private void initViewModel() {
        hikeViewModel = new ViewModelProvider(this).get(HikeViewModel.class);
    }

    private void setupDropdowns() {
        // Parking options
        String[] parkingOptions = {"Yes", "No"};
        ArrayAdapter<String> parkingAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, parkingOptions);
        actvParking.setAdapter(parkingAdapter);

        // Difficulty options
        String[] difficultyOptions = {"Easy", "Moderate", "Hard"};
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, difficultyOptions);
        actvDifficulty.setAdapter(difficultyAdapter);

        // Type options
        String[] typeOptions = {"Trail Walk", "Day Hike", "Trekking", "Summit Hike", "Nature Trail"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, typeOptions);
        actvType.setAdapter(typeAdapter);
    }

    private void checkEditMode() {
        int hikeId = getIntent().getIntExtra("HIKE_ID", -1);
        if (hikeId != -1) {
            isEditMode = true;
            tvTitle.setText("Edit Hike");
            btnSave.setText("Update Hike");
            loadHikeData(hikeId);
        }
    }

    private void loadHikeData(int hikeId) {
        hikeViewModel.getHikeById(hikeId).observe(this, hike -> {
            if (hike != null) {
                currentHike = hike;
                etName.setText(hike.getName());
                etLocation.setText(hike.getLocation());
                etDate.setText(hike.getDate());
                actvParking.setText(hike.getParking(), false);
                actvDifficulty.setText(hike.getDifficulty(), false);
                actvType.setText(hike.getType(), false);
                etLength.setText(hike.getLength());
                etDescription.setText(hike.getDescription());
            }
        });
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        etDate.setOnClickListener(v -> showDatePicker());

        btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                showConfirmationDialog();
            }
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            etDate.setText(sdf.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private boolean validateInputs() {
        String name = etName.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String parking = actvParking.getText().toString().trim();
        String difficulty = actvDifficulty.getText().toString().trim();
        String type = actvType.getText().toString().trim();
        String length = etLength.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Name is required");
            etName.requestFocus();
            return false;
        }

        if (location.isEmpty()) {
            etLocation.setError("Location is required");
            etLocation.requestFocus();
            return false;
        }

        if (date.isEmpty()) {
            etDate.setError("Date is required");
            etDate.requestFocus();
            return false;
        }

        if (parking.isEmpty()) {
            actvParking.setError("Parking is required");
            actvParking.requestFocus();
            return false;
        }

        if (difficulty.isEmpty()) {
            actvDifficulty.setError("Difficulty is required");
            actvDifficulty.requestFocus();
            return false;
        }

        if (type.isEmpty()) {
            actvType.setError("Type is required");
            actvType.requestFocus();
            return false;
        }

        if (length.isEmpty()) {
            etLength.setError("Length is required");
            etLength.requestFocus();
            return false;
        }

        return true;
    }

    private void showConfirmationDialog() {
        String message = isEditMode ?
                "Are you sure you want to update this hike?" :
                "Are you sure you want to save this hike?";

        new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage(message)
                .setPositiveButton("Yes", (dialog, which) -> saveHike())
                .setNegativeButton("No", null)
                .show();
    }

    private void saveHike() {
        String name = etName.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String parking = actvParking.getText().toString().trim();
        String difficulty = actvDifficulty.getText().toString().trim();
        String type = actvType.getText().toString().trim();
        String length = etLength.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (isEditMode && currentHike != null) {
            currentHike.setName(name);
            currentHike.setLocation(location);
            currentHike.setDate(date);
            currentHike.setParking(parking);
            currentHike.setDifficulty(difficulty);
            currentHike.setType(type);
            currentHike.setLength(length);
            currentHike.setDescription(description);

            hikeViewModel.update(currentHike);
            Toast.makeText(this, "Hike updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Hike newHike = new Hike(name, location, date, parking, length,
                    difficulty, type, description, null);
            hikeViewModel.insert(newHike);
            Toast.makeText(this, "Hike saved successfully!", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}