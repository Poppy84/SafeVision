package com.example.safevision;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private TextView tvWelcome, tvStatus, tvStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("SafeVision", MODE_PRIVATE);
        checkAuthentication();

        setupUI();
        updateDashboard();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDashboard(); // Actualizar datos al volver a la actividad
    }

    private void checkAuthentication() {
        if (!prefs.contains("auth_token") || prefs.getString("auth_token", "").isEmpty()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setupUI() {
        tvWelcome = findViewById(R.id.tvWelcome);
        tvStatus = findViewById(R.id.tvStatus);
        tvStats = findViewById(R.id.tvStats);

        Button btnRegisterFace = findViewById(R.id.btnRegisterFace);
        Button btnViewFaces = findViewById(R.id.btnViewFaces);
        Button btnAlerts = findViewById(R.id.btnAlerts);
        Button btnLiveView = findViewById(R.id.btnLiveView);
        Button btnSettings = findViewById(R.id.btnSettings);
        Button btnLogout = findViewById(R.id.btnLogout);

        // Actualizar información del usuario
        String username = prefs.getString("username", "Usuario");
        String fullName = prefs.getString("full_name", "");

        if (!fullName.isEmpty()) {
            tvWelcome.setText("¡Hola, " + fullName + "!");
        } else {
            tvWelcome.setText("¡Hola, " + username + "!");
        }

        // Configurar listeners de botones
        btnRegisterFace.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterFaceActivity.class);
            startActivity(intent);
        });

        btnViewFaces.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FaceManagementActivity.class);
            startActivity(intent);
        });

        btnAlerts.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AlertsActivity.class);
            startActivity(intent);
        });

        btnLiveView.setOnClickListener(v -> {
            openLiveView();
        });

        btnSettings.setOnClickListener(v -> {
            Toast.makeText(this, "Configuración próximamente", Toast.LENGTH_SHORT).show();
        });

        btnLogout.setOnClickListener(v -> {
            logout();
        });
    }

    private void updateDashboard() {
        // Actualizar estado del sistema
        boolean isDemoMode = prefs.getBoolean("demo_mode", false);

        if (isDemoMode) {
            tvStatus.setText("🔶 Modo Demo Activado");
            tvStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_orange_dark));
            tvStats.setText("Sistema en modo demostración\nConecta a tu servidor SafeVision");
        } else {
            tvStatus.setText("✅ Conectado al Servidor");
            tvStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
            tvStats.setText("Monitoreo activo las 24/7\nAlertas en tiempo real");
        }
    }

    private void openLiveView() {
        // Verificar si estamos en modo demo
        boolean isDemoMode = prefs.getBoolean("demo_mode", false);

        if (isDemoMode) {
            Toast.makeText(this, "Modo demo: La vista en vivo requiere conexión real", Toast.LENGTH_LONG).show();
            return;
        }

        // Abrir vista en vivo en el navegador o WebView
        try {
            String serverUrl = "http://10.167.78.183:5000/monitor"; // Tu IP del servidor
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(serverUrl));
            startActivity(browserIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Error abriendo vista en vivo: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void logout() {
        // Mostrar diálogo de confirmación
        new android.app.AlertDialog.Builder(this)
                .setTitle("Cerrar Sesión")
                .setMessage("¿Estás seguro de que quieres cerrar sesión?")
                .setPositiveButton("Sí", (dialog, which) -> performLogout())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void performLogout() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(this, "Sesión cerrada exitosamente", Toast.LENGTH_SHORT).show();

        // Redirigir al login
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}