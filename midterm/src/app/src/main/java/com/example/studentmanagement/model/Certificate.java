package com.example.studentmanagement.model;

import java.util.Date;

public class Certificate {
    private String id;
    private String subject;
    private Date completionDate;
    private String organization;

    public Certificate() {

    }

    public Certificate(String subject, Date completionDate, String organization) {
        this.subject = subject;
        this.completionDate = completionDate;
        this.organization = organization;
    }

    public Certificate(String id, String subject, Date completionDate, String organization) {
        this.id = id;
        this.subject = subject;
        this.completionDate = completionDate;
        this.organization = organization;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }
}
