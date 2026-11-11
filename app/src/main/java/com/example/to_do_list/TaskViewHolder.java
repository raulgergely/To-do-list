package com.example.to_do_list;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    TextView titleTextView;
    TextView descriptionTextView;
    CheckBox checkBox;

    public TaskViewHolder(@NonNull View itemView, TaskAdapter.OnItemClickListener listener) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.textTaskTitle);
        descriptionTextView = itemView.findViewById(R.id.textTaskDescription);
        checkBox = itemView.findViewById(R.id.checkBoxTask);

        itemView.setOnClickListener(v -> {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position);
            }
        });
        checkBox.setOnClickListener(v->{
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onCheckboxClick(position);
            }
        });
    }
}