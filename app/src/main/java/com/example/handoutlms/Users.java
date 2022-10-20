package com.example.handoutlms;

public class Users {
    public String id, fullname, email, institution;

    public Users(){

    }

    public Users(String id, String fullname, String email, String institution){
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.institution = institution;
    }

    //Getters
    public String getId() {return id;}
    public String getFullname() {return fullname;}
    public String getEmail() {return email;}
    public String getInstitution() {return institution;}

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
}
