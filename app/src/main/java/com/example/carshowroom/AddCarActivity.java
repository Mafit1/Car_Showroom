package com.example.carshowroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCarActivity extends AppCompatActivity {
    private DatabaseReference mDataBase;
    private final String CAR_KEY = "car";
    private EditText makerEditText, modelEditText, colorEditText, configurationEditText, yearEditText;
    private Button cancelButton, addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        init();

        cancelButton.setOnClickListener(v -> {
            finish();
        });

        addButton.setOnClickListener(v -> {
            String id =             mDataBase.getKey();
            String maker =          makerEditText.getText().toString();
            String model =          modelEditText.getText().toString();
            String color =          colorEditText.getText().toString();
            String configuration =  configurationEditText.getText().toString();
            String year =           yearEditText.getText().toString();

            if (TextUtils.isEmpty(maker) || TextUtils.isEmpty(model) || TextUtils.isEmpty(color) || TextUtils.isEmpty(configuration) || TextUtils.isEmpty(year)) {
                Toast.makeText(this, "Заполните поля", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                Toast.makeText(this, "Новые данные добавлены", Toast.LENGTH_SHORT).show();
            }

            Car newCar = new Car(id, maker, model, color, configuration, year);
            mDataBase.push().setValue(newCar);
            finish();
        });
    }

    private void init() {
        makerEditText =         findViewById(R.id.edit_text_maker);
        modelEditText =         findViewById(R.id.edit_text_model);
        colorEditText =         findViewById(R.id.edit_text_color);
        configurationEditText = findViewById(R.id.edit_text_configuration);
        yearEditText =          findViewById(R.id.edit_text_year);
        mDataBase =             FirebaseDatabase.getInstance("https://car-showroom-51ab0-default-rtdb.europe-west1.firebasedatabase.app/").getReference(CAR_KEY);
        cancelButton =          findViewById(R.id.button_cancel);
        addButton =             findViewById(R.id.button_add);
    }
}