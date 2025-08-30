package com.example.safevision;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextNewEmail, editTextNewPassword, editTextNewName;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextNewEmail = findViewById(R.id.editTextRegisterEmail);
        editTextNewPassword = findViewById(R.id.editTextRegisterPassword);
        editTextNewName = findViewById(R.id.editTextName);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEmail = editTextNewEmail.getText().toString().trim().toLowerCase();
                String newPassword = editTextNewPassword.getText().toString().trim();
                String newName = editTextNewName.getText().toString().trim();


                if (newEmail.isEmpty() || newPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    // Guardar usuario en SharedPreferences
                    SharedPreferences prefs = getSharedPreferences("SafeVisionPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("correo", newEmail);
                    editor.putString("password", newPassword);
                    editor.putString("name", newName);
                    editor.apply();

                    Toast.makeText(RegisterActivity.this, "Usuario registrado", Toast.LENGTH_SHORT).show();

                    // Regresar al login
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
