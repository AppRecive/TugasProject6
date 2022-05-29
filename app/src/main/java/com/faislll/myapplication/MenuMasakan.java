package com.faislll.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuMasakan extends AppCompatActivity {
    private Button btnGado, btnSoto, btnSate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_masakan);

        btnGado = findViewById(R.id.btnmsk_Gado);

        btnSate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent klikMenu1 = new Intent( MenuMasakan.this,MenuDetailSarapan.class);
                startActivity(klikMenu1);

            }
        });
    }
}