package com.example.safevision;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CameraActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private ImageView imageViewPreview;
    private TextView textViewResult;
    private Button buttonCapture;

    private Button buttonAlerts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        //Busca los identificadores de los elementos en la clase R
        imageViewPreview = findViewById(R.id.imageViewPreview);
        textViewResult = findViewById(R.id.textViewResult);
        buttonCapture = findViewById(R.id.buttonCapture);
        buttonAlerts = findViewById(R.id.buttonAlerts);

        buttonCapture.setOnClickListener(v -> {
            // Llamar a la cámara del celular
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });

        //abre la pantalla de alertas
        buttonAlerts.setOnClickListener(v -> {
            Intent intent = new Intent(CameraActivity.this, AlertsActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageViewPreview.setImageBitmap(imageBitmap);

            //  Simulación de respuesta de la IA
            String fakeResult = "Persona conocida (simulación)";
            textViewResult.setText("Resultado: " + fakeResult);

            Toast.makeText(this, "Imagen enviada a IA (simulada)", Toast.LENGTH_SHORT).show();
        }
    }
}
