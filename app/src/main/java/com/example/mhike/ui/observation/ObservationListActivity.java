package com.example.mhike.ui.observation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mhike.R;
import com.example.mhike.data.model.Observation;
import com.example.mhike.viewmodel.HikeViewModel;
import com.example.mhike.ui.hike.MainActivity;
import com.example.mhike.ui.profile.ProfileActivity;
import com.example.mhike.ui.search.SearchActivity;
import com.example.mhike.viewmodel.ObservationViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ObservationListActivity extends AppCompatActivity implements GroupedObservationAdapter.OnObservationClickListener {

    private RecyclerView rvObservations;
    private TextView tvTitle;
    private ImageButton btnBack, btnDeleteAll;
    private LinearLayout layoutEmptyState;
    private BottomNavigationView bottomNavigation;

    private GroupedObservationAdapter adapter;
    private ObservationViewModel observationViewModel;
    private HikeViewModel hikeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation_list);

        initViews();
        initViewModels();
        setupRecyclerView();
        setupListeners();
        loadObservations();
    }

    private void initViews() {
        rvObservations = findViewById(R.id.rvObservations);
        tvTitle = findViewById(R.id.tvHikeName);
        btnBack = findViewById(R.id.btnBack);
        btnDeleteAll = findViewById(R.id.btnDeleteAll);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Hide hike info card since we're showing all observations
        View cardHikeInfo = findViewById(R.id.cardHikeInfo);
        if (cardHikeInfo != null) {
            cardHikeInfo.setVisibility(View.GONE);
        }
    }

    private void initViewModels() {
        observationViewModel = new ViewModelProvider(this).get(ObservationViewModel.class);
        hikeViewModel = new ViewModelProvider(this).get(HikeViewModel.class);
    }

    private void setupRecyclerView() {
        adapter = new GroupedObservationAdapter(this);
        rvObservations.setLayoutManager(new LinearLayoutManager(this));
        rvObservations.setAdapter(adapter);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnDeleteAll.setOnClickListener(v -> showDeleteAllDialog());

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
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }

            return false;
        });
    }

    private void loadObservations() {
        tvTitle.setText("My observations");

        hikeViewModel.getHikesWithObservations().observe(this, hikesWithObservations -> {
            if (hikesWithObservations == null || hikesWithObservations.isEmpty()) {
                layoutEmptyState.setVisibility(View.VISIBLE);
                rvObservations.setVisibility(View.GONE);
            } else {
                layoutEmptyState.setVisibility(View.GONE);
                rvObservations.setVisibility(View.VISIBLE);
                adapter.setData(hikesWithObservations);
            }
        });
    }

    @Override
    public void onEditClick(Observation observation) {
        Intent intent = new Intent(this, AddObservationActivity.class);
        intent.putExtra("OBSERVATION_ID", observation.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Observation observation) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Observation")
                .setMessage("Are you sure you want to delete this observation?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    observationViewModel.delete(observation);
                    Toast.makeText(this, "Observation deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showDeleteAllDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete All Observations")
                .setMessage("Are you sure you want to delete ALL observations from all hikes?")
                .setPositiveButton("Delete All", (dialog, which) -> {
                    observationViewModel.deleteAll();
                    Toast.makeText(this, "All observations deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigation.setSelectedItemId(R.id.nav_observations);
    }
}