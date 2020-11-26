package com.budyfriend_code.song;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.budyfriend_code.song.adapter.AdapterVote;
import com.budyfriend_code.song.model.dataSong;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VoteActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Context context;
    ArrayList<dataSong> dataSongArrayList =new ArrayList<>();
    AdapterVote adapterVote;
    ProgressDialog progressDialog;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        recyclerView = findViewById(R.id.recyclerView);
        context = this;
        progressDialog = new ProgressDialog(context);

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
                    if (song!= null){
                        song.setKey(item.getKey());
                        dataSongArrayList.add(song);
                    }
                }
                adapterVote = new AdapterVote(context,dataSongArrayList);
                recyclerView.setAdapter(adapterVote);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}