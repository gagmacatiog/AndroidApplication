package com.example.georgealbert.queuemobileapplication;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by George Albert on 12/12/2017.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("MyFirebaseId", "Refreshed token: " + refreshedToken.toString());

        // TODO: Implement this method to send any registration to your app's servers.

        storeToken(refreshedToken);
    }

    private void storeToken(String token){
        SharedPrefManager.getInstance(getApplicationContext()).storeToken(token);
    }
}
