package com.vp.firebaseapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterAct extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button sign;
    private EditText name;
    private EditText email;
    private EditText passw;
    private DatabaseReference mref;
    private ProgressDialog dialog;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar) ;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        sign = (Button)findViewById(R.id.signin);
        name = (EditText)findViewById(R.id.name);
        passw = (EditText)findViewById(R.id.password);
        email = (EditText)findViewById(R.id.email);
        mref = FirebaseDatabase.getInstance().getReference().child("Users Lists");
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartSignIn();
            }
        });
    }

    private void StartSignIn() {

        final String user_name = name.getText().toString();
        String user_email = email.getText().toString();
        String user_passw = passw.getText().toString();
        if(!TextUtils.isEmpty(user_email) && !TextUtils.isEmpty(user_passw) && user_passw.length()>5){

            dialog.setMessage("Creating...");
            dialog.show();
        mAuth.createUserWithEmailAndPassword(user_email,user_passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    dialog.dismiss();
                    Intent intent = new Intent(RegisterAct.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                if(!task.isSuccessful()){
                    Toast.makeText(RegisterAct.this, "Unsuccessful",Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
        });
    }}

}
