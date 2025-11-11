package com.example.mhike.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.mhike.R;
import com.example.mhike.viewmodel.HikeViewModel;
import com.example.mhike.ui.hike.MainActivity;
import com.example.mhike.ui.observation.ObservationListActivity;
import com.example.mhike.viewmodel.ObservationViewModel;
import com.example.mhike.ui.search.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvEmail, tvTotalHikes, tvTotalObservations;
    private Button btnAboutApp;
    private BottomNavigationView bottomNavigation;

    private HikeViewModel hikeViewModel;
    private ObservationViewModel observationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
        initViewModels();
        loadStats();
        setupListeners();
    }

    private void initViews() {
        tvEmail = findViewById(R.id.tvEmail);
        tvTotalHikes = findViewById(R.id.tvTotalHikes);
        tvTotalObservations = findViewById(R.id.tvTotalObservations);
        btnAboutApp = findViewById(R.id.btnAboutApp);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void initViewModels() {
        hikeViewModel = new ViewModelProvider(this).get(HikeViewModel.class);
        observationViewModel = new ViewModelProvider(this).get(ObservationViewModel.class);
    }

    private void loadStats() {
        // Load total hikes
        hikeViewModel.getHikeCount().observe(this, count -> {
            if (count != null) {
                tvTotalHikes.setText(String.valueOf(count));
            } else {
                tvTotalHikes.setText("0");
            }
        });

        // Load total observations
        observationViewModel.getObservationCount().observe(this, count -> {
            if (count != null) {
                tvTotalObservations.setText(String.valueOf(count));
            } else {
                tvTotalObservations.setText("0");
            }
        });
    }

    private void setupListeners() {
        btnAboutApp.setOnClickListener(v -> showAboutDialog());

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_search) {
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            } else if (itemId == R.id.nav_observations) {
                startActivity(new Intent(this, ObservationListActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                return true;
            }

            return false;
        });
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("About M-Hike")
                .setMessage("M-Hike - Your Hiking Companion\n\n" +
                        "Version: 1.0.0\n\n" +
                        "M-Hike is a comprehensive hiking tracker app that helps you:\n" +
                        "• Track and manage your hiking adventures\n" +
                        "• Record observations during hikes\n" +
                        "• Search and filter hikes by type\n" +
                        "• Store photos and details of each hike\n\n" +
                        "Developed with ❤️ for hiking enthusiasts\n\n" +
                        "Built using:\n" +
                        "• Java (Native Android)\n" +
                        "• MVVM Architecture\n" +
                        "• Room Database\n" +
                        "• Material Design\n\n" +
                        "© 2025 M-Hike. All rights reserved.")
                .setPositiveButton("OK", null)
                .setNeutralButton("Contact Developer", (dialog, which) -> {
                    // Open email or contact
                    showContactDialog();
                })
                .show();
    }

    private void showContactDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Contact Developer")
                .setMessage("Email: trongnguyen@gmail.com\n\n" +
                        "Feel free to reach out for:\n" +
                        "• Bug reports\n" +
                        "• Feature requests\n" +
                        "• General feedback")
                .setPositiveButton("OK", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigation.setSelectedItemId(R.id.nav_profile);
    }
}