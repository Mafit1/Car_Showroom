package com.example.carshowroom;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CarListFragment extends Fragment implements CarAdapter.OnItemClickListener {
    private DatabaseReference mDataBase;
    private FirebaseAuth firebaseAuth;
    private final String DATA_BASE_URL = "https://car-showroom-51ab0-default-rtdb.europe-west1.firebasedatabase.app/";
    private final String CAR_KEY = "car";
    private RecyclerView carRecyclerView;
    private SearchView searchView;
    private Button addCarButton, signOutButton;
    private CarAdapter carAdapter;
    private ArrayList<Car> carList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_car_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDataBase = FirebaseDatabase.getInstance(DATA_BASE_URL).getReference(CAR_KEY);
        firebaseAuth = FirebaseAuth.getInstance();
        getDataFromDB();

        searchView = view.findViewById(R.id.search_view);
        carRecyclerView = view.findViewById(R.id.car_recycler_view);
        addCarButton = view.findViewById(R.id.add_car_button);
        signOutButton = view.findViewById(R.id.signOutButton);

        carAdapter = new CarAdapter(carList);

        carRecyclerView.setAdapter(carAdapter);
        carRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        carAdapter.setOnItemClickListener(new CarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Обработка нажатия на элемент RecyclerView
                Car selectedCar = carList.get(position);

                CarDetailsFragment carDetailsFragment = new CarDetailsFragment();

                Bundle bundle = new Bundle();
                bundle.putSerializable("car", selectedCar);
                carDetailsFragment.setArguments(bundle);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, carDetailsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Выход из аккаунта");
                builder.setMessage("Вы уверены, что хотите выйти из аккаунта?");
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseAuth.signOut();
                        Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        addCarButton.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new AddCarFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                carAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                carAdapter.filter(newText);
                return false;
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

        CarDetailsFragment carDetailsFragment = new CarDetailsFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("car", selectedCar);
        carDetailsFragment.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, carDetailsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}