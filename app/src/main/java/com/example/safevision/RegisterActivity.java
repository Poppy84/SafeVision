package com.example.safevision;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.safevision.api.ApiService;
import com.example.safevision.api.ApiClient;
import com.example.safevision.models.User;
import com.example.safevision.models.ApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.ResponseBody;
import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        apiService = ApiClient.getApiService();

        Button btnRegister = findViewById(R.id.btnRegister);
        Button btnBackToLogin = findViewById(R.id.btnBackToLogin);

        btnRegister.setOnClickListener(v -> attemptRegister());
        btnBackToLogin.setOnClickListener(v -> goBackToLogin());
    }

    private void attemptRegister() {
        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etFullName = findViewById(R.id.etFullName);

        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String fullName = etFullName.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Completa los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Crear objeto usuario para la API
        User user = new User(username, password, email, fullName);

        Toast.makeText(this, "Registrando usuario...", Toast.LENGTH_SHORT).show();

        Call<ResponseBody> call = apiService.register(user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // ✅ REGISTRO EXITOSO - La API devuelve un JSON simple
                    try {
                        String responseBody = response.body().string();
                        handleRegisterSuccess(responseBody);
                    } catch (IOException e) {
                        handleRegisterError("Error procesando respuesta: " + e.getMessage());
                    }
                } else {
                    // ❌ Error en el registro - Manejar error de la API
                    try {
                        String errorBody = response.errorBody().string();
                        handleRegisterError("Error: " + errorBody);
                    } catch (IOException e) {
                        handleRegisterError("Error en el registro: " + response.code() + " - " + response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                handleRegisterError("Error de conexión: " + t.getMessage());
            }
        });
    }

    private void handleRegisterSuccess(String responseBody) {
        Toast.makeText(this, "¡Usuario registrado exitosamente!", Toast.LENGTH_SHORT).show();

        // Mostrar respuesta del servidor en logs
        System.out.println("Respuesta del servidor: " + responseBody);

        // Volver al login
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("new_user", true);
        startActivity(intent);
        finish();
    }

    private void handleRegisterError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        System.out.println("Error en registro: " + errorMessage);
    }

    private void goBackToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}