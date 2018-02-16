package com.example.georgealbert.queuemobileapplication;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class queue_list extends Fragment implements View.OnClickListener{

    private TextView label1, queueNumber, label2, label3, windowNumber, reservedQueue, waitingTime;
    private Button currentServed, extendTime, dropQueue;

    private WaitingTime waitingTimer = new WaitingTime();

    public queue_list() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_queue_list, container, false);

        label1 = (TextView)view.findViewById(R.id.textView1);
        queueNumber = (TextView)view.findViewById(R.id.textView2);
        label2 = (TextView)view.findViewById(R.id.terminal4_transaction);
        windowNumber = (TextView)view.findViewById(R.id.textView4);
        label3 = (TextView)view.findViewById(R.id.terminal7_transaction);
        waitingTime = (TextView)view.findViewById(R.id.textView6);
        reservedQueue = (TextView)view.findViewById(R.id.textView7);

        currentServed = (Button)view.findViewById(R.id.button1);
        extendTime = (Button)view.findViewById(R.id.button2);
        dropQueue = (Button)view.findViewById(R.id.button3);

        Typeface courNew = Typeface.createFromAsset(getActivity().getAssets(), "fonts/couriernew.ttf");
        Typeface stencil = Typeface.createFromAsset(getActivity().getAssets(), "fonts/stencil.ttf");

        queueNumber.setTypeface(stencil);
        windowNumber.setTypeface(stencil);
        waitingTime.setTypeface(stencil);

        dropQueue.setOnClickListener(this);
        extendTime.setOnClickListener(this);
        currentServed.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        if(v == dropQueue){

//            reset time and change another one;;;

        } else if (v == extendTime) {


        }else if(v == currentServed){

            final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
            LayoutInflater inflater = this.getActivity().getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.activity_currently_served, null);
            builder.setView(dialogView);

            final Button close = (Button) dialogView.findViewById(R.id.close);

            final AlertDialog dialog = builder.create();
            dialog.show();

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

    }

    public void startTimer(){
        waitingTimer.startWaitingTime(waitingTime, 2, 0);
    }
}

