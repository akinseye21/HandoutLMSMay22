package com.example.handoutlms;

public class Chat {

    public String sender, receiver, message;

    public Chat(){

    }

    public Chat(String sender, String receiver, String message){
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    //Getters
    public String getSender() {return sender;}
    public String getReceiver() {return receiver;}
    public String getMessage() {return message;}

    //Setters
    public void setSender(String sender){
        this.sender = sender;
    }
    public void setReceiver(String receiver){
        this.receiver = receiver;
    }
    public void setMessage(String message){
        this.message = message;
    }

}
