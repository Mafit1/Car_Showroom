package com.example.carshowroom;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCarFragment extends Fragment {
    private DatabaseReference mDataBase;
    private final String CAR_KEY = "car";
    private EditText makerEditText, modelEditText, colorEditText, configurationEditText, yearEditText;
    private Button addButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_car, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        makerEditText =         view.findViewById(R.id.edit_text_maker);
        modelEditText =         view.findViewById(R.id.edit_text_model);
        colorEditText =         view.findViewById(R.id.edit_text_color);
        configurationEditText = view.findViewById(R.id.edit_text_configuration);
        yearEditText =          view.findViewById(R.id.edit_text_year);
        mDataBase =             FirebaseDatabase.getInstance("https://car-showroom-51ab0-default-rtdb.europe-west1.firebasedatabase.app/").getReference(CAR_KEY);
        addButton =             view.findViewById(R.id.button_add);

        addButton.setOnClickListener(v -> {
            String id =             mDataBase.getKey();
            String maker =          makerEditText.getText().toString();
            String model =          modelEditText.getText().toString();
            String color =          colorEditText.getText().toString();
            String configuration =  configurationEditText.getText().toString();
            String year =           yearEditText.getText().toString();

            if (TextUtils.isEmpty(maker) || TextUtils.isEmpty(model) || TextUtils.isEmpty(color) || TextUtils.isEmpty(configuration) || TextUtils.isEmpty(year)) {
                Toast.makeText(requireContext(), "Заполните поля", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                Toast.makeText(requireContext(), "Новые данные добавлены", Toast.LENGTH_SHORT).show();
            }

            Car newCar = new Car(id, maker, model, color, configuration, year);
            mDataBase.push().setValue(newCar);

            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }
}