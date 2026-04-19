package com.fitpro.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvError;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db    = FirebaseFirestore.getInstance();

        etEmail    = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin   = findViewById(R.id.btnLogin);
        tvError    = findViewById(R.id.tvError);

        btnLogin.setOnClickListener(v -> login());
    }

    private void login() {
        String email    = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        Log.d("LOGIN", "Email: " + email + " | Password: " + password);

        if (email.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs");
            return;
        }

        btnLogin.setEnabled(false);
        showError("Connexion en cours...");

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    Log.d("LOGIN", "✅ Auth réussie !");
                    String uid = authResult.getUser().getUid();

                    db.collection("users").document(uid).get()
                            .addOnSuccessListener(doc -> {
                                String role = doc.getString("role");
                                Log.d("LOGIN", "Role: " + role);

                                if ("admin".equals(role)) {
                                    startActivity(new Intent(this, AdminActivity.class));
                                } else {
                                    startActivity(new Intent(this, MainActivity.class));
                                }
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Log.e("LOGIN", "❌ Firestore: " + e.getMessage());
                                showError("Erreur profil: " + e.getMessage());
                                btnLogin.setEnabled(true);
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e("LOGIN", "❌ Auth: " + e.getMessage());
                    showError("Email ou mot de passe incorrect");
                    btnLogin.setEnabled(true);
                });
    }

    private void showError(String message) {
        tvError.setText(message);
        tvError.setVisibility(View.VISIBLE);
    }
}