package com.moviles.marvelapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class AuthActivity extends AppCompatActivity {

    Button registroButton, loginButton;
    EditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);

        registroButton = findViewById(R.id.registroButton);
        loginButton = findViewById(R.id.loginButton);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        //Configuración de la autenticación
        setup();
    }

    private void setup() {
        setTitle("Autenticación");

        registroButton.setOnClickListener(view -> {
            if (!emailEditText.getText().toString().isEmpty() && !passwordEditText.getText().toString().isEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showHome(Objects.requireNonNull(task.getResult().getUser()).getEmail(), ProviderType.BASIC);
                    } else {
                        showAlert();
                    }
                });
            } else {
                showAlert("Por favor ingrese email y contraseña.");
            }
        });

        loginButton.setOnClickListener(view -> {
            if (!emailEditText.getText().toString().isEmpty() && !passwordEditText.getText().toString().isEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showHome(Objects.requireNonNull(task.getResult().getUser()).getEmail(), ProviderType.BASIC);
                    } else {
                        showAlert();
                    }
                });
            } else {
                showAlert("Por favor ingrese email y contraseña.");
            }
        });
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Se ha producido un error autenticando al usuario");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Método sobrecargado para mostrar mensajes personalizados
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage(message);
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showHome(String email, ProviderType provider) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("provider", provider.name());
        startActivity(intent);
        finish();
    }
}
