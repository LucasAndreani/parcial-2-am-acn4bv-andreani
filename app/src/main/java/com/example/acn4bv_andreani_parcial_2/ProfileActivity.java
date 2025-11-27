package com.example.acn4bv_andreani_parcial_2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private SharedPreferences prefs;
    private static final String KEY_USER_NAME = "userName";
    private static final String PREF_NAME = "MyTaskAppPrefs";
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Configurar ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.profile_title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        btnBack = findViewById(R.id.btn_custom_back);
        btnBack.setOnClickListener(v -> {
            finish();
        });

        db = new DatabaseHelper(this);
        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        TextView tvUserName = findViewById(R.id.tv_profile_user_name);
        TextView tvTaskCount = findViewById(R.id.tv_profile_task_count);
        TextView tvFirstTaskDate = findViewById(R.id.tv_profile_first_task_date);

        String userName = prefs.getString(KEY_USER_NAME, "Usuario");
        tvUserName.setText(getString(R.string.profile_welcome_template, userName));

        int totalTasks = db.getAllTasksCount();
        String firstTaskDate = db.getFirstTaskDate();

        if (totalTasks > 0) {
            tvTaskCount.setText(getString(R.string.profile_task_count_template, totalTasks));
            tvFirstTaskDate.setText(getString(R.string.profile_first_task_date_template, formatDisplayDate(firstTaskDate)));
        } else {
            tvTaskCount.setText(R.string.profile_no_tasks_recorded);
            tvFirstTaskDate.setText("");
        }
    }

    private String formatDisplayDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return "";
        }
        try {
            SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = dbFormat.parse(dateString);
            SimpleDateFormat displayFormat = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
            return displayFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}