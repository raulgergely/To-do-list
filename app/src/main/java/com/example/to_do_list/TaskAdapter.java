package com.example.to_do_list;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder>{

    private List<Task> taskList;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onCheckboxClick(int position);
    }
    public TaskAdapter(List<Task> taskList, OnItemClickListener listener){
        this.taskList = taskList;
        this.listener = listener;
    }
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task,parent,false);
        return new TaskViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task curentTask = taskList.get(position);
        holder.titleTextView.setText(curentTask.getTitle());
        holder.descriptionTextView.setText((curentTask.getDescription()));
        holder.checkBox.setChecked(curentTask.isCompleted());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<Task> newList){
        this.taskList = newList;
        notifyDataSetChanged();
    }
    public void updateCompletedTask(int position){
        Task curentTask = this.taskList.get(position);
        curentTask.setCompleted(!curentTask.isCompleted());
        this.taskList.set(position,curentTask);
        notifyItemChanged(position);
    }
}
