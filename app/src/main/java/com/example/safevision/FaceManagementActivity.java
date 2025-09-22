package com.example.safevision;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class FaceManagementActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_management);

        // CORREGIDO: Usar findViewById con los IDs correctos
        TextView tvTitle = findViewById(R.id.tvTitle);
        Button btnAddFace = findViewById(R.id.btnAddFace);
        Button btnViewFaces = findViewById(R.id.btnViewFaces);
        Button btnBack = findViewById(R.id.btnBack);

        tvTitle.setText("Gestión de Rostros");

        btnAddFace.setOnClickListener(v -> {
            // Abrir galería para seleccionar imagen
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"), PICK_IMAGE_REQUEST);
        });

        btnViewFaces.setOnClickListener(v -> {
            Toast.makeText(this, "Lista de rostros registrados próximamente", Toast.LENGTH_SHORT).show();
        });

        btnBack.setOnClickListener(v -> {
            finish(); // Volver a MainActivity
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                String fileName = imageUri.getLastPathSegment();
                Toast.makeText(this, "Imagen seleccionada: " + fileName, Toast.LENGTH_SHORT).show();

                // Aquí procesarías la imagen para el reconocimiento facial
                // Por ahora solo mostramos un mensaje
                Toast.makeText(this, "Rostro agregado al sistema", Toast.LENGTH_LONG).show();
            }
        }
    }
}