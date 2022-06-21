package com.faislll.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuMasakan extends AppCompatActivity {
    private Button btnGado, btnSoto, btnSate;
    private Toolbar topToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_masakan);

        btnGado = findViewById(R.id.btnmsk_Gado);
        topToolbar = findViewById(R.id.toolbar_top);

        topToolbar.setTitle("Menu Masakan");

        btnGado.setOnClickListener(view -> {
            Intent klikMenu1 = new Intent( MenuMasakan.this,MenuDetailSarapan.class);
            startActivity(klikMenu1);
        });
    }
}