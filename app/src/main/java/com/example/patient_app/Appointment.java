package com.example.patient_app;

public class Appointment {
    private String appointmentId;
    private String selectedTimeSlot;
    private String status;
    private String patientName;
    private String user_id; // assuming user_id is a String
    private String message;
    private String doctorName;
    private String appointmentDate;

    // Required default constructor
    public Appointment() {
        // Default constructor required for Firebase
    }

    public Appointment(String appointmentId, String status, String patientName, String user_id, String message, String doctorName, String appointmentDate, String selectedTimeSlot) {
        this.appointmentId = appointmentId;
        this.status = status;
        this.patientName = patientName;
        this.user_id = user_id;
        this.message = message;
        this.doctorName = doctorName;
        this.appointmentDate = appointmentDate;
        this.selectedTimeSlot = selectedTimeSlot;
    }



    // Getter methods
    public String getAppointmentId() {
        return appointmentId;
    }

    public String getStatus() {
        return status;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getuser_id() {
        return user_id;
    }

    public String getMessage() {
        return message;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public String getTimeSlot() {
        return selectedTimeSlot;
    }

    // Setter methods
    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setTimeSlot(String selectedTimeSlot) {
        this.selectedTimeSlot = selectedTimeSlot;
    }
}
