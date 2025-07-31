package com.example.demo.model;

public class Demo {
    private Long id;
    private String message;

    public Demo(Long id, String message) {
        this.id = id;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Demo{" +
                "id=" + id +
                ", message='" + message + '\'' +
                '}';
    }
}