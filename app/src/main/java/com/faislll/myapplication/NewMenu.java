package com.faislll.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.faislll.myapplication.adapter.MenuAdapter;
import com.faislll.myapplication.model.Reseps;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class NewMenu extends AppCompatActivity {
    private static final String TAG = "New Menu ____: ";
    private ImageView imagePreview;
    private Uri uriImage;
    private boolean isEmpty;
    private EditText editTextNamaMenu, editTextBahan, editTextCaraMemasak, editTextDeskripsi;
    private String urlImage;
    private String newImageName;
    private StorageReference resepsReferencesImage;


    private static final int STORAGE_PERMISSION_CODE = 101;
    private UploadTask uploadTask;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_menu);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Intent gotoLogin = new Intent(NewMenu.this, Login.class);
            startActivity(gotoLogin);
            this.finish();
        }


        ImageButton addImage = findViewById(R.id.add_image);
        imagePreview = findViewById(R.id.preview_image);
        FloatingActionButton buttonSimpan = findViewById(R.id.buttonSimpan);

        editTextBahan = findViewById(R.id.et_bahan);
        editTextNamaMenu = findViewById(R.id.et_nama_menu);
        editTextCaraMemasak = findViewById(R.id.et_cara_memasak);
        editTextDeskripsi = findViewById(R.id.et_deskripsi);

        Toolbar toolbar = findViewById(R.id.toolbar_resep_baru);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24));
        toolbar.setNavigationOnClickListener(v -> this.finish());


        /***
         * use local data resep.
         * */
        Reseps reseps = (Reseps) getIntent().getSerializableExtra(MenuAdapter.RESEPS_DATA);
        String actionStep = getIntent().getStringExtra(MenuAdapter.ACTION_STATE);
        CircularProgressDrawable loading = new CircularProgressDrawable(this);
        loading.setStrokeWidth(5f);
        loading.setCenterRadius(30f);
        loading.start();

        if (reseps != null){
            Glide.with(getApplicationContext()).load(reseps.getUrl_image()).placeholder(loading).into(imagePreview);
            editTextBahan.setText(reseps.getBahan());
            editTextCaraMemasak.setText(reseps.getCara_memasak());
            editTextDeskripsi.setText(reseps.getDeskripsi());
            editTextNamaMenu.setText(reseps.getNama_menu());
        }

        FirebaseStorage storage = FirebaseStorage.getInstance();

        addImage.setOnClickListener(view -> checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE));


        buttonSimpan.setOnClickListener(view -> {
            /*** start validate */
//            if (editTextBahan.getText().toString().equals("")){
//                editTextBahan.setError("field tidak boleh kosong!");
//                editTextBahan.requestFocus();
//            } else if(editTextCaraMemasak.getText().toString().equals("")){
//                editTextCaraMemasak.setError("field tidak boleh kosong!");
//                editTextCaraMemasak.requestFocus();
//            } else if(editTextNamaMenu.getText().toString().equals("")){
//                editTextNamaMenu.setError("field tidak boleh kosong!");
//                editTextNamaMenu.requestFocus();
//            } else {
                    try {

                        imagePreview.setDrawingCacheEnabled(true);
                        imagePreview.buildDrawingCache();

                        FirebaseFirestore database = FirebaseFirestore.getInstance();
                        String idResep = UUID.randomUUID().toString();
                        String namaMenu = editTextNamaMenu.getText().toString();
                        String caraMemasak = editTextCaraMemasak.getText().toString();
                        String bahan = editTextBahan.getText().toString();
                        String deskripsi = editTextDeskripsi.getText().toString();

                        assert user != null;
                        Reseps resep = new Reseps(
                                idResep,
                                namaMenu,
                                user.getDisplayName(),
                                caraMemasak,
                                bahan,
                                urlImage,
                                deskripsi,
                                user.getUid());

                        if (uriImage != null){
                            Toast.makeText(getApplicationContext(), "uriImage != null", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap = ((BitmapDrawable) imagePreview.getDrawable()).getBitmap();
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                            byte[] dataImage = byteArrayOutputStream.toByteArray();

                            StorageReference reference = storage.getReference();
                            newImageName = editTextNamaMenu.getText().toString().replace(" ", "-")+ UUID.randomUUID().toString() + ".png";

                            resepsReferencesImage = reference.child("reseps/" + newImageName);
                            UploadTask uploadTask = resepsReferencesImage.putBytes(dataImage);

                            uploadTask.addOnSuccessListener(taskSnapshot -> resepsReferencesImage.getDownloadUrl()
                                    .addOnSuccessListener(uri -> {
                                        if(actionStep.equals(MenuAdapter.ACTION_UPDATE)){
                                            Map<String, Object> dataUpdated = new HashMap<>();

                                            dataUpdated.put("cara_memasak", caraMemasak);
                                            dataUpdated.put("bahan", bahan);
                                            dataUpdated.put("deskripsi", deskripsi);
                                            dataUpdated.put("nama_menu", namaMenu);

                                            assert reseps != null;
                                            dataUpdated.put("url_image", uri.toString());

                                            database.collection("reseps").document(reseps.getId_resep())
                                                    .update(dataUpdated).addOnCompleteListener(task -> {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(getApplicationContext(), "Success update data...", Toast.LENGTH_SHORT).show();
                                                }
                                                this.finish();
                                            }).addOnFailureListener(e -> {
                                                Toast.makeText(getApplicationContext(), "message:  " + e.getMessage() + " message!", Toast.LENGTH_SHORT).show();
                                                this.finish();
                                            });


                                        } else {

                                            if(isEmpty){
                                                Toast.makeText(getApplicationContext(), "The image source is should'nt empty, please choose some image for continue the proses!", Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                        resep.setUrl_image(uri.toString());
                                        database.collection("reseps").document(idResep).set(resep)
                                                .addOnCompleteListener(task -> {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(getApplicationContext(), "Data save completed", Toast.LENGTH_SHORT).show();
                                                        this.finish();
                                                    }
                                                }).addOnFailureListener(e -> {
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                });
                                        }

                                    })).addOnFailureListener(e -> {
                                Toast.makeText(getApplicationContext(),"mesage: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                                this.finish();
                            });
                        } else {

                            if(actionStep.equals(MenuAdapter.ACTION_UPDATE)) {
                                Map<String, Object> dataUpdated = new HashMap<>();

                                dataUpdated.put("cara_memasak", caraMemasak);
                                dataUpdated.put("bahan", bahan);
                                dataUpdated.put("deskripsi", deskripsi);
                                dataUpdated.put("nama_menu", namaMenu);

                                assert reseps != null;
                                dataUpdated.put("url_image", reseps.getUrl_image());

                                database.collection("reseps").document(reseps.getId_resep())
                                        .update(dataUpdated).addOnCompleteListener(task -> {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(), "Success update data...", Toast.LENGTH_SHORT).show();
                                    }
                                    this.finish();
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(getApplicationContext(), "message:  " + e.getMessage() + " message!", Toast.LENGTH_SHORT).show();
                                    this.finish();
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "The image source is should'nt empty, please choose some image for continue the proses!", Toast.LENGTH_SHORT).show();

                            }
                        }

                    } catch (Exception error){
                        Toast.makeText(getApplicationContext(),"message: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                        this.finish();
                    }
                /*** end validate */

//            }
        });
    }

    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(NewMenu.this, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(NewMenu.this, new String[] { permission }, requestCode);
        } else {
            startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), STORAGE_PERMISSION_CODE);
            } else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }

    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null && data.getData() != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            uriImage = getPickImageResultUri(data);
            Log.i(TAG, "onActivityResult: ______" + uriImage);
            imagePreview.setImageURI(uriImage);
            isEmpty = false;
        }

//        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            Log.i(TAG, "onActivityResult: " + String.valueOf(resultCode));
//
//            if(resultCode == RESULT_OK){
//                uriImage = getPickImageResultUri(data);
//                Log.i(TAG, "onActivityResult: " + uriImage);
//
//
//                imagePreview.setImageURI(uriImage);
//            } else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Log.d(TAG, "onActivityResult: DD___ Masuk Else");
//
//                Exception error = result.getError();
//                Toast.makeText(this, String.valueOf(error), Toast.LENGTH_SHORT).show();
//
//            }
//        }
    }
}