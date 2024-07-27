package com.example.jobs_for_women;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class dashboard extends AppCompatActivity {
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        firebaseAuth = FirebaseAuth.getInstance();
        //checkAndNavigate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            firebaseAuth.signOut();
            checkAndNavigate();
        }
        return super.onOptionsItemSelected(item);
    }

    public void profile_page_open(View view) {
        startActivity(new Intent(getApplicationContext(), profile_page.class));
    }

    public void add_candidate_page_open(View view) {
        startActivity(new Intent(getApplicationContext(), add_candidate.class));
    }

    public void candidate_view_page_open(View view) {
        startActivity(new Intent(getApplicationContext(), candidate_view.class));
    }



    private void checkAndNavigate() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null && user.isEmailVerified()) {
            // User signed in and email is verified, stay here
        } else {
            // User not signed in or email not verified, go to MainActivity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }
}
