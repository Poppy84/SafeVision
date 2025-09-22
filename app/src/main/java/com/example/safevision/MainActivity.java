package com.example.safevision;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("SafeVision", MODE_PRIVATE);
        checkAuthentication();

        setupUI();
    }

    private void checkAuthentication() {
        if (!prefs.contains("auth_token")) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setupUI() {
        TextView tvWelcome = findViewById(R.id.tvWelcome);
        Button btnFaces = findViewById(R.id.btnFaces);
        Button btnAlerts = findViewById(R.id.btnAlerts);
        Button btnLogout = findViewById(R.id.btnLogout);

        String username = prefs.getString("username", "Usuario");
        tvWelcome.setText("Hola, " + username);

        btnFaces.setOnClickListener(v -> {
            // Llamar a FaceManagementActivity
            Intent intent = new Intent(MainActivity.this, FaceManagementActivity.class);
            startActivity(intent);
        });

        btnAlerts.setOnClickListener(v -> {
            Toast.makeText(this, "Alertas próximamente", Toast.LENGTH_SHORT).show();
        });

        btnLogout.setOnClickListener(v -> {
            logout();
        });
    }

    private void logout() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}