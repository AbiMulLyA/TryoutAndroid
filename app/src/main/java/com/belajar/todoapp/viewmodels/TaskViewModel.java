package com.belajar.todoapp.viewmodels;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.belajar.todoapp.models.DataItem;
import com.belajar.todoapp.models.TaskModel;
import com.belajar.todoapp.repositories.TaskRepository;
import com.belajar.todoapp.repositories.TaskService;
import com.belajar.todoapp.utils.ClientUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class TaskViewModel extends  BaseViewModel{
    private MutableLiveData <List<DataItem>> tasks =new MutableLiveData<>();
    private List<DataItem> temp = new ArrayList<>();

    public void fetchTasks(){
        isLoading.setValue(true);
        TaskRepository taskRepository = new TaskRepository();
        taskRepository.service.getTask().enqueue(new Callback<TaskModel>() {
            @Override
            public void onResponse(Call<TaskModel> call, Response<TaskModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        tasks.setValue(response.body().getData());
                        temp = tasks.getValue();
//                        String js = new Gson().toJson(tasks);
//                        Log.d("hasil", ""+ js);
                    } else {
                        error.setValue("Data users kosong!");
                    }
                } else {
                    error.setValue(response.message());
                }

                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<TaskModel> call, Throwable t) {
                t.printStackTrace();
                Log.e("Error get posts", t.getMessage());

                error.setValue(t.getMessage());

                isLoading.setValue(false);
            }
        });
    }
    public void searchTask(CharSequence s) {
        List<DataItem> taskModels = new ArrayList<>();
        if (tasks.getValue() != null) {
            for (DataItem post : temp) {
                if (post.getTask().toLowerCase().contains(s)) {
                    taskModels.add(post);
                }
            }

            tasks.setValue(taskModels);
        }
    }
    public void updateTask(DataItem task){
        ClientUtil.client(TaskService.class, TaskRepository.BASE_URL).updateTask(task.getId(), task).enqueue(new Callback<TaskModel>() {
            @Override
            public void onResponse(Call<TaskModel> call, retrofit2.Response<TaskModel> response) {
            }
            @Override
            public void onFailure(Call<TaskModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    public void deleteTask(DataItem todos){
        ClientUtil.client(TaskService.class, TaskRepository.BASE_URL).deleteTask(todos.getId()).enqueue(new Callback<TaskModel>() {
            @Override
            public void onResponse(Call<TaskModel> call, Response<TaskModel> response) {

            }

            @Override
            public void onFailure(Call<TaskModel> call, Throwable t) {

            }
        });
    }
    public void updateStatus(DataItem task, Boolean status){
        if (status) {
            task.setStatus(false);
        } else {
            task.setStatus(true);
        }
        ClientUtil.client(TaskService.class, TaskRepository.BASE_URL).updateStatus(task.getId(), task).enqueue(new Callback<TaskModel>() {
            @Override
            public void onResponse(Call<TaskModel> call, Response<TaskModel> response) {

            }

            @Override
            public void onFailure(Call<TaskModel> call, Throwable t) {

            }
        });
    }
    public MutableLiveData <List<DataItem>> getTasks() {
        return tasks;
    }
}
