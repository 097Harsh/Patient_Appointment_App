package com.example.patient_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyUserAppoinmentAdapet extends RecyclerView.Adapter<MyUserAppoinmentAdapet.MyViewHolder>{

    Context context;
    ArrayList<Appointment> list;
    DatabaseReference usersRef; // Reference to the Firebase users table

    // Constructor
    public MyUserAppoinmentAdapet(Context context, ArrayList<Appointment> list) {
        this.context = context;
        this.list = list;
        // Initialize the Firebase database reference for users
        usersRef = FirebaseDatabase.getInstance().getReference().child("appoinments");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_appoinment_entry, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Appointment appointment = list.get(position);
        holder.appoinmentId.setText(appointment.getAppointmentId());;
        holder.patientName.setText(appointment.getPatientName());
        holder.timeSlot.setText(appointment.getTimeSlot());
        holder.doctorName.setText(appointment.getDoctorName());
        holder.status.setText(appointment.getStatus());
        holder.appoinmentDate.setText(String.valueOf(appointment.getAppointmentDate())); // Assuming you have a rating TextView

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // ViewHolder class
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView appoinmentId,patientName,timeSlot,doctorName,status,appoinmentDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            appoinmentId = itemView.findViewById(R.id.textid);
            patientName = itemView.findViewById(R.id.textname);
            timeSlot = itemView.findViewById(R.id.time);
            doctorName = itemView.findViewById(R.id.textdoctorname);
            status = itemView.findViewById(R.id.status);
            appoinmentDate = itemView.findViewById(R.id.textdate);
        }
    }
}
