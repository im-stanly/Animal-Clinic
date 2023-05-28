package com.Clinic.WebApp.model;

import lombok.Data;

import java.util.Date;

@Data
public class FunfactModel {
    @Data
    public static class Status{
        private boolean verified;
        private String feedback;
        private int sentCount;
    }
    private Status status;
    private String _id;
    private String user;
    private String text;
    private int __v;
    private String source;
    private Date updatedAt;
    private String type;
    private Date createdAt;
    private boolean deleted;
    private boolean used;
}

