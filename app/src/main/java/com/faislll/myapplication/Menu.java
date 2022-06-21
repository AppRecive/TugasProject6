package com.faislll.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.faislll.myapplication.adapter.MenuAdapter;
import com.faislll.myapplication.model.Reseps;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Menu extends AppCompatActivity {
    private static final String TAG = "TAG_MENU ";
    private RecyclerView recyclerView;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private MenuAdapter adapter;
    private ArrayList<Reseps> resepsData = new ArrayList<>();
    private Toolbar toolbar;
    private ProgressBar loadingItems;
    private FloatingActionButton buttonAdd;

    @SuppressLint({"NotifyDataSetChanged", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        recyclerView = findViewById(R.id.rv_menu);
        toolbar = findViewById(R.id.toolbar_list_menu);
        loadingItems = findViewById(R.id.loading_menu);
        buttonAdd = findViewById(R.id.add_menu);


        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24));
        toolbar.setNavigationOnClickListener(v -> {
            this.finish();
        });

        try {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
            adapter = new MenuAdapter(getApplicationContext(), resepsData);
            recyclerView.setAdapter(adapter);

            database.collection("reseps").get().addOnSuccessListener(queryDocumentSnapshots -> {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                for (DocumentSnapshot snapshot : list){
                    Reseps reseps = snapshot.toObject(Reseps.class);
                    resepsData.add(reseps);
                }
                adapter.notifyDataSetChanged();
                loadingItems.setVisibility(View.GONE);

            }).addOnCompleteListener(task -> {
                Log.i(TAG, "onCreate: OnComplete__" + task.getResult().getDocuments().toString());
            }).addOnFailureListener(e -> {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } catch (Exception e){
            e.printStackTrace();
        }

        buttonAdd.setOnClickListener(v -> {
            Intent intentNew = new Intent(Menu.this, NewMenu.class);
            intentNew.putExtra(MenuAdapter.ACTION_STATE, MenuAdapter.ACTION_NEW);
            intentNew.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentNew);
        });
    }
}