package com.example.georgealbert.queuemobileapplication;

import android.app.Activity;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;

public class Logs_Activity extends ArrayAdapter<Logs> {
    private Activity context;
    private List<Logs> loglist;

    public Logs_Activity(Activity context, List<Logs> loglist) {
        super(context, R.layout.activity_logs_, loglist);
        this.context = context;
        this.loglist = loglist;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the layout for this fragment

        LayoutInflater inflater = context.getLayoutInflater();
        View logsViewItem = inflater.inflate(R.layout.activity_logs_, null, true);

        int height = getScreenHeight();
        int width = getScreenWidth();
        height = height / 5;
        width = width / 5;

        // Modifying Text View # 1
        TextView textViewDate = (TextView) logsViewItem.findViewById(R.id.textView1);


        // Modifying Text View # 2
        TextView textViewTerminal = (TextView) logsViewItem.findViewById(R.id.textView2);


        // Modifying Text View # 3
        TextView textViewDetails = (TextView) logsViewItem.findViewById(R.id.terminal4_transaction);


        // Modifying Text View # 4
        TextView textViewStatus = (TextView) logsViewItem.findViewById(R.id.textView4);

        // Modifying Logs Container
        CardView logContainer = (CardView) logsViewItem.findViewById(R.id.logs_container);

        Logs logs = loglist.get(position);

        textViewDate.setText(logs.getLogDate());
        textViewTerminal.setText(logs.getLogTerminal());
        textViewDetails.setText(logs.getLogDetails());
        textViewStatus.setText(logs.getLogStatus());

        if(logs.getLogStatus().equals("Done")){
//            logContainer.setBackgroundColor(logsViewItem.getResources().getColor(R.color.doneColor));
            logContainer.setCardBackgroundColor(logsViewItem.getResources().getColor(R.color.doneColor));
        }else{
//            logContainer.setBackgroundColor(logsViewItem.getResources().getColor(R.color.cancelColor));
            logContainer.setCardBackgroundColor(logsViewItem.getResources().getColor(R.color.cancelColor));
        }

        return logsViewItem;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


}
