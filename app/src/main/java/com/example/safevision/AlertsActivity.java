package com.example.safevision;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AlertsActivity extends AppCompatActivity {

    private ListView listViewAlerts;
    private ArrayList<String> alertsList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);

        listViewAlerts = findViewById(R.id.listViewAlerts);

        // 🔹 Datos temporales simulando alertas
        alertsList = new ArrayList<>();
        alertsList.add("⚠️  Rostro desconocido detectado - 19/08/2025 02:30");
        alertsList.add("🚶  Movimiento sospechoso en el perímetro - 18/08/2025 23:15");
        alertsList.add("✅  Rostro conocido (Juan Pérez) - 18/08/2025 22:00");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alertsList);
        listViewAlerts.setAdapter(adapter);
    }
}
