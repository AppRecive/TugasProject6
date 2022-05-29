package com.faislll.myapplication;

import androidx.appcompat.app.AppCompatActivity;
//import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnMenu1, btnMenu2, btnMenu3, btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMenu1 = findViewById(R.id.btn_menuSarapan);
        btnMenu2 = findViewById(R.id.btn_MenuUtama);
        btnMenu3 = findViewById(R.id.btn_Menukue);
        btnProfile = findViewById(R.id.btn_Prfle);


        btnMenu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent klikMenu1 = new Intent( MainActivity.this,MenuSarapan.class);
                startActivity(klikMenu1);

            }
        });

        btnMenu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent klikMenu2 = new Intent( MainActivity.this,MenuMasakan.class);
                startActivity(klikMenu2);

            }
        });

        btnMenu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent klikMenu3 = new Intent( MainActivity.this,MenuKue.class);
                startActivity(klikMenu3);

            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent klikMenu4 = new Intent( MainActivity.this,Profile.class);
                startActivity(klikMenu4);

            }
        });
    }
}