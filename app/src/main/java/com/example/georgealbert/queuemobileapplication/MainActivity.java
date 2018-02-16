package com.example.georgealbert.queuemobileapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.support.v7.widget.CardView;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private CardView logBtn;
    private EditText username, password;
    private TextView register;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), Main2Activity.class));
            finish();
        }

        logBtn = (CardView)findViewById(R.id.logBtn);
        username = (EditText)findViewById(R.id.editText3);
        password = (EditText)findViewById(R.id.editText4);
        register = (TextView)findViewById(R.id.textView2);
        username.requestFocus();

        logBtn.setOnClickListener(this);
        register.setOnClickListener(this);


    }

    private void userLogin(){
        String user = username.getText().toString().trim();
        final String pass = password.getText().toString().trim();

        if(TextUtils.isEmpty(user)){
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Fetching Account Information...");
        progressDialog.show();

        user = user + "@usep.edu.ph";

        firebaseAuth.signInWithEmailAndPassword(user, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            // Start Main menu activity
                            startActivity(new Intent(getApplicationContext(), Main2Activity.class));
                            new Main2Activity().currentUserPassword = pass;
                            finish();
                        }else{
                            Toast.makeText(MainActivity.this, "Check Internet connection", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        if(v == register){
            finish();
            startActivity(new Intent(getApplicationContext(), Main3Activity.class));
        }else if(v == logBtn){
            userLogin();
        }
    }
}
