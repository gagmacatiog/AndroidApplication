package com.example.georgealbert.queuemobileapplication;

/**
 * Created by George Albert on 12/15/2017.
 */

public class Notification {

    String notificationMessage;
    String notificationDate;
    String notificationID;

    public Notification(){
    }

    public Notification(String notificationID, String notificationMessage, String notificationDate) {
        this.notificationID = notificationID;
        this.notificationMessage = notificationMessage;
        this.notificationDate = notificationDate;
    }

    public String getNotif() { return notificationMessage; }

    public String getDate() { return notificationDate; }

//    public String getTime() { return notificationTime; }

    public String getID() { return notificationID; }
}
