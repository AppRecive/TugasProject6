package com.faislll.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.faislll.myapplication.adapter.MenuAdapter;
import com.faislll.myapplication.model.Reseps;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class MenuDetailMasakan extends AppCompatActivity {

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail_masakan);
        TextView textViewBahan = findViewById(R.id.t_view_bahan);
        TextView textViewNama = findViewById(R.id.t_view_nama);
        TextView textViewCaraMemasak = findViewById(R.id.t_view_caramasak);
        TextView textViewDeskripsi = findViewById(R.id.t_view_deskripsi);
        ImageView imageViewMasakan = findViewById(R.id.img_menu);
        FloatingActionButton floatingActionButtonEdit = findViewById(R.id.edit_menu);
        FloatingActionButton floatingActionButtonDelete = findViewById(R.id.hapus);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        Toolbar toolbar = findViewById(R.id.toolbar_detail_masakan);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24));
        toolbar.setNavigationOnClickListener(v -> this.finish());

        Intent getExtraData = getIntent();
        Reseps resep = (Reseps) getExtraData.getSerializableExtra(MenuAdapter.RESEPS_DATA);

        CircularProgressDrawable loading = new CircularProgressDrawable(getApplicationContext());
        loading.setStrokeWidth(5f);
        loading.setCenterRadius(30f);
        loading.start();

        Glide.with(getApplicationContext()).load(resep.getUrl_image()).placeholder(loading).into(imageViewMasakan);
        textViewBahan.setText(resep.getBahan());
        textViewNama.setText(resep.getNama_menu());
        textViewCaraMemasak.setText(resep.getCara_memasak());
        textViewDeskripsi.setText(resep.getDeskripsi());

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference path = database.collection("reseps").document(resep.getId_resep());

        if(!user.getUid().equals(resep.getAuthorID())){
            floatingActionButtonEdit.setVisibility(View.GONE);
            floatingActionButtonDelete.setVisibility(View.GONE);
        }

        floatingActionButtonEdit.setOnClickListener(v -> {
            Intent intentResep = new Intent(MenuDetailMasakan.this, NewMenu.class);
            intentResep.putExtra(MenuAdapter.RESEPS_DATA, resep);
            intentResep.putExtra(MenuAdapter.ACTION_STATE, MenuAdapter.ACTION_UPDATE);
            startActivity(intentResep);
        });

        floatingActionButtonDelete.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Yakin akan menghapus resep ini?");

            dialog.setMessage("Setelah anda menghapus item, kami tidak bisa mengembalikan data tersebut!").setCancelable(false)
                    .setPositiveButton("Lanjutkan", (dialog12, which) -> {
//                         delete data
                        path.delete().addOnCompleteListener(task -> {
                            Toast.makeText(getApplicationContext(), "Data with name " + resep.getNama_menu() + " deleted!", Toast.LENGTH_SHORT).show();
                            this.finish();
                        });
                    }).setNegativeButton("Batalkan", (dialog1, which) -> dialog1.cancel());

            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
        });
    }
}