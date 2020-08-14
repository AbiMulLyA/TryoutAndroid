package com.belajar.todoapp.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.belajar.todoapp.R;
import com.belajar.todoapp.databinding.SingleTodoBinding;
import com.belajar.todoapp.models.DataItem;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<DataItem> items = new ArrayList<>();
    public interface TaskListener {
        void onUpdate(DataItem task);
        void onDelete(DataItem task);
        void onStatus(DataItem task, boolean status);
    }

    private TaskListener listener;

    public void setListener(TaskListener listener) {
        this.listener = listener;
    }

    public void setItems(List<DataItem> items){
        Log.d("data", "response" + items);
        this.items = items;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SingleTodoBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.single_todo,
                parent,
                false
        );
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, int position) {
        holder.bindData(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        if (items != null){
            return items.size();
        }else{
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        SingleTodoBinding binding;
        DateTimeFormatter formatter , localFormatter;

        public ViewHolder(@NonNull SingleTodoBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindData(DataItem taskModel, TaskListener listener){
            binding.setTask(taskModel);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                formatter = DateTimeFormatter
                        .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                        .withZone(ZoneId.of("UTC"));
                LocalDateTime date = LocalDateTime
                        .parse(taskModel.getUpdatedAt(), formatter);
                localFormatter  = DateTimeFormatter
                        .ofPattern(
                                "EEEE, dd MMMM yyyy",
                                new Locale("id", "ID")
                        );
                String localDate = date.format(localFormatter);
                binding.setDate(localDate);
            }
            binding.setStatus(taskModel.isStatus());
            binding.ivStatus.setOnClickListener(view -> listener.onStatus(taskModel, taskModel.isStatus()));
            binding.btnUpdate.setOnClickListener(view -> listener.onUpdate(taskModel));
            binding.btnDelete.setOnClickListener(view -> listener.onDelete(taskModel));
        }
    }
}
