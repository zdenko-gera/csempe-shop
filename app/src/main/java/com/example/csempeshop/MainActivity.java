package com.example.csempeshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final int SECRET_KEY = 6789;
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private SharedPreferences preferences;
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (user != null) {
            findViewById(R.id.loginButton).setVisibility(View.GONE);
            findViewById(R.id.registerButton).setVisibility(View.GONE);
            findViewById(R.id.logoutButton).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.loginButton).setVisibility(View.VISIBLE);
            findViewById(R.id.registerButton).setVisibility(View.VISIBLE);
            findViewById(R.id.logoutButton).setVisibility(View.GONE);
        }
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);

        startActivity(intent);
    }

    public void login(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);

        startActivity(intent);
    }

    public void toShop(View view) {
        Intent intent = new Intent(this, ShopActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);

        startActivity(intent);
    }

    public void logout(View view) {
        mAuth.signOut();
        user = null;
        Toast.makeText(this, "Kijelentkeztél!", Toast.LENGTH_SHORT).show();

        findViewById(R.id.loginButton).setVisibility(View.VISIBLE);
        findViewById(R.id.registerButton).setVisibility(View.VISIBLE);
        findViewById(R.id.logoutButton).setVisibility(View.GONE);
    }
}