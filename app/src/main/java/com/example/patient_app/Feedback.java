package com.example.patient_app;

public class Feedback {
    private String f_id; // Feedback ID
    private String user_id; // User ID
    private String name;
    private String email;
    private String msg; // Changed from 'message' to 'msg'
    private float rating;
    private String f_date; // Date field, changed from 'date' to 'f_date'

    public Feedback() {
        // Default constructor required for Firebase
    }

    public Feedback(String f_id, String user_id, String name, String email, String msg, float rating, String f_date) {
        this.f_id = f_id;
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.msg = msg;
        this.rating = rating;
        this.f_date = f_date;
    }

    public String getF_id() {
        return f_id;
    }

    public void setF_id(String f_id) {
        this.f_id = f_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getRating() {
        return (int) rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getF_date() {
        return f_date;
    }

    public void setF_date(String f_date) {
        this.f_date = f_date;
    }
}
