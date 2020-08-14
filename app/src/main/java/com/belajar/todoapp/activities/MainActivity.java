package com.belajar.todoapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.belajar.todoapp.R;
import com.belajar.todoapp.adapters.TaskAdapter;
import com.belajar.todoapp.databinding.ActivityMainBinding;
import com.belajar.todoapp.models.DataItem;
import com.belajar.todoapp.utils.ViewUtil;
import com.belajar.todoapp.viewmodels.TaskViewModel;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TaskAdapter adapter = new TaskAdapter();
        LinearLayoutManager grid = new LinearLayoutManager(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.rvTasks.setLayoutManager(grid);
        binding.rvTasks.setAdapter(adapter);

        TaskViewModel viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(TaskViewModel.class);
        viewModel.getTasks().observe(this, adapter::setItems);
        viewModel.fetchTasks();
        viewModel.getIsLoading().observe(this, isLoading -> {
            binding.pbLoadingPosts.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.rvTasks.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });

        viewModel.getError().observe(this, error -> {
            if (!TextUtils.isEmpty(error)) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                viewModel.searchTask(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        binding.etSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                ViewUtil.hideKeyboard(textView);
                return true;
            }
            return false;
        });
        binding.setViewModel(viewModel);

        adapter.setListener(new TaskAdapter.TaskListener() {
            @Override
            public void onUpdate(DataItem task) {
                startActivity(new Intent(
                        MainActivity.this,
                        TaskActivity.class));
            }

            @Override
            public void onDelete(DataItem task) {

            }

            @Override
            public void onStatus(DataItem task, boolean status) {
                viewModel.updateStatus(task, status);
                reload();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        binding = null;
    }

    public void reload(){
        finish();
        startActivity(getIntent());
    }
}