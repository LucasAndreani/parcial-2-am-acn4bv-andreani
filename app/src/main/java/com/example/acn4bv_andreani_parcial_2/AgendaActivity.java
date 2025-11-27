package com.example.acn4bv_andreani_parcial_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
// import android.widget.TextView; // Este import no es necesario
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
// import android.widget.Toast; // Quitado para usar Snackbar

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.snackbar.Snackbar; // Asegúrate de tener este import

public class AgendaActivity extends AppCompatActivity {

    private TasksListFragment tasksListFragment;
    private ImageView btnSearch;
    private ImageView btnHelp;

    private ImageButton btnProfile;

    private ImageButton btnBack;

    private SharedPreferences prefs;
    private static final String PREF_NAME = "MyTaskAppPrefs";
    private static final String KEY_DARK_MODE = "darkModeEnabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        boolean isDarkModeEnabled = prefs.getBoolean(KEY_DARK_MODE, false);

        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        btnSearch = findViewById(R.id.btn_search);
        btnHelp = findViewById(R.id.btn_help);
        btnProfile = findViewById(R.id.btn_profile);

        btnBack = findViewById(R.id.btn_custom_back);

        tasksListFragment = new TasksListFragment();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, tasksListFragment)
                    .commit();
        }

        btnHelp.setOnClickListener(v -> {
            Intent intent = new Intent(AgendaActivity.this, HelpActivity.class);
            startActivity(intent);
        });

        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(AgendaActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(AgendaActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Para limpiar la pila de actividades
            startActivity(intent);
            finish();
        });

        btnSearch.setOnClickListener(v -> showSearchDialog());
    }

    private void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_search, null);
        builder.setView(dialogView);

        EditText editTextSearch = dialogView.findViewById(R.id.editTextSearch);

        builder.setTitle(R.string.search_dialog_title)
                .setPositiveButton(R.string.search_button_text, (dialog, id) -> {
                    String searchText = editTextSearch.getText().toString().trim();
                    if (!searchText.isEmpty()) {
                        tasksListFragment.filterTasksByText(searchText); // Llama al metodo de filtrado del fragment
                        Snackbar.make(findViewById(android.R.id.content), getString(R.string.searching_for) + " '" + searchText + "'", Snackbar.LENGTH_SHORT).show();
                    } else {
                        tasksListFragment.clearSearchFilter();
                        Snackbar.make(findViewById(android.R.id.content), R.string.search_cleared, Snackbar.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel_button_text, (dialog, id) -> {
                    dialog.dismiss();
                    tasksListFragment.clearSearchFilter();
                    Snackbar.make(findViewById(android.R.id.content), R.string.search_cancelled, Snackbar.LENGTH_SHORT).show();
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}