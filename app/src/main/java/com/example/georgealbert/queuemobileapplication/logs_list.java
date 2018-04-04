package com.example.georgealbert.queuemobileapplication;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class logs_list extends Fragment {

    Main2Activity m2 = new Main2Activity();

    private DatabaseReference databaseLogs;
    private List<Logs> logsList;
    private ListView listViewLogs;

    FirebaseAuth firebaseAuth;

    @Override
    public void onStart() {
        super.onStart();

        databaseLogs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                logsList.clear();

                for(DataSnapshot logsSnapshot: dataSnapshot.getChildren()){
                    Logs logs = logsSnapshot.getValue(Logs.class);

                    logsList.add(logs);
                }

                Logs_Activity adapter = new Logs_Activity(getActivity(), logsList);
                listViewLogs.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_logs_list, container, false);
        listViewLogs = (ListView)view.findViewById(R.id.listViewLogs);
        firebaseAuth = FirebaseAuth.getInstance();
        String user = firebaseAuth.getCurrentUser().getEmail().split("@")[0];
        logsList = new ArrayList<>();
        databaseLogs = FirebaseDatabase.getInstance().getReference("Accounts/"+user+"/Logs");
        databaseLogs.keepSynced(true);

        return view;
    }

}

