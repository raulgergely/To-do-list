package com.example.to_do_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);
        Button cancelTask = findViewById(R.id.buttonCancel);
        Button addTask = findViewById(R.id.buttonSave);
        EditText edTitle =findViewById(R.id.editTaskTitle);
        EditText edDescription =findViewById(R.id.editTaskDescription);
        Task taskToEdit = (Task) getIntent().getSerializableExtra("taskToEdit");

        if(taskToEdit != null) {
            edTitle.setText(taskToEdit.getTitle());
            edDescription.setText(taskToEdit.getDescription());
        }
        cancelTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentReturn = new Intent();
                setResult(RESULT_OK,intentReturn);
                finish();
            }
        });
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task newTask =new Task(edTitle.getText().toString(), edDescription.getText().toString(), false);

                Intent intentReturn = new Intent();
                intentReturn.putExtra("newTask",newTask);
                setResult(RESULT_OK,intentReturn);
                finish();
            }
        });
    }
}