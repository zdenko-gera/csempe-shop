package com.example.csempeshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private static final int SECRET_KEY = 6789;
    private static final String PREF_KEY = RegisterActivity.class.getPackage().toString();
    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    EditText lastnameEditText;
    EditText firstnameEditText;
    EditText emailEditText;
    EditText mobileEditText;
    EditText passwordEditText;
    EditText confirmPasswordEditText;
    RadioGroup accountTypeRadioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 6789) {
            finish();
        }

        lastnameEditText = findViewById(R.id.lastnameEditText);
        firstnameEditText = findViewById(R.id.firstnameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        mobileEditText = findViewById(R.id.mobileEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        accountTypeRadioGroup = findViewById(R.id.accountTypeRadioGroup);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        String prefEmail = preferences.getString("email", "");
        String prefPassword = preferences.getString("password", "");

        emailEditText.setText(prefEmail);
        passwordEditText.setText(prefPassword);
        confirmPasswordEditText.setText(prefPassword);

        mAuth = FirebaseAuth.getInstance();

    }

    public void register(View view) {
        String lastname =  lastnameEditText.getText().toString();
        String firstname =  firstnameEditText.getText().toString();
        String email =  emailEditText.getText().toString();
        String mobile =  mobileEditText.getText().toString();
        String password =  passwordEditText.getText().toString();
        String confirmPassword =  confirmPasswordEditText.getText().toString();

        int accountTypeCheckedId = accountTypeRadioGroup.getCheckedRadioButtonId();
        RadioButton accountTypeButton = accountTypeRadioGroup.findViewById(accountTypeCheckedId);
        String accountType = accountTypeButton.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("SUCCESS", "User created successfully.");

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.putExtra("SECRET_KEY", SECRET_KEY);

                    startActivity(intent);

                    Toast.makeText(RegisterActivity.this, "Sikeresen regisztráltál,  " + firstname, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("ERROR", "User wasn't created.");
                    Toast.makeText(RegisterActivity.this, "No user created: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void cancel(View view) {
        finish();
    }
}