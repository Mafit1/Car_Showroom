package com.example.carshowroom;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CarDetailsActivity extends AppCompatActivity {
    private DatabaseReference mDataBase;
    private final String DATA_BASE_URL = "https://car-showroom-51ab0-default-rtdb.europe-west1.firebasedatabase.app/";
    private final String CAR_KEY = "car";
    private Button cancelButton, deleteButton;
    private TextView makerTextView, modelTextView, colorTextView, configurationTextView, yearTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);

        mDataBase = FirebaseDatabase.getInstance(DATA_BASE_URL).getReference(CAR_KEY);

        Car selectedCar = (Car) getIntent().getSerializableExtra("selectedCar");
        Bundle arguments = getIntent().getExtras();
        int position = arguments.getInt("position");

        makerTextView = findViewById(R.id.maker);
        makerTextView.setText("Марка: " + selectedCar.getMaker());

        modelTextView = findViewById(R.id.model);
        modelTextView.setText("Модель: " + selectedCar.getModel());

        colorTextView = findViewById(R.id.color);
        colorTextView.setText("Цвет: " + selectedCar.getColor());

        configurationTextView = findViewById(R.id.configuration);
        configurationTextView.setText("Комплектация: " + selectedCar.getConfiguration());

        yearTextView = findViewById(R.id.year);
        yearTextView.setText("Год выпуска: " + selectedCar.getYear());


        cancelButton = findViewById(R.id.cancelButton);
        deleteButton = findViewById(R.id.removeButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CarDetailsActivity.this);
                builder.setTitle("Удаление");
                builder.setMessage("Вы уверены, что хотите удалить данные?");
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDataBase.child(selectedCar.getId()).removeValue();
                        Toast.makeText(CarDetailsActivity.this, "Данные удалены", Toast.LENGTH_SHORT).show();
                        finish();
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
    }

}