package com.example.georgealbert.queuemobileapplication;

import android.app.Application;

/**
 * Created by George Albert on 12/13/2017.
 */

public class ApplicationClass extends Application {
    public queue_list list1 = new queue_list();
    public logs_list list2 = new logs_list();
    public notification_list list3 = new notification_list();
    public ApplicationInfo list4 = new ApplicationInfo();
}
