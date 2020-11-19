package com.eng.safeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private TextView registerTitle, registerUserBtn;
    private EditText editTextFullName, editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        registerTitle = findViewById(R.id.registerTitle);
        registerTitle.setOnClickListener(this);

        registerUserBtn = (Button) findViewById(R.id.registerBtn);
        registerUserBtn.setOnClickListener(this);

        editTextFullName = findViewById(R.id.registerFullNameInput);
        editTextEmail = findViewById(R.id.forgotPasswordEmailInput);
        editTextPassword = findViewById(R.id.loginPasswordInput);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerTitle:
                startActivity(new Intent(this, Login.class));
                break;
            case R.id.registerBtn:
                System.out.println("Clicking Register Button!");
                registerUser();
                break;
        }
    }

    private void registerUser() {
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        final String fullName = editTextFullName.getText().toString().trim();

        if (fullName.isEmpty()) {
            editTextFullName.setError("Full name is required!");
            editTextFullName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide valid email!");
            editTextEmail.requestFocus();
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Min password length should be 6 characters!");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(fullName, email);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();

                                        Toast t = new Toast(getApplicationContext());
                                        View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.toast_layout, null);
                                        t.setView(v);
                                        t.setDuration(Toast.LENGTH_LONG);
                                        t.setGravity(Gravity.CENTER, 0, 0);
                                        t.show();
                                        startActivity(new Intent(getApplicationContext(), Login.class));
                                    } else {
                                        Toast t = Toast.makeText(Register.this, "Failed to register! Try again!", Toast.LENGTH_LONG);
                                        t.setGravity(Gravity.CENTER, 0, 0);
                                        t.show();
                                    }
                                }
                            });
                        } else {
                            Toast t = Toast.makeText(Register.this, "Failed to register!", Toast.LENGTH_LONG);
                        }
                    }
                });

    }
}