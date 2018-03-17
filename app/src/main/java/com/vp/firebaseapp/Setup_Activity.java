package com.vp.firebaseapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class Setup_Activity extends AppCompatActivity {
    private Button propic;
    private EditText name;
    private Button fin;
    private static final int GALLERY_INTENT=1;
    private Uri imageuri = null;
    private StorageReference mStorage;
    private DatabaseReference mrefere;
    private FirebaseAuth mAuth;
    private ProgressDialog message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar) ;
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        propic = (Button)findViewById(R.id.selpic);

        message = new ProgressDialog(this);
        message.setMessage("Setting You Up..!!");
        name = (EditText) findViewById(R.id.editname);
        fin = (Button)findViewById(R.id.Finish);
        mrefere = FirebaseDatabase.getInstance().getReference().child("Users Lists");
        mStorage = FirebaseStorage.getInstance().getReference().child("Profile Pics");
        propic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gintent = new Intent(Intent.ACTION_GET_CONTENT);
                gintent.setType("image/*");
                startActivityForResult(gintent,GALLERY_INTENT);
            }
        });
        fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartSetUp();
            }
        });
    }


    private void StartSetUp() {

        final String user_name = name.getText().toString().trim();
        final String uid = mAuth.getCurrentUser().getUid();
        if(!TextUtils.isEmpty(user_name) && imageuri!=null){
            message.show();
            StorageReference filepath = mStorage.child(imageuri.getLastPathSegment());
            filepath.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    message.dismiss();
                    String downloaduri = taskSnapshot.getDownloadUrl().toString();
                    mrefere.child(uid).child("Name").setValue(user_name);
                    mrefere.child(uid).child("Image").setValue(downloaduri);
                    Intent intent = new Intent(Setup_Activity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Setup_Activity.this,"Unable to Create Account",Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_INTENT && resultCode==RESULT_OK){

            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageuri = result.getUri();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
