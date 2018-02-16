package com.example.georgealbert.queuemobileapplication;

/**
 * Created by George Albert on 12/15/2017.
 */

public class Logs {

    String logDate;
    String logTerminal;
    String logDetails;
    String logStatus;

    public Logs(){
        // Do not put anything here..
    }

    public Logs(String logDate, String logTerminal, String logDetails, String logStatus) {
        this.logDate = logDate;
        this.logTerminal = logTerminal;
        this.logDetails = logDetails;
        this.logStatus = logStatus;
    }


    public String getLogDate() {
        return logDate;
    }

    public String getLogTerminal() {
        return logTerminal;
    }

    public String getLogDetails() {
        return logDetails;
    }

    public String getLogStatus() {
        return logStatus;
    }
}
