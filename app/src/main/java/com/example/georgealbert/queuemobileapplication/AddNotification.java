package com.example.georgealbert.queuemobileapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class AddNotification extends AppCompatActivity {

    private Spinner notif;
    private Button addData, back;
    private DatabaseReference databaseNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notification);

        databaseNotification = FirebaseDatabase.getInstance().getReference("sample/notification/gmacatiog");

        notif = (Spinner)findViewById(R.id.spinner);
        addData = (Button)findViewById(R.id.button3);
        back = (Button)findViewById(R.id.button4);

        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addNotification();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addNotification(){


        String id = databaseNotification.push().getKey();
        String notificationMessage = notif.getSelectedItem().toString();
        String ID = new SimpleDateFormat("yyyyMMddkkmmss", Locale.getDefault()).format(new Date());
        String Date = new SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.getDefault()).format(new Date());
        new Notification(ID, notificationMessage, Date);
        Map<String, String> map = new HashMap<String, String>();
        map.put("notificationID", ID);
        map.put("notificationMessage", notificationMessage);
        map.put("notificationDate", Date);
        databaseNotification.child(id).setValue(map);

    }
}
