package com.example.mk;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    Context context;
    List<Contact> models;

    private onClickListener onClickListener;

    public void setOnItemClickListener(onClickListener onClick){
        this.onClickListener = onClick;
    }

    public void setData(List<Contact> data){
        this.models = data;
        notifyDataSetChanged();
    }

    public ContactAdapter(List<Contact> data, Context ctx){
        this.context = ctx;
        setData(data);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.contact_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(models.get(position));
        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onClickListener.onClick(v, position,1);
            }
        });
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(v,position,2);
            }
        });
        String jk = models.get(position).getGender();
        if (jk.equals("Laki-laki")){
            int imageResource = context.getResources().getIdentifier(holder.URL_MALE, null, context.getPackageName());
            Drawable res = context.getResources().getDrawable(imageResource);
            holder.iv_jk.setImageDrawable(res);
        } else if (jk.equals("Perempuan")){
            int imageResource = context.getResources().getIdentifier(holder.URL_FEMALE, null, context.getPackageName());
            Drawable res = context.getResources().getDrawable(imageResource);
            holder.iv_jk.setImageDrawable(res);
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final String URL_MALE = "@drawable/ic_male";
        public final String URL_FEMALE = "@drawable/ic_female";
        TextView tv_name;
        TextView tv_number;
        ImageView iv_jk;
        ImageView iv_delete;
        ImageView iv_edit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_nama);
            tv_number = itemView.findViewById(R.id.tv_nomor);
            iv_delete = itemView.findViewById(R.id.iv_delete);
            iv_edit = itemView.findViewById(R.id.iv_edit);
            iv_jk = itemView.findViewById(R.id.iv_jk);
        }

        public void bind(Contact contact){
            tv_name.setText(contact.getName());
            tv_number.setText("0"+contact.getPhone_number());
        }
    }

    public interface onClickListener{
        public void onClick(View v, int position, int menu);
    }
}
