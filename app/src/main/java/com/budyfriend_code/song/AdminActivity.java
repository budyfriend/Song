package com.budyfriend_code.song;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.budyfriend_code.song.adapter.AdapterItem;
import com.budyfriend_code.song.model.dataSong;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class AdminActivity extends AppCompatActivity {
    FloatingActionButton fab_add;
    RecyclerView recyclerView;
    Context context;
    ProgressDialog progressDialog;

    LayoutInflater layoutInflater;
    AlertDialog alertDialog;
    Bitmap bitmap;
    Uri uriFile;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    StorageReference storage = FirebaseStorage.getInstance().getReference();
    AdapterItem adapterItem;
    ArrayList<dataSong> dataSongArrayList= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        recyclerView = findViewById(R.id.recyclerView);
        fab_add = findViewById(R.id.fab_add);
        context = this;
        progressDialog = new ProgressDialog(context);
        layoutInflater = getLayoutInflater();

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputData();
            }
        });

        showData();
    }

    private void showData() {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        database.child("song").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataSongArrayList.clear();
                for (DataSnapshot item : snapshot.getChildren()){
                    dataSong song = item.getValue(dataSong.class);
                    if (song!=null){
                        song.setKey(item.getKey());
                        dataSongArrayList.add(song);
                    }
                }
                adapterItem = new AdapterItem(context,dataSongArrayList);
                recyclerView.setAdapter(adapterItem);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    ImageView iv_song;
    EditText et_title,
            et_description;
    Button find_image, btn_simpan;

    private void showInputData() {
        alertDialog = new AlertDialog.Builder(context).create();
        View view = layoutInflater.inflate(R.layout.input_song, null);
        alertDialog.setView(view);

        iv_song = view.findViewById(R.id.iv_song);
        et_title = view.findViewById(R.id.et_title);
        et_description = view.findViewById(R.id.et_description);
        find_image = view.findViewById(R.id.find_image);
        btn_simpan = view.findViewById(R.id.btn_simpan);

        alertDialog.show();

        find_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(context).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent, "Select Image"), 2);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = et_title.getText().toString();
                String description = et_description.getText().toString();
                if (bitmap == null) {
                    Toast.makeText(context, "Masukan gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
                } else if (title.isEmpty()) {
                    et_title.setError("Data tidak boleh kosong");
                    et_title.requestFocus();
                } else if (description.isEmpty()) {
                    et_description.setError("Data tidak boleh kosong");
                    et_description.requestFocus();
                } else {
                    String key = database.push().getKey();
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                    storage.child("song").child(key).putFile(uriFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storage.child("song").child(key).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    database.child("song").child(key).setValue(new dataSong(
                                            title,
                                            description,
                                            uri.toString(),
                                            0
                                    ));
                                    alertDialog.dismiss();
                                    progressDialog.dismiss();
                                    Toast.makeText(context, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    alertDialog.dismiss();
                                    progressDialog.dismiss();
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            alertDialog.dismiss();
                            progressDialog.dismiss();
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            float progress = 100.0f * snapshot.getBytesTransferred() / snapshot.getTotalByteCount();
                            progressDialog.setMessage("Upload... " + String.format("%.2f", progress) + " %");
                        }
                    });
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            try {
                uriFile = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriFile);
                iv_song.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}