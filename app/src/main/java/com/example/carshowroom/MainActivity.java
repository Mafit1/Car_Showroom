package com.example.carshowroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDataBase;
    private final String DATA_BASE_URL = "https://car-showroom-51ab0-default-rtdb.europe-west1.firebasedatabase.app/";
    private final String CAR_KEY = "car";
    private EditText searchEditText;
    private ListView carListView;
    private Button addCarButton;
    private ArrayAdapter<Car> carAdapter;
    private ArrayList<Car> carList = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        addCarButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddCarActivity.class);
            startActivity(intent);
        });
    }

    private void init() {
        mDataBase = FirebaseDatabase.getInstance(DATA_BASE_URL).getReference(CAR_KEY);
        getDataFromDB();

        searchEditText = findViewById(R.id.search_edit_text);
        carListView = findViewById(R.id.car_list_view);
        carListView.setClickable(true);
        carListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Car selectedCar = carList.get(position);
                Intent intent = new Intent(MainActivity.this, CarDetailsActivity.class);
                intent.putExtra("selectedCar", selectedCar);
                startActivity(intent);
            }
        });
        addCarButton = findViewById(R.id.add_car_button);

        carAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, carList);
        carListView.setAdapter(carAdapter);
    }

    private void getDataFromDB() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!carList.isEmpty()) {
                    carList.clear();
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Car car = dataSnapshot.getValue(Car.class);
                    car.setId(dataSnapshot.getKey());
                    assert car != null;
                    carList.add(car);
                }
                carAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDataBase.addValueEventListener(valueEventListener);
    }
}