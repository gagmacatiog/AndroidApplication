package com.example.georgealbert.queuemobileapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener, ActivityCommunicator {

    private FirebaseAuth firebaseAuth;
    private TextView userEmail;
    String currentUser, fullName;
    private ImageView usernotification, userlogs, useraddqueue, userImage;
    public String currentUserPassword;

    private ApplicationClass applicationClass;
    private FrameLayout notificationLayout, infoLayout;

    private ProgressDialog progressDialog;
    private MenuItem menuItem, menuItem2;
    private Animation slideDown, slideUp;

    queue_list qList = new queue_list();

    public boolean logsVisited = false, notificationVisited = false, infoVisited = false;

    DatabaseReference databaseReference, queue_status, transaction_type;

    private FragmentRefreshListener fragmentRefreshListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar topToolbar = (Toolbar)findViewById(R.id.my_toolbar1);
        setSupportActionBar(topToolbar);

        applicationClass = new ApplicationClass();
        progressDialog = new ProgressDialog(this);

        usernotification = (ImageView)findViewById(R.id.imageView7);
        userlogs = (ImageView)findViewById(R.id.imageView6);
        useraddqueue = (ImageView)findViewById(R.id.imageView4);
        userImage = (ImageView)findViewById(R.id.imageView3);

        notificationLayout = (FrameLayout) findViewById(R.id.notification_layout);
        userEmail = (TextView)findViewById(R.id.textView4);

        infoLayout = (FrameLayout) findViewById(R.id.info_layout);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        usernotification.setOnClickListener(this);
        userlogs.setOnClickListener(this);
        useraddqueue.setOnClickListener(this);
        userImage.setOnClickListener(this);

        currentUser = firebaseAuth.getCurrentUser().getEmail().split("@")[0];
//        userEmail.setText("ID # "+ currentUser);

        databaseReference = FirebaseDatabase.getInstance().getReference("Pre_Queue");
        queue_status = FirebaseDatabase.getInstance().getReference("Queue_Status");
        transaction_type = FirebaseDatabase.getInstance().getReference("Transaction_Type");

        displayFragment(1);
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkingActiveQueue();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

//            case  R.id.action_zero:
//
//                startActivity(new Intent(getApplicationContext(), AddLogs.class));
//
//                return true;
//
//            case  R.id.action_zero_point:
//
//                startActivity(new Intent(getApplicationContext(), AddNotification.class));
//
//                return true;

            case R.id.action_five:
                // User Account
                startActivity(new Intent(getApplicationContext(), Profile.class));

                return true;

            case R.id.action_three:
                // Application notification
                if(notificationVisited==false) {
                    displayFragment(3);
                }
                if(notificationLayout.getVisibility() == View.GONE) {
                    if(infoLayout.getVisibility() == View.VISIBLE) {
                        pageAnimation(2, 2);
                        menuItem2.setIcon(R.drawable.ic_action_menu_info_ia);

                        // Delay 1 second
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                            }
                        }, 1000);
                    }
                    pageAnimation(1, 1);
                    menuItem.setIcon(buildCounterDrawable(0, R.drawable.ic_action_usernotification_active_2));
                }else{
                    pageAnimation(2, 1);
                    menuItem.setIcon(buildCounterDrawable(0, R.drawable.ic_action_usernotification));
                }
                return true;

            case R.id.action_four:
