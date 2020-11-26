package com.budyfriend_code.song;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.budyfriend_code.song.model.dataLogin;
import com.budyfriend_code.song.model.dataUser;
import com.budyfriend_code.song.session.preferences;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {
    EditText txt_username,
            txt_no_hp,
            txt_alamat,
            txt_password,
            txt_konfirmasi,
            et_tanggal;
    Context context;
    RadioGroup rb_group;
    RadioButton rb_button;
    Button btn_Daftar, btn_tanggal_lahir;
    TextView txt_sudah_punya;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    Locale id = new Locale("in", "ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-YYYY", id);
    Date date = new Date();
    Date date2 = new Date();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        context = this;
        progressDialog = new ProgressDialog(context);
        txt_username = findViewById(R.id.username);
        txt_sudah_punya = findViewById(R.id.sudah_punya);
        txt_no_hp = findViewById(R.id.no_hp);
        txt_alamat = findViewById(R.id.alamat);
        txt_password = findViewById(R.id.password);
        txt_konfirmasi = findViewById(R.id.konfirmasi);
        rb_group = findViewById(R.id.rb_group);
        btn_Daftar = findViewById(R.id.btn_Daftar);
        et_tanggal = findViewById(R.id.et_tanggal);
        btn_tanggal_lahir = findViewById(R.id.btn_tanggal_lahir);

        btn_tanggal_lahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        et_tanggal.setText(simpleDateFormat.format(calendar.getTime()));
                        date = calendar.getTime();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().getTouchables().get(0).performClick();
                datePickerDialog.show();
            }
        });
        txt_sudah_punya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, LoginActivity.class));
            }
        });

        btn_Daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = txt_username.getText().toString();
                final String no_hp = txt_no_hp.getText().toString();
                final String alamat = txt_alamat.getText().toString();
                final String password = txt_password.getText().toString();
                String konfirmasi = txt_konfirmasi.getText().toString();

                if (username.isEmpty()) {
                    txt_username.setError("Data tidak boleh kosong");
                    txt_username.requestFocus();
                } else if (no_hp.isEmpty()) {
                    txt_no_hp.setError("Data tidak boleh kosong");
                    txt_no_hp.requestFocus();
                } else if (alamat.isEmpty()) {
                    txt_alamat.setError("Data tidak boleh kosong");
                    txt_alamat.requestFocus();
                } else if (password.isEmpty()) {
                    txt_password.setError("Data tidak boleh kosong");
                    txt_password.requestFocus();
                } else if (konfirmasi.isEmpty()) {
                    txt_konfirmasi.setError("Data tidak boleh kosong");
                    txt_konfirmasi.requestFocus();
                } else if (!konfirmasi.equals(password)) {
                    txt_konfirmasi.setError("Konfirmasi password salah");
                    txt_konfirmasi.requestFocus();
                } else {
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    database.child("user").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Toast.makeText(context, "Username sudah digunakan!", Toast.LENGTH_SHORT).show();
                            } else {
                                int selectRadio = rb_group.getCheckedRadioButtonId();
                                rb_button = findViewById(selectRadio);


                                database.child("user").child(username).setValue(new dataUser(
                                        username,
                                        rb_button.getText().toString(),
                                        no_hp,
                                        alamat,
                                        simpleDateFormat.format(date.getTime()),
                                        simpleDateFormat.format(date2.getTime()),
                                        false
                                )).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        database.child("login").child(username).setValue(new dataLogin(username, password, "user"));
                                        Toast.makeText(context, "Data berhasil terdaftar", Toast.LENGTH_SHORT).show();
                                        preferences.setActive(context, true);
                                        preferences.setLevel(context, "user");
                                        preferences.setId(context, username);
                                        preferences.setUsername(context, username);
                                        progressDialog.dismiss();
                                        startActivity(new Intent(context, VoteActivity.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "Data gaga terdaftar", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

    }
}