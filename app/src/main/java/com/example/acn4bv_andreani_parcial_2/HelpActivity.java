package com.example.acn4bv_andreani_parcial_2;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class HelpActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private static final String PREF_NAME = "MyTaskAppPrefs";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_DARK_MODE = "darkModeEnabled";

    private ListView lvHelpContent;
    private Switch switchDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Toolbar toolbar = findViewById(R.id.toolbar_help);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.help_activity_title);
        }

        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        lvHelpContent = findViewById(R.id.lv_help_content);
        switchDarkMode = findViewById(R.id.switch_dark_mode);

        // Cargar el estado guardado del modo oscuro
        boolean isDarkModeEnabled = prefs.getBoolean(KEY_DARK_MODE, false); // Default: modo claro
        switchDarkMode.setChecked(isDarkModeEnabled);

        // configurar listener para el Switch
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // guardar la preferencia
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(KEY_DARK_MODE, isChecked);
            editor.apply();

            // aplicar el tema inmediatamente
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            // recrear la actividad para que el cambio de tema se aplique
            recreate();
        });

        loadHelpContent();
    }

    // toda logica para ver el nombre q ingreso el user x mas que lo cambie
    @Override
    protected void onResume() {
        super.onResume();
        loadHelpContent();
    }

    private void loadHelpContent() {
        String userName = prefs.getString(KEY_USER_NAME, "Usuario");

        Resources res = getResources();
        String[] rawHelpArray = res.getStringArray(R.array.help_content_array);

        List<String> formattedHelpContent = new ArrayList<>();
        for (String item : rawHelpArray) {
            if (item.contains("[USER_PLACEHOLDER]")) {
                formattedHelpContent.add(item.replace("[USER_PLACEHOLDER]", userName));
            } else {
                formattedHelpContent.add(item);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.list_item_text,
                R.id.text_content,
                formattedHelpContent);

        lvHelpContent.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}