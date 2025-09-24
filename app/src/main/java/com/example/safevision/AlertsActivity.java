package com.example.safevision;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.safevision.api.ApiService;
import com.example.safevision.api.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlertsActivity extends AppCompatActivity {

    private ListView listViewAlerts;
    private ProgressBar progressBar;
    private TextView textViewEmpty;
    private ArrayList<String> alertsList;
    private ArrayAdapter<String> adapter;
    private ApiService apiService;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);

        // Inicializar vistas
        listViewAlerts = findViewById(R.id.listViewAlerts);
        progressBar = findViewById(R.id.progressBar);
        textViewEmpty = findViewById(R.id.textViewEmpty);

        // Inicializar lista y adapter
        alertsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alertsList);
        listViewAlerts.setAdapter(adapter);

        // Inicializar API y preferences
        apiService = ApiClient.getApiService();
        prefs = getSharedPreferences("SafeVision", MODE_PRIVATE);

        // Cargar alertas
        loadAlerts();
    }

    private void loadAlerts() {
        // Verificar si el usuario está logueado
        if (!isUserLoggedIn()) {
            showLoginPrompt();
            return;
        }

        showLoading(true);

        String token = prefs.getString("auth_token", "");
        Call<ResponseBody> call = apiService.getAlerts("Bearer " + token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                showLoading(false);

                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        processAlertsResponse(responseBody);
                    } catch (IOException e) {
                        Log.e("AlertsActivity", "Error reading response", e);
                        showError("Error procesando respuesta");
                    }
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showLoading(false);
                Log.e("AlertsActivity", "Network error", t);
                showError("Error de conexión: " + t.getMessage());

                // Mostrar datos de demo si hay error
                showDemoAlerts();
            }
        });
    }

    private void processAlertsResponse(String responseBody) {
        try {
            JSONArray alertsArray = new JSONArray(responseBody);
            alertsList.clear();

            if (alertsArray.length() == 0) {
                showEmptyState("No hay alertas recientes");
                return;
            }

            for (int i = 0; i < alertsArray.length(); i++) {
                JSONObject alert = alertsArray.getJSONObject(i);
                String alertText = formatAlert(alert);
                alertsList.add(alertText);
            }

            adapter.notifyDataSetChanged();
            textViewEmpty.setVisibility(View.GONE);
            listViewAlerts.setVisibility(View.VISIBLE);

        } catch (JSONException e) {
            Log.e("AlertsActivity", "Error parsing JSON", e);
            showError("Error formateando alertas");
        }
    }

    private String formatAlert(JSONObject alert) throws JSONException {
        String type = alert.getString("alert_type");
        String description = alert.getString("description");
        String timestamp = alert.getString("created_at");

        // Icono según tipo
        String icon = "⚠️"; // Por defecto
        if (type.equals("unknown_face")) {
            icon = "👤";
        } else if (type.equals("suspicious_behavior")) {
            icon = "🚨";
        } else if (type.equals("motion_detection")) {
            icon = "🎥";
        }

        // Formatear fecha
        String formattedDate = formatTimestamp(timestamp);

        return icon + " " + description + " - " + formattedDate;
    }

    private String formatTimestamp(String timestamp) {
        try {
            // Formato original: "2025-09-22T21:10:58"
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

            Date date = inputFormat.parse(timestamp);
            return outputFormat.format(date);
        } catch (Exception e) {
            return timestamp; // Devolver original si hay error
        }
    }

    private void handleErrorResponse(Response<ResponseBody> response) {
        try {
            if (response.code() == 401) {
                showLoginPrompt();
            } else {
                String errorBody = response.errorBody().string();
                showError("Error " + response.code() + ": " + errorBody);
            }
        } catch (IOException e) {
            showError("Error: " + response.code());
        }
    }

    private boolean isUserLoggedIn() {
        String token = prefs.getString("auth_token", "");
        return token != null && !token.isEmpty() && !token.startsWith("demo_token");
    }

    private void showLoginPrompt() {
        alertsList.clear();
        alertsList.add("🔐 Inicia sesión para ver las alertas");
        adapter.notifyDataSetChanged();

        Toast.makeText(this, "Debes iniciar sesión", Toast.LENGTH_LONG).show();

        // Redirigir al login después de 2 segundos
        new android.os.Handler().postDelayed(() -> {
            Intent intent = new Intent(AlertsActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
    }

    private void showDemoAlerts() {
        alertsList.clear();
        alertsList.add("👤 Rostro desconocido detectado - 22/09/2025 21:10");
        alertsList.add("🚨 Movimiento sospechoso detectado - 22/09/2025 20:45");
        alertsList.add("🎥 Actividad nocturna sospechosa - 22/09/2025 02:30");
        alertsList.add("👤 Rostro desconocido detectado - 22/09/2025 01:15");
        alertsList.add("🚨 Vehículo detenido sospechoso - 21/09/2025 23:45");

        adapter.notifyDataSetChanged();
        textViewEmpty.setVisibility(View.GONE);
        listViewAlerts.setVisibility(View.VISIBLE);

        Toast.makeText(this, "Modo demo - Alertas de ejemplo", Toast.LENGTH_SHORT).show();
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        if (show) {
            listViewAlerts.setVisibility(View.GONE);
            textViewEmpty.setVisibility(View.GONE);
        }
    }

    private void showEmptyState(String message) {
        textViewEmpty.setText(message);
        textViewEmpty.setVisibility(View.VISIBLE);
        listViewAlerts.setVisibility(View.GONE);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        textViewEmpty.setText("Error cargando alertas\n\n" + message);
        textViewEmpty.setVisibility(View.VISIBLE);
        listViewAlerts.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar alertas al volver a la actividad
        if (isUserLoggedIn()) {
            loadAlerts();
        }
    }
}