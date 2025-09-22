package com.example.safevision;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = getSharedPreferences("SafeVision", MODE_PRIVATE);

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);

        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simular login exitoso
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("auth_token", "demo_token_" + System.currentTimeMillis());
        editor.putString("username", username);
        editor.apply();

        Toast.makeText(this, "¡Bienvenido " + username + "!", Toast.LENGTH_SHORT).show();

        // Ir a MainActivity (pantalla principal)
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // Cerrar LoginActivity para que no se pueda volver con back
    }
}