//                infoLayout.setVisibility(View.VISIBLE);
                // Application Info..
                if(infoLayout.getVisibility() == View.GONE) {
                    if(notificationLayout.getVisibility() == View.VISIBLE) {
                        pageAnimation(2, 1);
                        menuItem.setIcon(buildCounterDrawable(0, R.drawable.ic_action_usernotification));

                        // Delay 1 second
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                            }
                        }, 1000);
                    }
                    pageAnimation(1, 2);
                    menuItem2.setIcon(R.drawable.ic_action_menu_info);
                }else{
                    pageAnimation(2, 2);
                    menuItem2.setIcon(R.drawable.ic_action_menu_info_ia);
                }
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    // On alertDialog Invalid Values

    public void onAlertDialogInvalid(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("We detected a False Account Password!")
                .setTitle("Invalid Password");

        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
            }
        });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int count = 1;
        getMenuInflater().inflate(R.menu.main, menu);

        menuItem = menu.findItem(R.id.action_three);
        menuItem2 = menu.findItem(R.id.action_four);
        menuItem.setIcon(buildCounterDrawable(count, R.drawable.ic_action_usernotification));

        return true;
    }

    private Drawable buildCounterDrawable(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.custom_action_item_layout, null);
        view.setBackgroundResource(backgroundImageId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.count);
            textView.setText("" + count);
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(getResources(), bitmap);
    }

    public void pageAnimation(int x, final int y){

        switch (x){
            case 1:
                slideDown = AnimationUtils.loadAnimation (getApplicationContext(), R.anim.slide_down);

                switch (y){
                    case 1:
                        notificationLayout.setAnimation(slideDown);
                        notificationLayout.setVisibility(View.VISIBLE);
                        break;

                    case 2:
                        infoLayout.setAnimation(slideDown);
                        infoLayout.setVisibility(View.VISIBLE);
                        break;

                    default:
                        break;
                }

                break;

            case 2:
                slideUp = AnimationUtils.loadAnimation (getApplicationContext(), R.anim.slide_up);

                switch (y){
                    case 1:
                        notificationLayout.setAnimation(slideUp);
                        notificationLayout.setVisibility(View.GONE);
                        break;

                    case 2:
                        infoLayout.setAnimation(slideUp);
                        infoLayout.setVisibility(View.GONE);
                        break;

                    default:
                        break;
                }

                break;

            default:
                break;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (y){
                    case 1:
                        notificationLayout.clearAnimation();
                        break;
                    case 2:
                        infoLayout.clearAnimation();
                        break;
                    default:
                        break;
                }
            }
        }, 1000);
    }

    @Override
    public void onClick(View v) {
        if(notificationLayout.getVisibility() == View.VISIBLE) {
            pageAnimation(2, 1);
        }
        useraddqueue.setImageResource(R.drawable.ic_menu_add);
        userlogs.setImageResource(R.drawable.ic_action_userlogs);
        usernotification.setImageResource(R.drawable.ic_action_userhome);
        if(v == usernotification){

//            Log.i("Info", "Pressed: user notification");
            displayFragment(1);
            usernotification.setImageResource(R.drawable.ic_action_usernotification_active);

        }else if(v == userlogs){

//            Log.i("Info", "Pressed: user logs");
            displayFragment(2);
            userlogs.setImageResource(R.drawable.ic_action_userlogs_active);


        }else if(v == useraddqueue){

//            Log.i("Info", "Pressed: user add queue");
            useraddqueue.setImageResource(R.drawable.ic_menu_add_active);

            // 1. Instantiate an AlertDialog.Builder with its constructor
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.custom_terminal_dialog, null);
            builder.setView(dialogView);

//            final Spinner terminalList = (Spinner) dialogView.findViewById(R.id.terminal_spinner);

            final Button cancel = (Button) dialogView.findViewById(R.id.cancel);
            final Button submit = (Button) dialogView.findViewById(R.id.submit);
            final Spinner spinner = (Spinner) dialogView.findViewById(R.id.spinner2);

            loadSpinnerValue(spinner);
            final ArrayList<Integer> type_id = new ArrayList<>();

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    type_id.clear();
                    transaction_list list = (transaction_list) parent.getSelectedItem();
                    type_id.add(list.getId());
//                    spinner.setSelection(list.getId());
//                    Toast.makeText(context, "Country ID: "+country.getId()+",  Country Name : "+country.getName(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });


            final AlertDialog dialog = builder.create();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {

                    if(getFragmentRefreshListener()!=null){
                        getFragmentRefreshListener().onRefresh();
                    }

                }
            });

            dialog.show();

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                int tType = type_id.get(0);

                String id = databaseReference.push().getKey();

                //Setting that user has an active queue;
                queue_status.child(currentUser).setValue("Active");

                databaseReference.child(id).child("Transaction_Type").setValue(tType);
                databaseReference.child(id).child("Student_No").setValue(currentUser);
                databaseReference.child(id).child("Full_Name").setValue(fullName);
                databaseReference.child(id).child("Date").setValue(ServerValue.TIMESTAMP);

                dialog.dismiss();

                }

            });

        }else if(v == userImage){

            // 1. Instantiate an AlertDialog.Builder with its constructor
//            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            LayoutInflater inflater = this.getLayoutInflater();
//            final View dialogView = inflater.inflate(R.layout.user_profile, null);
//            builder.setView(dialogView);
//
////            final Spinner terminalList = (Spinner) dialogView.findViewById(R.id.terminal_spinner);
//
//            final AlertDialog dialog = builder.create();
//            dialog.show();
        }
    }

    public void checkingActiveQueue(){

        queue_status.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(currentUser).getValue().toString().equals("Active")){
                    useraddqueue.setClickable(false);
                }else{
                    useraddqueue.setClickable(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void activityRestart(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void loadSpinnerValue(final Spinner spin){

        final ArrayList<transaction_list> type = new ArrayList<>();
        transaction_type.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    type.add(new transaction_list(Integer.parseInt(snapshot.child("id").getValue().toString()),
                            snapshot.child("Transaction_Name").getValue().toString()));

                }

                //fill data in spinner
                ArrayAdapter<transaction_list> adapter = new ArrayAdapter<transaction_list>(Main2Activity.this, R.layout.spinner_item, type);
                adapter.setDropDownViewResource(R.layout.spinner_item);
                spin.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void displayFragment(int x){

        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FrameLayout mainLayout = (FrameLayout)findViewById(R.id.layout_template);
        FrameLayout logsLayout = (FrameLayout)findViewById(R.id.logs_layout);



        switch (x){
            case 1:
                logsLayout.setVisibility(View.GONE);
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out);
                ft.replace(mainLayout.getId(), applicationClass.list1);
                mainLayout.setVisibility(View.VISIBLE);
                break;

            case 2:
                mainLayout.setVisibility(View.GONE);
                ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out_left);
                ft.replace(logsLayout.getId(), applicationClass.list2);
                logsLayout.setVisibility(View.VISIBLE);

                break;

            case 3:
//                ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out_left);

                ft.replace(R.id.notification_layout, applicationClass.list3);

                break;

            case 4:
//                ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out_left);

                ft.replace(R.id.info_layout, applicationClass.list4);

                break;

            default:
                break;

        }

        if(x==2 && logsVisited == false) {
            progressDialog.setMessage("Loading resources...");
            progressDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    logsVisited = true;
                }
            }, 3000);
        }

        ft.commit();
    }

    public void passDataToActivity(String value){
        this.fullName = value;
    }

    public interface FragmentRefreshListener{
        void onRefresh();
    }

    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

}
