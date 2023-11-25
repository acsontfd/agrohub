package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText searchEditText;
    private CheckBox halalCheckBox;
    private CheckBox vegetarianCheckBox;
    private CheckBox organicCheckBox;
    private CheckBox nonHalalCheckBox;
    ArrayList<ExampleFarm> filteredList;
    ArrayList<ExampleFarm> exampleList = new ArrayList<>();
    ExampleAdapter exampleAdapter;
    public String currentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        halalCheckBox = findViewById(R.id.halalcheckBox);
        vegetarianCheckBox = findViewById(R.id.vegetariancheckBox);
        organicCheckBox = findViewById(R.id.organicCheckBox);
        nonHalalCheckBox = findViewById(R.id.nonHalalCheckBox);

        BottomNavigationView bnv = findViewById(R.id.bottom_Navigation);

        Intent intent = getIntent();
        currentuser = intent.getStringExtra("username");

        final int[] currentPageId = {R.id.home};
        bnv.setSelectedItemId(currentPageId[0]);

        halalCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            filterByCheckBoxes();
        });

        vegetarianCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            filterByCheckBoxes();
        });

        organicCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            filterByCheckBoxes();
        });

        nonHalalCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            filterByCheckBoxes();
        });

        exampleList.add(new ExampleFarm(R.drawable.farmfresh, " Farm Fresh ", "Halal", "FARM FRESH @ UPM (Industry Centre of Excellence, ICoE)"));
        exampleList.add(new ExampleFarm(R.drawable.tan, " Tan Brothers", "Vegetarian", "Ringlet, Cameron highlands "));
        exampleList.add(new ExampleFarm(R.drawable.everfresh, " Ever Fresh Cameron ", "Vegetarian", "Ever Fresh Cameron Sdn. Bhd."));
        exampleList.add(new ExampleFarm(R.drawable.fish," Hai Seng Huat", "Halal", "Hai Seng Huat Fishery Sdn Bhd"));
        exampleList.add(new ExampleFarm(R.drawable.zenxin," Zenxin Organic", "Organic", "Zenxin Organic Food @ PJ Section 17"));

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        exampleAdapter = new ExampleAdapter(exampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(exampleAdapter);

        searchEditText = findViewById(R.id.editTextText);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id != currentPageId[0]) {
                    // Only start a new activity if the selected item is different from the current page
                    Intent intent = null;

                    if (id == R.id.home) {
                        return true; // Prevent reloading the same page
                    } else if (id == R.id.profile) {
                        intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        intent.putExtra("username",currentuser);
                    } else if (id == R.id.community) {
                        intent = new Intent(getApplicationContext(), CommunityActivity.class);
                        intent.putExtra("username",currentuser);
                    }

                    if (intent != null) {
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish(); // Close the current activity
                    }
                    currentPageId[0] = id; // Update the current page
                    return true;
                }

                return false;
            }
        });

    }

    private void filter(String text) {
        filteredList = new ArrayList<>();
        for (ExampleFarm farm : exampleList) {
            if (farm.mText1.toLowerCase().contains(text.toLowerCase()) || farm.mText2.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(farm);
            }
        }
        exampleAdapter.filterList(filteredList);
    }

    private void filterByCheckBoxes() {
        ArrayList<ExampleFarm> filteredList = new ArrayList<>();

        boolean halalChecked = halalCheckBox.isChecked();
        boolean vegetarianChecked = vegetarianCheckBox.isChecked();
        boolean organicChecked = organicCheckBox.isChecked();
        boolean nonHalalChecked = nonHalalCheckBox.isChecked();

        for (ExampleFarm farm : exampleList) {
            String farmDetails = farm.getText2().toLowerCase();

            boolean isHalal = farmDetails.contains("halal");
            boolean isVegetarian = farmDetails.contains("vegetarian");
            boolean isOrganic = farmDetails.contains("organic");
            boolean isNonHalal = farmDetails.contains("non-halal");

            // Filtering based on checkbox states
            if ((halalChecked && isHalal) || (vegetarianChecked && isVegetarian) ||
                    (organicChecked && isOrganic) || (nonHalalChecked && isNonHalal)) {
                filteredList.add(farm);
            }

            // When no checkboxes are checked, show all farms
            if (!halalChecked && !vegetarianChecked && !organicChecked && !nonHalalChecked) {
                filteredList.addAll(exampleList);
                break; // Break the loop as we've added all farms
            }
        }

        exampleAdapter.filterList(filteredList);
    }


}