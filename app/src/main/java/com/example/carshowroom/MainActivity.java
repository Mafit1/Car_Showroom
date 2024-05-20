package com.example.carshowroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CarAdapter.OnItemClickListener {
    private DatabaseReference mDataBase;
    private FirebaseAuth firebaseAuth;
    private final String DATA_BASE_URL = "https://car-showroom-51ab0-default-rtdb.europe-west1.firebasedatabase.app/";
    private final String CAR_KEY = "car";
    private EditText searchEditText;
    private RecyclerView carRecyclerView;
    private Button addCarButton, signOutButton;
    private CarAdapter carAdapter;
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
        firebaseAuth = FirebaseAuth.getInstance();
        getDataFromDB();

        searchEditText = findViewById(R.id.search_edit_text);
        carRecyclerView = findViewById(R.id.car_recycler_view);
        addCarButton = findViewById(R.id.add_car_button);
        signOutButton = findViewById(R.id.signOutButton);

        carAdapter = new CarAdapter(carList, this);
        carRecyclerView.setAdapter(carAdapter);
        carRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        carAdapter.setOnItemClickListener(new CarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Обработка нажатия на элемент RecyclerView
                Car selectedCar = carList.get(position);
                Intent intent = new Intent(MainActivity.this, CarDetailsActivity.class);
                intent.putExtra("selectedCar", selectedCar);
                startActivity(intent);
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
                startActivity(intent);
            }
        });
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
                    assert car != null;
                    car.setId(dataSnapshot.getKey());
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

    @Override
    public void onItemClick(int position) {
        Car selectedCar = carList.get(position);
        Intent intent = new Intent(MainActivity.this, CarDetailsActivity.class);
        intent.putExtra("selectedCar", selectedCar);
        startActivity(intent);
    }
}
