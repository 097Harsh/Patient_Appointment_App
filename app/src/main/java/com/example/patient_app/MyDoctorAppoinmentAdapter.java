package com.example.patient_app;

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

public class MyDoctorAppoinmentAdapter extends RecyclerView.Adapter<MyDoctorAppoinmentAdapter.MyViewHolder> {

    private static final String TAG = "MyDoctorAppoinmentAdapter";
    private Context context;
    private ArrayList<Appointment> list;
    private DatabaseReference databaseReference;

    public MyDoctorAppoinmentAdapter(Context context, ArrayList<Appointment> list) {
        this.context = context;
        this.list = list;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("appointments");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_appoinment_entry, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Appointment appointment = list.get(position);
        holder.msg.setText(appointment.getMessage());
        holder.name.setText(appointment.getPatientName());
        holder.date.setText(appointment.getAppointmentDate());
        holder.status.setText(String.valueOf(appointment.getStatus()));
        holder.time.setText(String.valueOf(appointment.getTimeSlot()));
        holder.setButtonVisibility(appointment.getStatus());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, msg, status, date, time;
        Button acceptButton, rejectButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textname);
            msg = itemView.findViewById(R.id.textmsg);
            status = itemView.findViewById(R.id.apt_status);
            date = itemView.findViewById(R.id.apt_date);
            time = itemView.findViewById(R.id.apt_time);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);

            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateStatus("Accepted", getAdapterPosition());
                }
            });

            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateStatus("Rejected", getAdapterPosition());
                }
            });
        }

        private void updateStatus(String newStatus, int position) {
            list.get(position).setStatus(newStatus);
            notifyItemChanged(position);

            String appointmentId = list.get(position).getAppointmentId();

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

        public void setButtonVisibility(String appointmentStatus) {
            if (appointmentStatus.equals("Pending")) {
                acceptButton.setVisibility(View.VISIBLE);
                rejectButton.setVisibility(View.VISIBLE);
            } else {
                acceptButton.setVisibility(View.GONE);
                rejectButton.setVisibility(View.GONE);
            }
        }
    }
}