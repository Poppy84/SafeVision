package com.example.safevision;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    TextView textWelcome;
    Button btnCamaras, btnAlertas, btnConfig, btnCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        textWelcome = findViewById(R.id.textWelcome);
        btnCamaras = findViewById(R.id.btnCamaras);
        btnAlertas = findViewById(R.id.btnAlertas);
        btnConfig = findViewById(R.id.btnConfig);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        // Bienvenida con el usuario guardado
        SharedPreferences prefs = getSharedPreferences("SafeVisionPrefs", MODE_PRIVATE);
        String name = prefs.getString("name", "usuario");
        textWelcome.setText("Bienvenido, " + name);

        // Boton que abre la pantalla de las cámaras
        btnCamaras.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, CameraActivity.class);
                startActivity(intent);
                finish();

        });

        //abre la pantalla de alertas
        btnAlertas.setOnClickListener(v -> {
           // Toast.makeText(this, "Abrir alertas (pendiente)", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HomeActivity.this, CameraActivity.class);
            startActivity(intent);
            finish();
        });

        //Agregar las llaves para el bloque de código para poder usar lambda
        btnConfig.setOnClickListener(v ->
                Toast.makeText(this, "Abrir configuración (pendiente)", Toast.LENGTH_SHORT).show()
        );

        btnCerrarSesion.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
