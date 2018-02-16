package com.example.georgealbert.queuemobileapplication;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class notification_list extends Fragment {

    private DatabaseReference databaseNotification;
    private List<Notification> notificationList;
    private ListView listViewNotification;

    @Override
    public void onStart() {
        super.onStart();

        databaseNotification.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                notificationList.clear();

                for(DataSnapshot notifSnapshot: dataSnapshot.getChildren()){
                    Notification notif = notifSnapshot.getValue(Notification.class);

                    notificationList.add(notif);
                }

                Notification_Activity adapter = new Notification_Activity(getActivity(), notificationList);
                listViewNotification.setAdapter(adapter);

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

        View view = inflater.inflate(R.layout.fragment_notification_list, container, false);
        listViewNotification = (ListView)view.findViewById(R.id.listViewNotification);
        notificationList = new ArrayList<>();
        databaseNotification = FirebaseDatabase.getInstance().getReference("sample/notification/gmacatiog");
        return view;
    }

}

