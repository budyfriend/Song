package com.budyfriend_code.song.adapter;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
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
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdapterItem extends RecyclerView.Adapter<AdapterItem.ItemViewHolder> {
    Context context;
    ArrayList<dataSong> dataSongArrayList;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    StorageReference storage = FirebaseStorage.getInstance().getReference();

    public AdapterItem(Context context, ArrayList<dataSong> dataSongArrayList) {
        this.context = context;
        this.dataSongArrayList = dataSongArrayList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.viewBind(dataSongArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSongArrayList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_song,
                iv_delete;
        TextView tv_title,
                tv_description,
                tv_raiting;
        Button btn_voting;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_song = itemView.findViewById(R.id.iv_song);
            iv_delete = itemView.findViewById(R.id.iv_delete);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_raiting = itemView.findViewById(R.id.tv_raiting);
            btn_voting = itemView.findViewById(R.id.btn_voting);
        }

        public void viewBind(dataSong dataSong) {

            iv_delete.setVisibility(View.VISIBLE);

            tv_title.setText("Title : "+dataSong.getTitle());
            tv_description.setText("Description : "+dataSong.getDescription());
            tv_raiting.setText("Raiting : "+dataSong.getRaiting());

            Glide.with(context).load(dataSong.getImg()).placeholder(android.R.drawable.ic_menu_report_image).into(iv_song);
            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog
                            .setMessage("Apakah kamu yakin ingin menghapus data ini?")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    storage.child("song").child(dataSong.getKey()).delete();
                                    database.child("song").child(dataSong.getKey()).removeValue();
                                    Toast.makeText(context, "Data berhasil di hapus", Toast.LENGTH_SHORT).show();
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
        }
    }
}
