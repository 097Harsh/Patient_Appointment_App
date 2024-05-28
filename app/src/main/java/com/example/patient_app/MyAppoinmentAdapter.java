package com.example.patient_app;

import static androidx.constraintlayout.widget.Constraints.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyAppoinmentAdapter extends RecyclerView.Adapter<MyAppoinmentAdapter.MyViewHolder> {

    Context context;
    ArrayList<Appointment> list; // Change ArrayList type to Appointment

    public MyAppoinmentAdapter(Context context, ArrayList<Appointment> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyAppoinmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.appoinment_entry, parent, false);
        return new MyAppoinmentAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAppoinmentAdapter.MyViewHolder holder, int position) {
        Appointment appointment = list.get(position);
        holder.msg.setText(appointment.getMessage());
        holder.name.setText(appointment.getPatientName());
        holder.date.setText(appointment.getAppointmentDate());
        holder.doctorName.setText(appointment.getDoctorName());
        holder.status.setText(appointment.getStatus()); // Assuming status is a String
        holder.timeSlot.setText(appointment.getTimeSlot()); // Set time slot in the TextView
        // No need to fetch user name here, as it's already being handled in the Activity/Fragment
        holder.setButtonVisibility(appointment.getStatus());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // MyViewHolder class
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, msg, status, date,doctorName,timeSlot;
        Button acceptButton, rejectButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textname);
            msg = itemView.findViewById(R.id.textmsg);
            status = itemView.findViewById(R.id.textstatus);
            date = itemView.findViewById(R.id.textdate);
            doctorName =  itemView.findViewById(R.id.textdoctor);
            timeSlot = itemView.findViewById(R.id.texttime);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
            //status update operation's
            // Set click listeners for accept and reject buttons
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Update status to "Accepted"
                    updateStatus("Accepted", getAdapterPosition());
                }
            });

            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Update status to "Rejected"
                    updateStatus("Rejected", getAdapterPosition());
                }
            });
        }
        // Method to update status
        private void updateStatus(String newStatus, int position) {
            // Update status in the appointment object
            list.get(position).setStatus(newStatus);
            // Notify adapter of the change
            notifyItemChanged(position);
            // Get the appointment ID of the updated appointment
            String appointmentId = list.get(position).getAppointmentId();

            // Update status in the Firebase Realtime Database
            DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference("appointments");
            Query query = appointmentsRef.orderByChild("appointmentId").equalTo(appointmentId);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        snapshot.getRef().child("status").setValue(newStatus);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, "Error updating status: " + databaseError.getMessage());
                }
            });
        }
        // Method to set visibility of accept and reject buttons based on status
        public void setButtonVisibility(String appointmentStatus) {
            if (appointmentStatus.equals("Pending")) {
                // Show both accept and reject buttons
                acceptButton.setVisibility(View.VISIBLE);
                rejectButton.setVisibility(View.VISIBLE);
            } else {
                // Hide both accept and reject buttons
                acceptButton.setVisibility(View.GONE);
                rejectButton.setVisibility(View.GONE);
            }
        }
    }
}
