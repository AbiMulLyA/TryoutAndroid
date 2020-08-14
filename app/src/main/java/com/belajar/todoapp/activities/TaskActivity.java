package com.belajar.todoapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.belajar.todoapp.R;
import com.belajar.todoapp.databinding.ActivityTaskBinding;
import com.belajar.todoapp.models.DataItem;
import com.belajar.todoapp.viewmodels.TaskViewModel;

public class TaskActivity extends AppCompatActivity {
    TaskViewModel viewModel;
    ActivityTaskBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_task);

        DataItem task = getIntent().getParcelableExtra("DATA");
        binding.setData(task);

        viewModel = new ViewModelProvider(TaskActivity.this, new ViewModelProvider.NewInstanceFactory()).get(TaskViewModel.class);
        binding.btnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.setTask(binding.etData.getText().toString());
                viewModel.updateTask(task);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}