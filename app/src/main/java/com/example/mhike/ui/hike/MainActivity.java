package com.example.mhike.ui.hike;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mhike.R;
import com.example.mhike.data.model.Hike;
import com.example.mhike.ui.observation.ObservationListActivity;
import com.example.mhike.ui.profile.ProfileActivity;
import com.example.mhike.ui.search.SearchActivity;
import com.example.mhike.viewmodel.HikeViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements HikeAdapter.OnHikeClickListener {

    private RecyclerView rvPopularHikes, rvAllHikes;
    private HikeAdapter popularHikesAdapter, allHikesAdapter;
    private FloatingActionButton fabAddHike;
    private ImageButton btnDeleteAll;
    private BottomNavigationView bottomNavigation;
    private HikeViewModel hikeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initViewModel();
        setupRecyclerViews();
        setupListeners();
        observeData();
    }

    private void initViews() {
        rvPopularHikes = findViewById(R.id.rvPopularHikes);
        rvAllHikes = findViewById(R.id.rvAllHikes);
        fabAddHike = findViewById(R.id.fabAddHike);
        btnDeleteAll = findViewById(R.id.btnDeleteAll);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void initViewModel() {
        hikeViewModel = new ViewModelProvider(this).get(HikeViewModel.class);
    }

    private void setupRecyclerViews() {
        // Popular Hikes (Horizontal)
        popularHikesAdapter = new HikeAdapter(this, true);
        rvPopularHikes.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        rvPopularHikes.setAdapter(popularHikesAdapter);

        // All Hikes (Vertical)
        allHikesAdapter = new HikeAdapter(this, false);
        rvAllHikes.setLayoutManager(new LinearLayoutManager(this));
        rvAllHikes.setAdapter(allHikesAdapter);
    }

    private void setupListeners() {
        fabAddHike.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddHikeActivity.class);
            startActivity(intent);
        });

        btnDeleteAll.setOnClickListener(v -> showDeleteAllDialog());

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_search) {
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            } else if (itemId == R.id.nav_observations) {
                startActivity(new Intent(this, ObservationListActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }

            return false;
        });
    }

    private void observeData() {
        hikeViewModel.getAllHikes().observe(this, hikes -> {
            if (hikes != null) {
                // Get top 3 for popular hikes
                if (hikes.size() > 0) {
                    int popularCount = Math.min(hikes.size(), 3);
                    popularHikesAdapter.setHikes(hikes.subList(0, popularCount));
                }

                // All hikes
                allHikesAdapter.setHikes(hikes);
            }
        });
    }

    @Override
    public void onHikeClick(Hike hike) {
        Intent intent = new Intent(this, HikeDetailActivity.class);
        intent.putExtra("HIKE_ID", hike.getId());
        startActivity(intent);
    }

    @Override
    public void onEditClick(Hike hike) {
        Intent intent = new Intent(this, AddHikeActivity.class);
        intent.putExtra("HIKE_ID", hike.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Hike hike) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Hike")
                .setMessage("Are you sure you want to delete \"" + hike.getName() + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    hikeViewModel.delete(hike);
                    Toast.makeText(this, "Hike deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showDeleteAllDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete All Hikes")
                .setMessage("Are you sure you want to delete all hikes? This action cannot be undone.")
                .setPositiveButton("Delete All", (dialog, which) -> {
                    hikeViewModel.deleteAll();
                    Toast.makeText(this, "All hikes deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigation.setSelectedItemId(R.id.nav_home);
    }
}