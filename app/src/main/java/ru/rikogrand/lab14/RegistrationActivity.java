package ru.rikogrand.lab14;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RegistrationActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextNumber;
    private EditText editTextDateOfBirth;
    private EditText editTextPassword;
    private EditText editTextRepeatPassword;
    private CheckBox checkBoxRememberMe;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextNumber = findViewById(R.id.editTextNumber);
        editTextDateOfBirth = findViewById(R.id.editTextDateOfBirth);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextRepeatPassword = findViewById(R.id.editTextRepeatPassword);
        checkBoxRememberMe = findViewById(R.id.checkBoxRememberMe);
        Button buttonRegister = findViewById(R.id.buttonRegister);

        PhoneNumberFormatter.formatPhoneNumber(editTextNumber);
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );


        editTextDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateAndSaveData()) {
                    handleRegistrationSuccess();
                }
            }
        });
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(Calendar.YEAR, year);
            selectedDate.set(Calendar.MONTH, monthOfYear);
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            editTextDateOfBirth.setText(sdf.format(selectedDate.getTime()));
        }
    };

    private boolean validateAndSaveData() {
        String email = editTextEmail.getText().toString().trim();
        String number = editTextNumber.getText().toString().trim();
        String dateOfBirth = editTextDateOfBirth.getText().toString().trim();
        String password = editTextPassword.getText().toString();
        String repeatPassword = editTextRepeatPassword.getText().toString();
        boolean rememberMe = checkBoxRememberMe.isChecked();



        if (!isValidEmail(email)) {
            editTextEmail.setError("Неверный email адрес");
            return false;
        }
        if(!isEmailAlreadyInUse(email)){
            editTextEmail.setError("Данная почта уже используется");
            return false;
        }


        if (!isValidPhoneNumber(number)) {
            editTextNumber.setError("Неверный номер телефона");
            return false;
        }

        if (!isValidDateOfBirth(dateOfBirth)) {
            editTextDateOfBirth.setError("Неверная дата рождения");
            return false;
        }

        if (!isValidPassword(password)) {
            editTextPassword.setError("Неверный пароль. Длина пароля должна быть не менее 8 символов, должна присутстовать хотя бы 1 заглавная буква и 1 цифра");
            return false;
        }

        if (!password.equals(repeatPassword)) {
            editTextRepeatPassword.setError("Пароли не совпадают");
            return false;
        }



        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String hashedPassword = password;

        editor.putString("email", email);
        editor.putString("number", number);
        editor.putString("dateOfBirth", dateOfBirth);
        editor.putString("password", hashedPassword);
        editor.putBoolean("rememberMe", rememberMe);
        editor.apply();

        return true;
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        String domainRegex = "^[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";

        if (!email.matches(emailRegex)) {
            return false;
        }

        String[] parts = email.split("@");
        if (parts.length == 2 && !parts[1].matches(domainRegex)) {
        }

        return true;

    }
    private boolean isEmailAlreadyInUse(String email) {
        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        if(preferences.contains("email") && preferences.getString("email", "").equals(email)) {
            return false;
        }
        else {
        return true;

        }


       // SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
      //  String existingEmail = preferences.getString("email", "");
       // return existingEmail.equals(email);
    }
    private boolean isValidPhoneNumber(String number) {
        if (number == null || number.isEmpty()) {
            return false;
        }

        String digitsOnly = number.replaceAll("[^0-9]", "");

        String phoneRegex = "^\\+7\\s\\(\\d{3}\\)\\s\\d{3}-\\d{2}-\\d{2}$";

        if (digitsOnly.length() < 11) {
            return false;
        }

        return true;
    }

    private boolean isValidDateOfBirth(String dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.isEmpty()) {
            return false;
        }

        String dateRegex = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$";

        if (!dateOfBirth.matches(dateRegex)) {
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date dob = sdf.parse(dateOfBirth);
            Calendar dobCalendar = Calendar.getInstance();
            dobCalendar.setTime(dob);

            Calendar currentDate = Calendar.getInstance();


            currentDate.add(Calendar.YEAR, -18);

            if (dobCalendar.after(currentDate)) {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }

        if (password.length() < 8) {
            return false;
        }

        if (!password.matches(".*[A-Z].*")) {
            return false;
        }

        if (!password.matches(".*[a-z].*")) {
            return false;
        }

        if (!password.matches(".*\\d.*")) {
            return false;
        }

        return true;
    }

    private void handleRegistrationSuccess() {
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);

        Toast.makeText(RegistrationActivity.this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();
    }
}