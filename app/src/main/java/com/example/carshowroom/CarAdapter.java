package com.example.carshowroom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private ArrayList<Car> carList;
    private ArrayList<Car> carListFull; // Полный список для фильтрации
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public CarAdapter(ArrayList<Car> carList) {
        this.carList = carList;
        carListFull = new ArrayList<>(carList); // Создаем копию оригинального списка
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_item_layout, parent, false);
        return new CarViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car currentCar = carList.get(position);
        holder.carTextView.setText(currentCar.getMaker() + " " + currentCar.getModel());
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public void filter(String text) {
        carList.clear();
        if (text.isEmpty()) {
            carList.addAll(carListFull);
        } else {
            String query = text.toLowerCase().trim();
            for (Car car : carListFull) {
                if (car.getMaker().toLowerCase().contains(query) || car.getModel().toLowerCase().contains(query)) {
                    carList.add(car);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {

        public TextView carTextView;

        public CarViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            carTextView = itemView.findViewById(R.id.carTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
