package com.example.acn4bv_andreani_parcial_2;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class TasksListFragment extends Fragment implements TasksAdapter.OnTaskActionListener {

    private TextView tvMonthYear;
    private TextView tvDayOfWeekDate;
    private RecyclerView rvTasks;
    private FloatingActionButton fabAddTask;
    private ImageView btnPreviousDay;
    private ImageView btnNextDay;

    private String currentSearchQuery = "";

    private List<Task> tasksList;
    private TasksAdapter tasksAdapter;
    private DatabaseHelper db;
    private Calendar currentCalendar;

    public TasksListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(requireContext());
        currentCalendar = Calendar.getInstance();
        tasksList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tasks_list, container, false);

        tvMonthYear = v.findViewById(R.id.tv_month_year);
        tvDayOfWeekDate = v.findViewById(R.id.tv_day_of_week_date);
        rvTasks = v.findViewById(R.id.rv_tasks);
        fabAddTask = v.findViewById(R.id.fab_add_task);
        btnPreviousDay = v.findViewById(R.id.btn_prev_day);
        btnNextDay = v.findViewById(R.id.btn_next_day);

        rvTasks.setLayoutManager(new LinearLayoutManager(requireContext()));
        tasksAdapter = new TasksAdapter(requireContext(), tasksList, this);
        rvTasks.setAdapter(tasksAdapter);

        updateDateDisplay();
        loadTasks();

        fabAddTask.setOnClickListener(view -> showAddTaskDialog(null));
        btnPreviousDay.setOnClickListener(view -> changeDay(-1));
        btnNextDay.setOnClickListener(view -> changeDay(1));

        return v;
    }

    private void updateDateDisplay() {
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy", new Locale("es", "ES"));
        tvMonthYear.setText(monthYearFormat.format(currentCalendar.getTime()));

        SimpleDateFormat dayOfWeekDateFormat = new SimpleDateFormat("EEEE, d", new Locale("es", "ES"));
        tvDayOfWeekDate.setText(dayOfWeekDateFormat.format(currentCalendar.getTime()));
    }

    private void loadTasks() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDateString = dateFormat.format(currentCalendar.getTime());

        List<Task> allTasksForDate = db.getTasksForDate(currentDateString);
        List<Task> filteredTasks = new ArrayList<>();

        if (!currentSearchQuery.isEmpty()) {
            for (Task task : allTasksForDate) {
                if (task.getTitle().toLowerCase().contains(currentSearchQuery.toLowerCase()) ||
                        task.getDescription().toLowerCase().contains(currentSearchQuery.toLowerCase())) {
                    filteredTasks.add(task);
                }
            }
        } else {
            filteredTasks.addAll(allTasksForDate);
        }

        tasksAdapter.setTasks(filteredTasks);

        if (filteredTasks.isEmpty() && getView() != null) {
            if (!currentSearchQuery.isEmpty()) {
                Snackbar.make(getView(), getString(R.string.no_tasks_found_for_search) + " '" + currentSearchQuery + "'", Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(getView(), R.string.no_tasks_for_today, Snackbar.LENGTH_LONG).show();
            }
        }
    }


    private void changeDay(int daysToAdd) {
        currentCalendar.add(Calendar.DAY_OF_YEAR, daysToAdd);
        updateDateDisplay();
        currentSearchQuery = "";
        loadTasks();
    }

    private void showAddTaskDialog(@Nullable Task taskToEdit) {

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_task, null);

        final EditText inputTitle = dialogView.findViewById(R.id.editTextTaskTitle);
        final EditText inputDescription = dialogView.findViewById(R.id.editTextTaskDescription);
        final EditText inputTime = dialogView.findViewById(R.id.editTextTaskTime);

        inputTime.setHint("HH:MM (ej. 09:30)");
        inputTime.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_TIME);

        String dialogTitle = "Nueva tarea";
        if (taskToEdit != null) {
            dialogTitle = "Editar tarea";
            inputTitle.setText(taskToEdit.getTitle());
            inputDescription.setText(taskToEdit.getDescription());
            inputTime.setText(taskToEdit.getTime());
        }

        new AlertDialog.Builder(requireContext())
                .setTitle(dialogTitle)
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String title = inputTitle.getText().toString().trim();
                    String description = inputDescription.getText().toString().trim();
                    String time = inputTime.getText().toString().trim();

                    if (title.isEmpty()) {
                        inputTitle.setError("El título no puede estar vacío");
                        Snackbar.make(fabAddTask, R.string.task_title_cannot_be_empty, Snackbar.LENGTH_SHORT).show();
                        return;
                    } else {
                        inputTitle.setError(null);
                    }

                    Pattern timePattern = Pattern.compile("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$");
                    if (time.isEmpty()) {
                        inputTime.setError("La hora no puede estar vacía");
                        Snackbar.make(fabAddTask, R.string.task_time_cannot_be_empty, Snackbar.LENGTH_SHORT).show();
                        return;
                    } else if (!timePattern.matcher(time).matches()) {
                        inputTime.setError("Formato de hora inválido. Usa HH:MM (ej. 09:30)");
                        Snackbar.make(fabAddTask, R.string.task_time_invalid_format, Snackbar.LENGTH_SHORT).show();
                        return;
                    } else {
                        inputTime.setError(null);
                    }

                    boolean ok;
                    if (taskToEdit == null) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String taskDateString = dateFormat.format(currentCalendar.getTime());

                        Task newTask = new Task(title, description, time, taskDateString);
                        ok = db.insertTask(newTask);
                        if (ok) {
                            Snackbar.make(fabAddTask, R.string.task_added_successfully, Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(fabAddTask, R.string.error_adding_task, Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        taskToEdit.setTitle(title);
                        taskToEdit.setDescription(description);
                        taskToEdit.setTime(time);
                        ok = db.updateTask(taskToEdit);
                        if (ok) {
                            Snackbar.make(fabAddTask, R.string.task_updated_successfully, Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(fabAddTask, R.string.error_updating_task, Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    loadTasks();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onEditClick(Task task) {
        showAddTaskDialog(task);
    }

    @Override
    public void onDeleteClick(Task task) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar tarea")
                .setMessage("¿Estás seguro de que quieres eliminar la tarea '" + task.getTitle() + "'?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    boolean ok = db.deleteTask(task.getId());
                    if (ok) {
                        Snackbar.make(fabAddTask, R.string.task_deleted_successfully, Snackbar.LENGTH_SHORT).show();
                        loadTasks();
                    } else {
                        Snackbar.make(fabAddTask, R.string.error_deleting_task, Snackbar.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    public void filterTasksByText(String query) {
        this.currentSearchQuery = query;
        loadTasks();
    }

    public void clearSearchFilter() {
        this.currentSearchQuery = "";
        loadTasks();
    }
}