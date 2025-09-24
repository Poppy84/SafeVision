package com.example.safevision;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterFaceActivity extends AppCompatActivity {

    private EditText editTextPersonName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_face);

        editTextPersonName = findViewById(R.id.editTextPersonName);
        Button btnStartCapture = findViewById(R.id.btnStartCapture);

        btnStartCapture.setOnClickListener(v -> startFaceCapture());
    }

    private void startFaceCapture() {
        String personName = editTextPersonName.getText().toString().trim();

        if (personName.isEmpty()) {
            Toast.makeText(this, "Ingresa un nombre para la persona", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, FaceCaptureActivity.class);
        intent.putExtra("person_name", personName);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            int capturesCount = data.getIntExtra("captures_count", 0);
            String personName = data.getStringExtra("person_name");

            Toast.makeText(this,
                    capturesCount + " imágenes de " + personName + " registradas",
                    Toast.LENGTH_LONG).show();

            finish();
        }
    }
}