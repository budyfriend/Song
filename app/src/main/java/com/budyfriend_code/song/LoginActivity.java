package com.budyfriend_code.song;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.budyfriend_code.song.model.dataLogin;
import com.budyfriend_code.song.session.preferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText txt_username,
            txt_password;
    Button btnLogin;
    TextView pendaftaran;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    Context context;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        progressDialog = new ProgressDialog(context);
        getSupportActionBar().hide();

        txt_username = findViewById(R.id.txt_username);
        txt_password = findViewById(R.id.txt_password);
        btnLogin = findViewById(R.id.btnLogin);
        pendaftaran = findViewById(R.id.pendaftaran);

        pendaftaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,SignUpActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txt_username.getText().toString();
                String password = txt_password.getText().toString();
                if (username.isEmpty()) {
                    txt_username.setError("Data tidak boleh kosong");
                    txt_username.requestFocus();
                } else if (password.isEmpty()) {
                    txt_password.setError("Data tidak boleh kosong");
                    txt_password.requestFocus();
                }else {
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    database.child("login").child(username).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                dataLogin dataLogin = snapshot.getValue(dataLogin.class);
                                assert dataLogin != null;
                                dataLogin.setKey(snapshot.getKey());
                                if (dataLogin.getPassword().equals(password)) {
                                    preferences.setActive(context, true);
                                    preferences.setId(context, dataLogin.getKey());
                                    preferences.setUsername(context, dataLogin.getUsername());

                                    if (dataLogin.getLevel().equals("admin")) {
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "Selamat datang admin", Toast.LENGTH_SHORT).show();
                                        preferences.setLevel(context, "admin");
                                        progressDialog.dismiss();
                                        startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                                        finish();
                                    } else if (dataLogin.getLevel().equals("user")) {
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "Selamat datang user", Toast.LENGTH_SHORT).show();
                                        preferences.setLevel(context, "user");
                                        progressDialog.dismiss();
                                        startActivity(new Intent(LoginActivity.this, VoteActivity.class));
                                        finish();
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(context, "Password Salah", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Username belum terdaftar", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (preferences.getActive(context)) {
            switch (preferences.getLevel(context)) {
                case "admin":
                    startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                    finish();
                    break;
                case "user":
                    startActivity(new Intent(LoginActivity.this, VoteActivity.class));
                    finish();
                    break;
            }
        }
    }
}