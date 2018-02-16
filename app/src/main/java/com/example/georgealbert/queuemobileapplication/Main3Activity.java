package com.example.georgealbert.queuemobileapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Main3Activity extends AppCompatActivity{

    private ImageView goback;
    private CheckBox termsandcondition;
    private EditText username, password, retypepassword;
    private Button register;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in, R.anim.zoom_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.zoom_out);
        setContentView(R.layout.activity_main3);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), Main2Activity.class));
            finish();
        }

        progressDialog = new ProgressDialog(this);

        goback = (ImageView)findViewById(R.id.imageView2);
        termsandcondition = (CheckBox)findViewById(R.id.checkBox);
        username = (EditText)findViewById(R.id.editText);
        password = (EditText)findViewById(R.id.editText5);
        retypepassword = (EditText)findViewById(R.id.editText6);
        register = (Button)findViewById(R.id.button2);
        username.requestFocus();

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //return to login view
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.zoom_in, R.anim.slide_out);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //register user
                registerUser();
            }
        });

    }

    public void registerUser(){
        String extension = "@usep.edu.ph";
        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String repass = retypepassword.getText().toString().trim();

        if(TextUtils.isEmpty(user)){
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(repass)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }else if(!pass.equals(repass)){
            Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show();
            return;
        }

        user = user + extension;

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        try{

            firebaseAuth.createUserWithEmailAndPassword(user, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful()){
                                Toast.makeText(Main3Activity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Main2Activity.class));
                                finish();
                            }else{
                                Toast.makeText(Main3Activity.this, "Check Internet connection", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }catch (Exception ex){
            Toast.makeText(Main3Activity.this, ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }

    }

}
