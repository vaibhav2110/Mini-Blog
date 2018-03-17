package com.vp.firebaseapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Single_user extends AppCompatActivity {
    private TextView title;
    private TextView desc;
    private ImageView main_img;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Button rem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_user);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/DancingScript.otf");
        Typeface custom_font2 = Typeface.createFromAsset(getAssets(),  "fonts/Remachine.ttf");

        mAuth = FirebaseAuth.getInstance();
        final String post_key = getIntent().getExtras().getString("post_k");
        title = (TextView)findViewById(R.id.title);
        title.setTypeface(custom_font);

        desc = (TextView)findViewById(R.id.desc);
        desc.setTypeface(custom_font2);
        rem = (Button)findViewById(R.id.remove);
        main_img = (ImageView)findViewById(R.id.mainimg);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blogs").child(post_key);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String)dataSnapshot.child("Title").getValue();
                String post_desc = (String) dataSnapshot.child("Desc").getValue();
                String post_img = (String) dataSnapshot.child("ImageUri").getValue();
                String post_uid = (String) dataSnapshot.child("Uid").getValue();

                title.setText(post_title);
                desc.setText(post_desc);

                Picasso.with(Single_user.this).load(post_img).into(main_img);
                if(mAuth.getCurrentUser().getUid().equals(post_uid)){

                    rem.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        rem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.removeValue();
                Intent intent = new Intent(Single_user.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });





    }
}
