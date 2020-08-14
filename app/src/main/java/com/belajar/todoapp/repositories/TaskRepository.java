package com.belajar.todoapp.repositories;

import com.belajar.todoapp.utils.ClientUtil;

public class TaskRepository {

    private static final String BASE_URL = "https://online-course-todo.herokuapp.com/";

    public TaskService service;

    public TaskRepository() {
        service = ClientUtil.client(TaskService.class, BASE_URL);
    }
}
