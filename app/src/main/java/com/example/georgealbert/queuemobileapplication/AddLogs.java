package com.example.georgealbert.queuemobileapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddLogs extends AppCompatActivity {

    private Spinner terminal, details;
    private Button addData, back;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_logs);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseLogs = FirebaseDatabase.getInstance().getReference("sample/logs/gmacatiog");

        terminal = (Spinner)findViewById(R.id.spinner);
        details = (Spinner)findViewById(R.id.spinner3);
        addData = (Button)findViewById(R.id.button3);
        back = (Button)findViewById(R.id.button4);

        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addLogs();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addLogs(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy HH:mm");
        String dateTime = sdf.format(c.getTime());
        String term = terminal.getSelectedItem().toString();
        String detail = details.getSelectedItem().toString();
        String status = "Done";

        String id = databaseLogs.push().getKey();

        Logs logs = new Logs(dateTime, term, detail, status);

        databaseLogs.child(id).setValue(logs);
    }
}
