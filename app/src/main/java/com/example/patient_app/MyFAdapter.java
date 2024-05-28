package com.example.patient_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyFAdapter extends RecyclerView.Adapter<MyFAdapter.MyViewHolder> {

    Context context;
    ArrayList<Feedback> list; // Change ArrayList type to Feedback

    DatabaseReference usersRef; // Reference to the Firebase users table

    public MyFAdapter(Context context, ArrayList<Feedback> list) {
        this.context = context;
        this.list = list;
        // Initialize the Firebase database reference for users
        usersRef = FirebaseDatabase.getInstance().getReference().child("feedback");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.feedbackentry, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Feedback feedback = list.get(position);
        holder.msg.setText(feedback.getMsg());;
        holder.name.setText(feedback.getName());
        holder.date.setText(feedback.getF_date());
        holder.rating.setText(String.valueOf(feedback.getRating())); // Assuming you have a rating TextView

        String userId = feedback.getUser_id();

        // Fetch name associated with the user ID from Firebase

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // MyViewHolder class
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, msg, rating, date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textname);
            msg = itemView.findViewById(R.id.textmsg);
            rating = itemView.findViewById(R.id.textrating);
            date = itemView.findViewById(R.id.textdate); // Assuming you have a rating TextView
        }
    }
}
