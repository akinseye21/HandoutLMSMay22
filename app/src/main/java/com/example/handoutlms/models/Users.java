package com.example.handoutlms.models;

public class Users {
    public String id, fullname, email, institution, status;

    public Users(){

    }

    public Users(String id, String fullname, String email, String institution, String status){
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.institution = institution;
        this.status = status;
    }

    //Getters
    public String getId() {return id;}
    public String getFullname() {return fullname;}
    public String getEmail() {return email;}
    public String getInstitution() {return institution;}
    public String getStatus() {return status;}

    //Setters
    public void setId(String id){
        this.id = id;
    }
    public void setFullname(String fullname){
        this.fullname = fullname;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setInstitution(String institution){
        this.institution = institution;
    }
    public void setStatus(String status){
        this.status = status;
    }

}
