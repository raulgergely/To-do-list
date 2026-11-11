package com.example.to_do_list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupMenu;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> addTaskLauncher;
    private int currentEditPosition = -1;
    private Button filterbtn;
    private CheckBox checkBox;
    private TaskAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        filterbtn = findViewById(R.id.filterBtn);

        List<Task> taskList = new ArrayList<>();
        taskList.add(new Task("Învață RecyclerView", "Înțelege cum funcționează Adapterul și ViewHolder-ul", true));
        taskList.add(new Task("Pregătire interviu Bento", "Repetă conceptele fundamentale de Android", false));
        taskList.add(new Task("Construiește To-Do List", "Scrie codul pas cu pas pentru aplicație", false));
        taskList.add(new Task("Ia o pauză", "Meritată după atâta cod!", false));



        // ... în interiorul lui onCreate, unde creai adaptorul vechi:
        adapter = new TaskAdapter(taskList, new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position){
                currentEditPosition = position; // Salvăm poziția
                Task taskToEdit = taskList.get(position); // Luăm task-ul de pe rândul respectiv

                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                // Împachetăm task-ul existent în Intent ca să știe ecranul ce să precompleteze
                intent.putExtra("taskToEdit", taskToEdit);

                addTaskLauncher.launch(intent); // Lansăm ecranul prin launcher
            }
            @Override
            public void onCheckboxClick(int position) {
               adapter.updateCompletedTask(position);
            }
        });
        recyclerView.setAdapter(adapter);

        FloatingActionButton fabAddTask = findViewById(R.id.buttonAddTask);

        addTaskLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
                Intent data = result.getData();

                Task newTask = (Task) data.getSerializableExtra("newTask");
                if(newTask != null){
                    if(currentEditPosition != -1) {
                        taskList.set(currentEditPosition, newTask);
                        adapter.notifyItemChanged(currentEditPosition);
                        currentEditPosition = -1;
                    }else {
                        taskList.add(newTask);
                        adapter.notifyItemInserted(taskList.size() - 1);
                    }
                }
            }
        });
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                addTaskLauncher.launch(intent);
            }
        });
        filterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this,v);
                popupMenu.getMenuInflater().inflate(R.menu.filter_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(item -> {
                    int id =item.getItemId();
                    if (id == R.id.menu_all) {
                        adapter.updateList(taskList);
                        return true;
                    } else if (id == R.id.menu_active) {
                        List<Task> completedList = new ArrayList<>();

                        for (Task task : taskList) {
                            if (!task.isCompleted()) {
                                completedList.add(task);
                            }
                        }
                        adapter.updateList(completedList);
                        return true;
                    } else if (id == R.id.menu_completed) {
                        List<Task> completedList = new ArrayList<>();

                        for (Task task : taskList) {
                            if (task.isCompleted()) {
                                completedList.add(task);
                            }
                        }
                        adapter.updateList(completedList);
                        return true;
                    }
                    return false;
                });
                popupMenu.show();
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                Task deletedTask = taskList.get(position);

                taskList.remove(position);
                adapter.notifyItemRemoved(position);

                com.google.android.material.snackbar.Snackbar.make(recyclerView, "Task deleted", com.google.android.material.snackbar.Snackbar.LENGTH_LONG)
                        .setAction("UNDO", v -> {
                            taskList.add(position, deletedTask);
                            adapter.notifyItemInserted(position);
                        })
                        .show();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

}