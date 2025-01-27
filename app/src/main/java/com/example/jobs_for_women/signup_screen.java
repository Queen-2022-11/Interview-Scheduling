package com.example.jobs_for_women;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signup_screen extends AppCompatActivity {

    TextInputEditText et_email, et_password, et_cpassword;
    Button btn_signup;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);

        mAuth = FirebaseAuth.getInstance();
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_cpassword = findViewById(R.id.et_cpassword);
        btn_signup = findViewById(R.id.btn_signup);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sign Up...");

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_email.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String cpassword = et_cpassword.getText().toString().trim();

                if (!password.equals(cpassword)) {
                    et_cpassword.setError("Password is not Matching");
                    et_cpassword.setFocusable(true);
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    et_email.setError("Invalid Email");
                    et_email.setFocusable(true);
                } else if (password.length() < 4 || password.length() > 10) {
                    et_password.setError("Between 4 to 10 alphanumeric Characters");
                    et_password.setFocusable(true);
                } else {
                    registerUser(email, password);
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void registerUser(final String email, String password) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            sendVerificationEmail();
                        } else {
                            Toast.makeText(signup_screen.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(signup_screen.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendVerificationEmail() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(signup_screen.this, "Verification mail sent to your Email", Toast.LENGTH_SHORT).show();
                        signOut(); // Custom sign-out after successful registration
                    } else {
                        Toast.makeText(signup_screen.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void signOut() {
        mAuth.signOut();
        Toast.makeText(signup_screen.this, "Sign-out successful", Toast.LENGTH_SHORT).show();
        // Redirect to the login screen or any other desired activity
        Intent intent = new Intent(getApplicationContext(), login_screen.class);
        startActivity(intent);
        finish();
    }

    public void login_page_open(View view) {
        Intent i = new Intent(getApplicationContext(), login_screen.class);
        startActivity(i);
        finish();
    }
}
