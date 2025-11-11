package com.example.mhike.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mhike.R;
import com.example.mhike.data.model.Hike;
import com.example.mhike.ui.hike.HikeAdapter;
import com.example.mhike.ui.hike.HikeDetailActivity;
import com.example.mhike.ui.hike.HikeViewModel;
import com.example.mhike.ui.hike.MainActivity;
import com.example.mhike.ui.observation.ObservationListActivity;
import com.example.mhike.ui.profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements HikeAdapter.OnHikeClickListener {

    private SearchView searchView;
    private ChipGroup chipGroup;
    private RecyclerView rvSearchResults;
    private LinearLayout layoutEmptyState;
    private BottomNavigationView bottomNavigation;

    private HikeAdapter searchAdapter;
    private HikeViewModel hikeViewModel;
    private String currentFilter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();
        initViewModel();
        setupRecyclerView();
        setupListeners();
        loadAllHikes();
    }

    private void initViews() {
        searchView = findViewById(R.id.searchView);
        chipGroup = findViewById(R.id.chipGroup);
        rvSearchResults = findViewById(R.id.rvSearchResults);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void initViewModel() {
        hikeViewModel = new ViewModelProvider(this).get(HikeViewModel.class);
    }

    private void setupRecyclerView() {
        searchAdapter = new HikeAdapter(this, false);
        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        rvSearchResults.setAdapter(searchAdapter);
    }

    private void setupListeners() {
        // Search listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    if (currentFilter != null) {
                        filterByType(currentFilter);
                    } else {
                        loadAllHikes();
                    }
                } else {
                    performSearch(newText);
                }
                return true;
            }
        });

        // Chip filter listeners
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                currentFilter = null;
                loadAllHikes();
                return;
            }

            int checkedId = checkedIds.get(0);

            if (checkedId == R.id.chipAll) {
                currentFilter = null;
                loadAllHikes();
            } else if (checkedId == R.id.chipTrailWalk) {
                currentFilter = "Trail Walk";
                filterByType(currentFilter);
            } else if (checkedId == R.id.chipDayHike) {
                currentFilter = "Day Hike";
                filterByType(currentFilter);
            } else if (checkedId == R.id.chipTrekking) {
                currentFilter = "Trekking";
                filterByType(currentFilter);
            } else if (checkedId == R.id.chipSummitHike) {
                currentFilter = "Summit Hike";
                filterByType(currentFilter);
            } else if (checkedId == R.id.chipNatureTrail) {
                currentFilter = "Nature Trail";
                filterByType(currentFilter);
            }
        });

        // Bottom navigation
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_search) {
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

    private void loadAllHikes() {
        hikeViewModel.getAllHikes().observe(this, hikes -> {
            updateResults(hikes);
        });
    }

    private void performSearch(String query) {
        hikeViewModel.searchHikes(query).observe(this, hikes -> {
            updateResults(hikes);
        });
    }

    private void filterByType(String type) {
        hikeViewModel.getHikesByType(type).observe(this, hikes -> {
            updateResults(hikes);
        });
    }

    private void updateResults(List<Hike> hikes) {
        if (hikes == null || hikes.isEmpty()) {
            rvSearchResults.setVisibility(View.GONE);
            layoutEmptyState.setVisibility(View.VISIBLE);
        } else {
            rvSearchResults.setVisibility(View.VISIBLE);
            layoutEmptyState.setVisibility(View.GONE);
            searchAdapter.setHikes(hikes);
        }
    }

    @Override
    public void onHikeClick(Hike hike) {
        Intent intent = new Intent(this, HikeDetailActivity.class);
        intent.putExtra("HIKE_ID", hike.getId());
        startActivity(intent);
    }

    @Override
    public void onEditClick(Hike hike) {
        // Read-only in search, do nothing
    }

    @Override
    public void onDeleteClick(Hike hike) {
        // Read-only in search, do nothing
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigation.setSelectedItemId(R.id.nav_search);
    }
}