package com.example.csempeshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 6789;
    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    EditText emailEditText;
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 6789) {
            finish();
        }

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        String prefEmail = preferences.getString("email", "");
        String prefPassword = preferences.getString("password", "");

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", emailEditText.getText().toString());
        editor.putString("password", passwordEditText.getText().toString());

        editor.apply();
    }

    public void login(View view) {
        String email =  emailEditText.getText().toString();
        String password =  passwordEditText.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("SUCCESS", "User logged in successfully.");

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("SECRET_KEY", SECRET_KEY);

                    startActivity(intent);

                    Toast.makeText(LoginActivity.this, "Sikeresen bejelentkeztél!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("ERROR", "User wasn't logged in.");
                    Toast.makeText(LoginActivity.this, "Sikertelen bejelentkezés: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void cancel(View view) {
        finish();
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);

        startActivity(intent);
    }
}