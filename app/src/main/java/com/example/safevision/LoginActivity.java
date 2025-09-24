package com.example.safevision;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// ✅ Importaciones para la API REAL (usando tus clases existentes)
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
        apiService = ApiClient.getApiService(); // ✅ Usar tu ApiClient existente

        // Verificar si ya está logueado
        checkIfLoggedIn();

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v -> attemptLogin());
        btnRegister.setOnClickListener(v -> openRegisterActivity());
    }

    private void checkIfLoggedIn() {
        if (prefs.contains("auth_token") && !prefs.getBoolean("demo_mode", false)) {
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

        // ✅ CONEXIÓN REAL CON TU SERVIDOR (NO MÁS SIMULACIÓN)
        performRealLogin(username, password);
    }

    private void performRealLogin(String username, String password) {
        Toast.makeText(this, "Conectando al servidor...", Toast.LENGTH_SHORT).show();

        LoginRequest loginRequest = new LoginRequest(username, password);

        Call<LoginResponse> call = apiService.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // ✅ LOGIN EXITOSO CON EL SERVIDOR REAL
                    handleRealLoginSuccess(response.body());
                } else {
                    // ❌ Error en las credenciales o servidor
                    String errorMessage = "Error en el login";
                    if (response.code() == 401) {
                        errorMessage = "Usuario o contraseña incorrectos";
                    } else if (response.code() == 500) {
                        errorMessage = "Error interno del servidor";
                    }
                    handleLoginError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // ❌ Error de conexión al servidor (red, timeout, etc.)
                handleConnectionError(username, t.getMessage());
            }
        });
    }

    private void handleRealLoginSuccess(LoginResponse loginResponse) {
        // Guardar datos reales del usuario desde el servidor
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

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void handleLoginError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();

        // Limpiar campo de password
        EditText etPassword = findViewById(R.id.etPassword);
        etPassword.setText("");
    }

    private void handleConnectionError(String username, String errorDetails) {
        // ✅ Mostrar error REAL de conexión
        Toast.makeText(this, "Error de conexión al servidor", Toast.LENGTH_SHORT).show();

        // Preguntar si quiere usar modo demo
        new android.app.AlertDialog.Builder(this)
                .setTitle("Sin conexión al servidor")
                .setMessage("No se puede conectar al servidor SafeVision (" + errorDetails + "). ¿Usar modo demo?")
                .setPositiveButton("Modo Demo", (dialog, which) -> enableDemoMode(username))
                .setNegativeButton("Reintentar", (dialog, which) -> attemptLogin())
                .setNeutralButton("Cancelar", null)
                .show();
    }

    private void enableDemoMode(String username) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("auth_token", "demo_token_" + System.currentTimeMillis());
        editor.putString("username", username);
        editor.putString("full_name", username);
        editor.putBoolean("demo_mode", true);
        editor.apply();

        Toast.makeText(this, "Modo demo activado", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void openRegisterActivity() {
        Toast.makeText(this, "Registro próximamente", Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(this, RegisterActivity.class);
        // startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs.getBoolean("demo_mode", false)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("demo_mode");
            editor.remove("auth_token");
            editor.apply();
        }
    }
}