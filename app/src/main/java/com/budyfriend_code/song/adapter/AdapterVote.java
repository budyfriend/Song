package com.budyfriend_code.song.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.budyfriend_code.song.R;
import com.budyfriend_code.song.model.dataSong;
import com.budyfriend_code.song.model.dataUser;
import com.budyfriend_code.song.session.preferences;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterVote extends RecyclerView.Adapter<AdapterVote.VoteViewHolder> {
    Context context;
    ArrayList<dataSong> dataSongArrayList;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public AdapterVote(Context context, ArrayList<dataSong> dataSongArrayList) {
        this.context = context;
        this.dataSongArrayList = dataSongArrayList;
    }

    @NonNull
    @Override
    public VoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
        return new VoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VoteViewHolder holder, int position) {
        holder.viewBind(dataSongArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSongArrayList.size();
    }

    public class VoteViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_song,
                iv_delete;
        TextView tv_title,
                tv_description,
                tv_raiting;
        Button btn_voting;

        public VoteViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_song = itemView.findViewById(R.id.iv_song);
            iv_delete = itemView.findViewById(R.id.iv_delete);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_raiting = itemView.findViewById(R.id.tv_raiting);
            btn_voting = itemView.findViewById(R.id.btn_voting);
        }

        public void viewBind(dataSong dataSong) {
            database.child("user").child(preferences.getUsername(context)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dataUser user = snapshot.getValue(dataUser.class);
                    if (user.isVote()) {
                        btn_voting.setVisibility(View.GONE);
                    } else {
                        btn_voting.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            btn_voting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setMessage("Apa kamu yakin ingin memilih ini?")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    long count = dataSong.getRaiting() + 1;
                                    database.child("song").child(dataSong.getKey()).child("raiting").setValue(count);
                                    database.child("user").child(preferences.getUsername(context)).child("vote").setValue(true);
                                    Toast.makeText(context,"Berhasil di pilih",Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            });

            tv_title.setText("Title : " + dataSong.getTitle());
            tv_description.setText("Description : " + dataSong.getDescription());
            tv_raiting.setText("Raiting : " + dataSong.getRaiting());

            Glide.with(context).load(dataSong.getImg()).placeholder(android.R.drawable.ic_menu_report_image).into(iv_song);
        }
    }
}
