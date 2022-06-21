package com.faislll.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            Intent goToLoginPage = new Intent(MainActivity.this, Login.class);
            startActivity(goToLoginPage);
            this.finish();
        }

        Button buttonMenuResep = findViewById(R.id.btnMenuResp);
        Button btnProfile = findViewById(R.id.btn_Prfle);
        Button btnLogout = findViewById(R.id.btn_logout);

        btnLogout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        });

        buttonMenuResep.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, Menu.class)));

        btnProfile.setOnClickListener(view -> {
            Intent klikMenu4 = new Intent( MainActivity.this,Profile.class);
            startActivity(klikMenu4);

        });
    }
}