package com.vp.firebaseapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.URI;

public class CreatePost extends AppCompatActivity {
    private ImageButton img;
    private static final int GALLERY_INTENT = 1;
    private StorageReference mStorage;
    private Button post;
    private EditText title;
    private EditText desc;
    private Uri uri = null;
    private ProgressDialog prdialog;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        prdialog = new ProgressDialog(this);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/DancingScript.otf");
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        img = (ImageButton)findViewById(R.id.imageButton);
        mStorage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userData = FirebaseDatabase.getInstance().getReference().child("Users Lists").child(mAuth.getCurrentUser().getUid());
        title = (EditText)findViewById(R.id.editText);
        title.setTypeface(custom_font);
        desc = (EditText)findViewById(R.id.editText2);
        desc.setTypeface(custom_font);
        post = (Button)findViewById(R.id.post);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent,GALLERY_INTENT);
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PostData();
            }
        });
    }

    private void PostData() {

        prdialog.setMessage("Uploading...");


        final String title_val = title.getText().toString();
        final String desc_val = desc.getText().toString();
        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && uri!=null){
            prdialog.show();
            StorageReference mref = mStorage.child("Blog_Posts").child(uri.getLastPathSegment());
            mref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    final Uri downloadurl = taskSnapshot.getDownloadUrl();
                    final DatabaseReference mChild = mDatabase.child("Blogs").push();
                    userData.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mChild.child("Title").setValue(title_val);
                            mChild.child("Desc").setValue(desc_val);
                            mChild.child("ImageUri").setValue(downloadurl.toString());
                            mChild.child("Uid").setValue(mAuth.getCurrentUser().getUid());
                            mChild.child("ProPic").setValue(dataSnapshot.child("Image").getValue());
                            mChild.child("Username").setValue(dataSnapshot.child("Name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    startActivity(new Intent(CreatePost.this,MainActivity.class));

                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    prdialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreatePost.this,"Upload Unsuccessful...",Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            uri = data.getData();
            CropImage.activity(uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageuri = result.getUri();
                img.setImageURI(imageuri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
