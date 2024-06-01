package com.example.carshowroom;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CarDetailsFragment extends Fragment {
    private DatabaseReference mDataBase;
    private StorageReference mStorageRef;
    private final String DATA_BASE_URL = "https://car-showroom-51ab0-default-rtdb.europe-west1.firebasedatabase.app/";
    private final String CAR_KEY = "car";
    private Button deleteButton;
    private TextView makerTextView, modelTextView, colorTextView, configurationTextView, yearTextView;
    private ImageView carImageView;
    private Car selectedCar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_details, container, false);

        if (getArguments() != null) {
            selectedCar = (Car) getArguments().getSerializable("car");
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDataBase = FirebaseDatabase.getInstance(DATA_BASE_URL).getReference(CAR_KEY);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        carImageView = view.findViewById(R.id.carImage);

        StorageReference imgRef = mStorageRef.child("car_photos/" + selectedCar.getId() + ".jpg");
        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Загрузка изображения в ImageView
                Glide.with(requireContext()).load(uri).into(carImageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Обработка ошибок при загрузке изображения
                Toast.makeText(requireContext(), "Ошибка загрузки изображения", Toast.LENGTH_SHORT).show();
            }
        });

        makerTextView = view.findViewById(R.id.maker);
        makerTextView.setText("Марка: " + selectedCar.getMaker());

        modelTextView = view.findViewById(R.id.model);
        modelTextView.setText("Модель: " + selectedCar.getModel());

        colorTextView = view.findViewById(R.id.color);
        colorTextView.setText("Цвет: " + selectedCar.getColor());

        configurationTextView = view.findViewById(R.id.configuration);
        configurationTextView.setText("Комплектация: " + selectedCar.getConfiguration());

        yearTextView = view.findViewById(R.id.year);
        yearTextView.setText("Год выпуска: " + selectedCar.getYear());

        deleteButton = view.findViewById(R.id.removeButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Удаление");
                builder.setMessage("Вы уверены, что хотите удалить данные?");
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDataBase.child(selectedCar.getId()).removeValue();
                        Toast.makeText(requireContext(), "Данные удалены", Toast.LENGTH_SHORT).show();
                        requireActivity().getSupportFragmentManager().popBackStack();
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