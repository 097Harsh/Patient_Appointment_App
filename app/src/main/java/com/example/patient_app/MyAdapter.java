package com.example.patient_app;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<User> list;

    public MyAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.userentry,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = list.get(position);
        holder.name.setText(user.getName());
        holder.email.setText(user.getEmail());
        holder.contact.setText(user.getContact());
        holder.role.setText(user.getRole());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //Myviewholder class
    public  class  MyViewHolder extends  RecyclerView.ViewHolder{

        TextView name,email,contact,role;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textname);
            email = itemView.findViewById(R.id.textemail);
            contact = itemView.findViewById(R.id.textcontact);
            role = itemView.findViewById(R.id.textrole); // Adding role here
            Log.d("TAG", "onDataChange: "+name);
            Log.d("TAG", "onDataChange: "+email);
            Log.d("TAG", "onDataChange: "+contact);

        }
    }
}
