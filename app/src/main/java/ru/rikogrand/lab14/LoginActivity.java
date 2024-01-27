package ru.rikogrand.lab14;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextLoginEmail;
    private EditText editTextLoginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextLoginEmail = findViewById(R.id.editTextLoginEmail);
        editTextLoginPassword = findViewById(R.id.editTextLoginPassword);

        Button buttonLogin = findViewById(R.id.buttonLogin);

        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        if (preferences.getBoolean("rememberMe", false)) {
            editTextLoginEmail.setText(preferences.getString("email", ""));
            editTextLoginPassword.setText(preferences.getString("password", ""));
        }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateLogin();
            }
        });
    }

    private void validateLogin() {
        String loginEmail = editTextLoginEmail.getText().toString().trim();
        String loginPassword = editTextLoginPassword.getText().toString();

        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        String storedEmail = preferences.getString("email", "");
        String storedPassword = preferences.getString("password", "");

        if (loginEmail.equals(storedEmail) && loginPassword.equals(storedPassword)) {
            Intent intent = new Intent(this, SuccessActivity.class);
            startActivity(intent);

            Toast.makeText(this, "Вход успешно выполнен", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Неверные данные. Попробуйсте снова", Toast.LENGTH_SHORT).show();
        }
    }
}