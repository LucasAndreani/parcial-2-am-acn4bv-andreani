package com.example.acn4bv_andreani_parcial_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {

    private Context context;
    private List<Task> tasks; // Esta lista ahora es manejada principalmente por el adaptador
    private OnTaskActionListener listener;

    // interfaz de Callback, tambien para q el recycler view puedan interactuar con la activity o fragment
    public interface OnTaskActionListener {
        void onEditClick(Task task);
        void onDeleteClick(Task task);
    }
    public TasksAdapter(Context context, List<Task> tasks, OnTaskActionListener listener) {
        this.context = context;
        this.tasks = tasks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    // muy importante para el recycle porque se llena conj la nueva info
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task currentTask = tasks.get(position);

        holder.tvTime.setText(currentTask.getTime());
        holder.tvTitle.setText(currentTask.getTitle());
        holder.tvDescription.setText(currentTask.getDescription());

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(currentTask);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(currentTask);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    // metodo para que el fragment reemplace la lista del adaptador
    public void setTasks(List<Task> newTasks) {
        this.tasks = newTasks; // Asigna directamente la nueva lista
        notifyDataSetChanged(); // con esto se actualiza el recycle
    }

    // segun las buenas practicas, esta bueno tenerla adentro del adapter esta clase.
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvTime;
        TextView tvTitle;
        TextView tvDescription;
        ImageView btnEdit;
        ImageView btnDelete;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_task_icon);
            tvTime = itemView.findViewById(R.id.tv_task_time);
            tvTitle = itemView.findViewById(R.id.tv_task_title);
            tvDescription = itemView.findViewById(R.id.tv_task_description);
            btnEdit = itemView.findViewById(R.id.btn_edit_task);
            btnDelete = itemView.findViewById(R.id.btn_delete_task);
        }
    }
}