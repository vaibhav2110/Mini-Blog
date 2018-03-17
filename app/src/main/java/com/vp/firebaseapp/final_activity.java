package com.vp.firebaseapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;

import static com.vp.firebaseapp.BranchMaterialFragment.y;

public class final_activity extends AppCompatActivity {
    private RecyclerView bloglist;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Questions");

        int x = y;
        Intent intent2 = getIntent();
        int sem = intent2.getIntExtra("sem", 0);
        String xx = String.valueOf(x);
        String semm = String.valueOf(sem);
        String query = xx+sem;


        bloglist = (RecyclerView)findViewById(R.id.blog_vw);
        bloglist.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        bloglist.setLayoutManager(linearLayoutManager);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Questions").child(query);

        mDatabase.keepSynced(true);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Questions,QuestionHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Questions,QuestionHolder>(
                Questions.class,
                R.layout.question_view,
                QuestionHolder.class,
                mDatabase


        ) {
            @Override
            protected void populateViewHolder(QuestionHolder viewHolder, Questions model, int position) {

                viewHolder.setTitle(getApplicationContext(),model.getTitle());
                viewHolder.setImage(getApplicationContext(),model.getImageUri());
                viewHolder.post_title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

            }
        };
        bloglist.setAdapter(firebaseRecyclerAdapter);

    }

    public static class QuestionHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView post_title;

        public QuestionHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitle(Context ctx, String title){

            post_title = (TextView)mView.findViewById(R.id.subject);
            Typeface custom_font = Typeface.createFromAsset(ctx.getAssets(),  "fonts/DancingScript.otf");

            post_title.setTypeface(custom_font);
            post_title.setText(title);
        }

        public void setImage(Context ctx, String img){
            PhotoView post_image = (PhotoView) mView.findViewById(R.id.img);
            Picasso.with(ctx).load(img).into(post_image);
        }
    }
}
