package com.example.mhike.ui.hike;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.mhike.R;
import com.example.mhike.data.model.Hike;
import com.example.mhike.ui.observation.AddObservationActivity;
import com.example.mhike.viewmodel.HikeViewModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class HikeDetailActivity extends AppCompatActivity {

    private ImageView ivHikeImage;
    private TextView tvHikeName, tvLocation, tvDistance, tvDifficulty, tvParking, tvDescription;
    private Button btnStart;
    private ImageButton btnBack, btnFavorite;
    private CollapsingToolbarLayout collapsingToolbar;

    private HikeViewModel hikeViewModel;
    private Hike currentHike;
    private int hikeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_detail);

        initViews();
        initViewModel();
        getHikeData();
        setupListeners();
    }

    private void initViews() {
        ivHikeImage = findViewById(R.id.ivHikeImage);
        tvHikeName = findViewById(R.id.tvHikeName);
        tvLocation = findViewById(R.id.tvLocation);
        tvDistance = findViewById(R.id.tvDistance);
        tvDifficulty = findViewById(R.id.tvDifficulty);
        tvParking = findViewById(R.id.tvParking);
        tvDescription = findViewById(R.id.tvDescription);
        btnStart = findViewById(R.id.btnStart);
        btnBack = findViewById(R.id.btnBack);
        btnFavorite = findViewById(R.id.btnFavorite);
        collapsingToolbar = findViewById(R.id.collapsingToolbar);
    }

    private void initViewModel() {
        hikeViewModel = new ViewModelProvider(this).get(HikeViewModel.class);
    }

    private void getHikeData() {
        hikeId = getIntent().getIntExtra("HIKE_ID", -1);

        if (hikeId == -1) {
            Toast.makeText(this, "Error loading hike", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        hikeViewModel.getHikeById(hikeId).observe(this, hike -> {
            if (hike != null) {
                currentHike = hike;
                displayHikeDetails(hike);
            }
        });
    }

    private void displayHikeDetails(Hike hike) {
        // Set toolbar title
        collapsingToolbar.setTitle(hike.getName());

        // Set text fields
        tvHikeName.setText(hike.getName());
        tvLocation.setText(hike.getLocation());
        tvDistance.setText(hike.getLength());
        tvDifficulty.setText(hike.getDifficulty());
        tvParking.setText(hike.getParking());

        // Set description
        if (hike.getDescription() != null && !hike.getDescription().isEmpty()) {
            tvDescription.setText(hike.getDescription());
        } else {
            tvDescription.setText("No description available.");
        }

        // Load image
        if (hike.getImageUri() != null && !hike.getImageUri().isEmpty()) {
            try {
                ivHikeImage.setImageURI(Uri.parse(hike.getImageUri()));
            } catch (Exception e) {
                ivHikeImage.setImageResource(R.drawable.image_hike_1);
            }
        } else {
            ivHikeImage.setImageResource(R.drawable.image_hike_1);
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnFavorite.setOnClickListener(v -> {
            // Toggle favorite (implement if needed)
            Toast.makeText(this, "Favorite feature coming soon!", Toast.LENGTH_SHORT).show();
        });

        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(HikeDetailActivity.this, AddObservationActivity.class);
            intent.putExtra("HIKE_ID", hikeId);
            startActivity(intent);
        });
    }
}