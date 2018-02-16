package com.example.georgealbert.queuemobileapplication;

import android.app.Activity;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Notification_Activity extends ArrayAdapter<Notification> {
    private Activity context;
    private List<Notification> notificationlist;
    private TextView textView;

    public Notification_Activity(Activity context, List<Notification> notificationlist) {
        super(context, R.layout.activity_notification_, notificationlist);
        this.context = context;
        this.notificationlist = notificationlist;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the layout for this fragment

        LayoutInflater inflater = context.getLayoutInflater();
        View notificationViewItem = inflater.inflate(R.layout.activity_notification_, null, true);

        int height = getScreenHeight();
        int width = getScreenWidth();

        // Modifying Text View # 1
        TextView textViewDate = (TextView) notificationViewItem.findViewById(R.id.textView1);
        textView = (TextView) notificationViewItem.findViewById(R.id.textView2);

        //Modifying Notification Image
        ImageView imageview = (ImageView) notificationViewItem.findViewById(R.id.notif_image);

        Notification notif = notificationlist.get(position);

        if (notif.getNotif().equals("5 minutes remaining")) {
            imageview.setImageResource(R.drawable.five_mins_timer);
            textViewDate.setText("Estimated serving time: \n" + notif.getNotif());
        } else if (notif.getNotif().equals("3 people away")) {
            imageview.setImageResource(R.drawable.fiveaway);
            textViewDate.setText("Queue position update: \n" + notif.getNotif());
        }

        // Hours / minutes ago of the notification
        this.getTimeParse(notif.getDate());

        return notificationViewItem;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public void getTimeParse(String date) {

        String Date = new SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.getDefault()).format(new Date());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");

        try {

            Date date1 = simpleDateFormat.parse(date);
            Date date2 = simpleDateFormat.parse(Date);

            printDifference(date2, date1);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void printDifference(Date startDate, Date endDate){

        //milliseconds
        long different = startDate.getTime() - endDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;


        // Setting the value of time lapse textview...

        String addInfo;

        if(elapsedDays != 0){

            if(elapsedDays < 2){
                addInfo = " day ago";
            }else{
                addInfo = " days ago";
            }
            textView.setText(Long.toString(elapsedDays) + addInfo);

        }else if(elapsedHours != 0){

            if(elapsedHours < 2){
                addInfo = " hour ago";
            }else{
                addInfo = " hours ago";
            }
            textView.setText(Long.toString(elapsedHours) + addInfo);

        }else if(elapsedMinutes != 0){

            if(elapsedMinutes < 2){
                addInfo = " minute ago";
            }else{
                addInfo = " minutes ago";
            }
            textView.setText(Long.toString(elapsedMinutes) + addInfo);

        }else{

            if(elapsedSeconds < 2){
                addInfo = " second ago";
            }else{
                addInfo = " seconds ago";
            }
            textView.setText(Long.toString(elapsedSeconds) + addInfo);

        }

    }

}
