package com.belajar.todoapp.repositories;
import com.belajar.todoapp.models.DataItem;
import com.belajar.todoapp.models.TaskModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TaskService {
    @GET("api/v1/todos")
    Call <TaskModel> getTask();

    @Headers("Content-Type: application/json")
    @PUT("api/v1/todos/{id}")
    Call<TaskModel> updateTask(@Path("id") int id, @Body DataItem taskModel);

    @Headers("Content-Type: application/json")
    @PUT("api/v1/todos/{id}")
    Call<TaskModel> updateStatus(@Path("id") int id, @Body DataItem taskModel);

    @Headers("Content-Type: application/json")
    @DELETE("api/v1/todos/{id}")
    Call<TaskModel> deleteTask(@Path("id") int id);
}
