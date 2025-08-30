package com.example.safevision;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText editTextForgotEmail;
    private Button buttonRecover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editTextForgotEmail = findViewById(R.id.editTextForgotEmail);
        buttonRecover = findViewById(R.id.buttonRecover);

        buttonRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextForgotEmail.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Ingresa tu correo", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Se envió un enlace de recuperación a " + email, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
