package com.example.acn4bv_andreani_parcial_2;

public class Task {
    private int id;
    private String title;
    private String description;
    private String time;
    private String date;
    private int isDone;

    public Task(int id, String title, String description, String time, String date, int isDone) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.time = time;
        this.date = date;
        this.isDone = isDone;
    }

    // constructor para crear una nueva tarea sin ID
    public Task(String title, String description, String time, String date) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.date = date;
        this.isDone = 0;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public int isDone() { return isDone; } // Este es el método que usa DatabaseHelper
    public void setDone(int done) { isDone = done; }

    @Override
    public String toString() {
        return title + " (" + time + ")";
    }
}