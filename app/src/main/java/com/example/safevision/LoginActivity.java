package com.example.safevision;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// ✅ Importaciones para la API
import com.example.safevision.api.ApiService;
import com.example.safevision.api.ApiClient;
import com.example.safevision.models.LoginRequest;
import com.example.safevision.models.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = getSharedPreferences("SafeVision", MODE_PRIVATE);
        apiService = ApiClient.getApiService(); // ✅ Inicializar servicio API

        // Verificar si ya está logueado
        checkIfLoggedIn();

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v -> attemptLogin());
        btnRegister.setOnClickListener(v -> openRegisterActivity());
    }

    private void checkIfLoggedIn() {
        if (prefs.contains("auth_token") && !prefs.getBoolean("demo_mode", false)) {
            // Solo redirigir si es un token real, no demo
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void attemptLogin() {
        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);

        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mostrar loading
        Toast.makeText(this, "Conectando...", Toast.LENGTH_SHORT).show();

        // ✅ CONEXIÓN REAL CON TU API SAFEVISION
        LoginRequest loginRequest = new LoginRequest(username, password);

        Call<LoginResponse> call = apiService.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // ✅ LOGIN EXITOSO CON LA API
                    handleLoginSuccess(response.body());
                } else {
                    // ❌ Error en las credenciales
                    handleLoginError("Usuario o contraseña incorrectos");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // ❌ Error de conexión al servidor
                handleConnectionError(username, t.getMessage());
            }
        });
    }

    private void handleLoginSuccess(LoginResponse loginResponse) {
        // Guardar datos reales del usuario
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("auth_token", loginResponse.getToken());
        editor.putString("username", loginResponse.getUsername());
        editor.putString("full_name", loginResponse.getFullName());
        editor.putString("email", loginResponse.getEmail());
        editor.putInt("user_id", loginResponse.getUserId());
        editor.putBoolean("demo_mode", false); // ✅ No es modo demo
        editor.apply();

        String welcomeName = loginResponse.getFullName() != null ?
                loginResponse.getFullName() : loginResponse.getUsername();

        Toast.makeText(this, "¡Bienvenido " + welcomeName + "!", Toast.LENGTH_SHORT).show();

        // Ir a MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void handleLoginError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();

        // Opcional: Limpiar campos de password
        EditText etPassword = findViewById(R.id.etPassword);
        etPassword.setText("");
    }

    private void handleConnectionError(String username, String errorDetails) {
        // ✅ Ofrecer modo demo cuando no hay conexión
        Toast.makeText(this, "Error de conexión al servidor", Toast.LENGTH_SHORT).show();

        // Preguntar si quiere usar modo demo
        new android.app.AlertDialog.Builder(this)
                .setTitle("Sin conexión")
                .setMessage("No se puede conectar al servidor. ¿Usar modo demo?")
                .setPositiveButton("Sí", (dialog, which) -> enableDemoMode(username))
                .setNegativeButton("Reintentar", (dialog, which) -> attemptLogin())
                .setNeutralButton("Cancelar", null)
                .show();
    }

    private void enableDemoMode(String username) {
        // ✅ Activar modo demo
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("auth_token", "demo_token_" + System.currentTimeMillis());
        editor.putString("username", username);
        editor.putString("full_name", username);
        editor.putBoolean("demo_mode", true); // ✅ Marcar como demo
        editor.apply();

        Toast.makeText(this, "Modo demo activado", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void openRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Limpiar modo demo al volver al login
        if (prefs.getBoolean("demo_mode", false)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("demo_mode");
            editor.remove("auth_token");
            editor.apply();
        }
    }
}