package com.example.acn4bv_andreani_parcial_2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName;
    private Button btnIrAgenda;

    private static final String PREF_NAME = "MyTaskAppPrefs";
    private static final String KEY_USER_NAME = "userName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        btnIrAgenda = findViewById(R.id.btn_ir_agenda);

        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String savedUserName = prefs.getString(KEY_USER_NAME, "");

        if (!savedUserName.isEmpty()) {
            editTextName.setText(savedUserName);
        }

        btnIrAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = editTextName.getText().toString().trim();

                if (userName.isEmpty()) {
                    Snackbar.make(v, "Por favor, ingresá tu nombre", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(KEY_USER_NAME, userName);
                editor.apply();

                Intent intent = new Intent(MainActivity.this, AgendaActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString(KEY_USER_NAME, userName);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

    }
}