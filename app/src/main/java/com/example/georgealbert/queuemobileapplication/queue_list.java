package com.example.georgealbert.queuemobileapplication;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.transition.Visibility;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ViewStubCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class queue_list extends Fragment implements View.OnClickListener, FragmentCommunicator{

    private TextView queueNumber, windowNumber, beforeYou, terminal, name, id;
    public TextView waitingTime;
    private Button dropQueue;
    String fullName;

    final private boolean checker[] = new boolean[1];
    public CountDownTimer cdt;

    public ActivityCommunicator activityCommunicator;

    ImageView extendTime, refresh, seeAll;
    DatabaseReference queue_status, userProfile, servicingTerminal, queue_info, queue_active;
    String currentUser, userFullName;
    final Map<String, String> user_queue_info = new HashMap<>();

    FirebaseAuth firebaseAuth;

    private WaitingTime waitingTimer = new WaitingTime();

    public queue_list() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_queue_list, container, false);

        checker[0] = false;
        queueNumber = (TextView)view.findViewById(R.id.queueNumber);
//        windowNumber = (TextView)view.findViewById(R.id.textView4);
        waitingTime = (TextView)view.findViewById(R.id.waitingTime);

        terminal = (TextView) view.findViewById(R.id.terminalTransaction);
        beforeYou = (TextView) view.findViewById(R.id.beforeYou);

        name = (TextView) view.findViewById(R.id.fullName);
        id = (TextView) view.findViewById(R.id.idNumber);

        dropQueue = (Button)view.findViewById(R.id.button3);
        extendTime = (ImageView) view.findViewById(R.id.button2);
        seeAll = (ImageView) view.findViewById(R.id.button1);
        refresh = (ImageView) view.findViewById(R.id.button4);

        dropQueue.setOnClickListener(this);
        extendTime.setOnClickListener(this);
        seeAll.setOnClickListener(this);
        refresh.setOnClickListener(this);


        firebaseAuth = FirebaseAuth.getInstance();

        queue_status = FirebaseDatabase.getInstance().getReference("Main_Queue");
        currentUser = firebaseAuth.getCurrentUser().getEmail().split("@")[0];
        id.setText(currentUser);

        servicingTerminal = FirebaseDatabase.getInstance().getReference("Servicing_Terminal");
        userProfile = FirebaseDatabase.getInstance().getReference("Accounts/"+currentUser+"/Profile");
        queue_info = FirebaseDatabase.getInstance().getReference("Queue_Info");
        queue_active = FirebaseDatabase.getInstance().getReference("Queue_Status");


        queue_status.keepSynced(true);
        userProfile.keepSynced(true);

        ((Main2Activity)getActivity()).setFragmentRefreshListener(new Main2Activity.FragmentRefreshListener() {
            @Override
            public void onRefresh() {

                startWaitingTime(30);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        queue_status.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot notifSnapshot: dataSnapshot.getChildren()){

                    if(notifSnapshot.child("Student_No").getValue().toString() == currentUser){
                        queueNumber.setText(notifSnapshot.child("Customer_Queue_Number").getValue().toString());
                        user_queue_info.put("Queue_Number", notifSnapshot.child("Customer_Queue_Number").getValue().toString());
                        user_queue_info.put("Servicing_Office", notifSnapshot.child("Servicing_Office").getValue().toString());
                        continue;
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fullName = dataSnapshot.child("firstName").getValue().toString() + " " +
                        dataSnapshot.child("middleName").getValue().toString().substring(0,1) + ". " +
                        dataSnapshot.child("lastName").getValue().toString();
                name.setText(fullName);
                passFullName();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        checkingActiveQueue();

    }

    @Override
    public void onClick(View v) {

        if(v == dropQueue){

//            reset time and change another one;;;

        } else if (v == extendTime) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
            LayoutInflater inflater = this.getActivity().getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.activity_move_queue, null);
            builder.setView(dialogView);

            final NumberPicker numberPicker = (NumberPicker) dialogView.findViewById(R.id.numberPicker);
            final Button cancel = (Button) dialogView.findViewById(R.id.cancel);
            final Button proceed = (Button) dialogView.findViewById(R.id.proceed);

            numberPicker.setMinValue(5);
            numberPicker.setMaxValue(20);
            numberPicker.setValue(13);
            numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            numberPicker.setWrapSelectorWheel(false);

            final AlertDialog dialog = builder.create();
            dialog.show();


            proceed.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Log.e("NumberPicker: ", ""+numberPicker.getValue());
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        }else if(v == seeAll){

            final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
            LayoutInflater inflater = this.getActivity().getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.activity_currently_served, null);
            builder.setView(dialogView);

            final Button close = (Button) dialogView.findViewById(R.id.close);

            final Map<Integer, ImageView> term_image = new HashMap<>();
            term_image.put(1, (ImageView) dialogView.findViewById(R.id.terminal1_image));
            term_image.put(2, (ImageView) dialogView.findViewById(R.id.terminal2_image));
            term_image.put(3, (ImageView) dialogView.findViewById(R.id.terminal3_image));
            term_image.put(4, (ImageView) dialogView.findViewById(R.id.terminal4_image));
            term_image.put(5, (ImageView) dialogView.findViewById(R.id.terminal5_image));
            term_image.put(6, (ImageView) dialogView.findViewById(R.id.terminal6_image));
            term_image.put(7, (ImageView) dialogView.findViewById(R.id.terminal7_image));
            term_image.put(8, (ImageView) dialogView.findViewById(R.id.terminal8_image));
            term_image.put(9, (ImageView) dialogView.findViewById(R.id.terminal9_image));

            final Map<Integer, TextView> term = new HashMap<>();
            term.put(1, (TextView) dialogView.findViewById(R.id.terminal1_transaction));
            term.put(2, (TextView) dialogView.findViewById(R.id.terminal2_transaction));
            term.put(3, (TextView) dialogView.findViewById(R.id.terminal3_transaction));
            term.put(4, (TextView) dialogView.findViewById(R.id.terminal4_transaction));
            term.put(5, (TextView) dialogView.findViewById(R.id.terminal5_transaction));
            term.put(6, (TextView) dialogView.findViewById(R.id.terminal6_transaction));
            term.put(7, (TextView) dialogView.findViewById(R.id.terminal7_transaction));
            term.put(8, (TextView) dialogView.findViewById(R.id.terminal8_transaction));
            term.put(9, (TextView) dialogView.findViewById(R.id.terminal9_transaction));

            final Map<Integer, TextView> term_queue = new HashMap<>();
            term_queue.put(1, (TextView) dialogView.findViewById(R.id.terminal1_queue));
            term_queue.put(2, (TextView) dialogView.findViewById(R.id.terminal2_queue));
            term_queue.put(3, (TextView) dialogView.findViewById(R.id.terminal3_queue));
            term_queue.put(4, (TextView) dialogView.findViewById(R.id.terminal4_queue));
            term_queue.put(5, (TextView) dialogView.findViewById(R.id.terminal5_queue));
            term_queue.put(6, (TextView) dialogView.findViewById(R.id.terminal6_queue));
            term_queue.put(7, (TextView) dialogView.findViewById(R.id.terminal7_queue));
            term_queue.put(8, (TextView) dialogView.findViewById(R.id.terminal8_queue));
            term_queue.put(9, (TextView) dialogView.findViewById(R.id.terminal9_queue));

            getAllActiveTerminal(term_image, term, term_queue);

            final AlertDialog dialog = builder.create();
            dialog.show();

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        } else if(v == refresh){

        }

    }

    public void checkingActiveQueue(){

        queue_active.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(currentUser).getValue().toString().equals("Active")){
                    extendTime.setClickable(true);
                }else{
                    extendTime.setClickable(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getAllActiveTerminal(final Map<Integer, ImageView> image, final Map<Integer, TextView> Wnumber, final Map<Integer, TextView> Qnumber){

        if(user_queue_info.get("Queue_Number") == null){

            queue_info.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot terminal : dataSnapshot.getChildren()) {
                        int key = Integer.parseInt(terminal.getKey());

                        image.get(key).setVisibility(View.VISIBLE);
                        Wnumber.get(key).setVisibility(View.VISIBLE);
                        Qnumber.get(key).setVisibility(View.VISIBLE);

                        Wnumber.get(key).setText(terminal.child("Office_Name").getValue().toString());
                        Qnumber.get(key).setText(terminal.child("Current_Queue").getValue().toString()
                        );
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }else {

            servicingTerminal.child(user_queue_info.get("Servicing_Office")).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot terminal : dataSnapshot.getChildren()) {
                        int window = Integer.parseInt(terminal.child("Window").getValue().toString());
                        String termName = terminal.child("Name").getValue().toString();

                        // Set Visibility...
                        image.get(window).setVisibility(View.VISIBLE);
                        Wnumber.get(window).setVisibility(View.VISIBLE);
                        Qnumber.get(window).setVisibility(View.VISIBLE);

                        Wnumber.get(window).setText(termName + " " + window);

                        Log.e("Value", terminal.child("Customer_Queue_Number").getValue().toString());
                        String num = terminal.child("Customer_Queue_Number").getValue().toString().replace(" ", "");
                        if (num == "") {

                            Qnumber.get(window).setText("Offline");

                        } else {

                            Qnumber.get(window).setText(num);

                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }


    public void passFullName(){
        activityCommunicator.passDataToActivity(fullName);
    }

    public void passDataToFragment(String value){
        waitingTime.setText(value);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activityCommunicator = (ActivityCommunicator) context;
    }

    public void startWaitingTime(final int Time) {


        if(!checker[0]) {
            // in per second... this is milliseconds...
            final long totalTime = Time * 1000;

            cdt = new CountDownTimer(totalTime, 1000) {
                public void onTick(final long millisUntilFinished) {
                    String withHours;
                    checker[0] = true;
                    if(millisUntilFinished > 60*60*1000){
                        withHours = String.format("%02d:%02d",
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished)));
                    }else{
                        withHours = String.format("%02d:%02d",
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                    }

//                        Log.e("Time", withHours);
                    waitingTime.setText(withHours);

                }

                public void onFinish() {
                    waitingTime.setText("--:--:--");
                    checker[0] = false;
                }
            }.start();

        }

    }
}

