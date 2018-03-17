package com.vp.firebaseapp;



import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.Manifest;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.senab.photoview.PhotoView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private RecyclerView bloglist;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthlistener;
    private DatabaseReference mrefer;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference mDatabaseLike;
    private static final int REQUEST_READ_STORAGE = 112;

    private boolean mProcessLike = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        bloglist = (RecyclerView)findViewById(R.id.blog_vw);
        bloglist.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        bloglist.setLayoutManager(linearLayoutManager);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        boolean hasPermission = (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED);

        if(!hasPermission){

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_STORAGE);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(MainActivity.this,CreatePost.class));
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blogs");
        mrefer = FirebaseDatabase.getInstance().getReference().child("Users Lists");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
        mrefer.keepSynced(true);
        mDatabase.keepSynced(true);
        mDatabaseLike.keepSynced(true);
        mAuth = FirebaseAuth.getInstance();
        mAuthlistener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                  if(firebaseAuth.getCurrentUser()==null){
                      Intent intent = new Intent(MainActivity.this,login_activity.class);
                      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                      startActivity(intent);
                  }
            }
        };

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_READ_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //reload my activity with permission granted or use the features what required the permission
                } else
                {
                    Toast.makeText(MainActivity.this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Question) {

            startActivity(new Intent(MainActivity.this,QuestionBank.class));

        }

         else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
        checkuser();}

        mAuth.addAuthStateListener(mAuthlistener);
        FirebaseRecyclerAdapter<Blog,blogholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, blogholder>(
                Blog.class,
                R.layout.blog_view,
                blogholder.class,
                mDatabase


        ) {
            @Override
            protected void populateViewHolder(blogholder viewHolder, Blog model, int position) {

                final String post_key = getRef(position).getKey();
                 viewHolder.setTitle(getApplicationContext(),model.getTitle());
                 viewHolder.setDesc(getApplicationContext(),model.getDesc());
                 viewHolder.setImage(getApplicationContext(),model.getImageUri());
                 viewHolder.setUsername(getApplicationContext(),model.getUsername());
                 viewHolder.setlkbutton(post_key);
                 viewHolder.setProPic(getApplicationContext(),model.getProPic());

                 viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Intent intent = new Intent(MainActivity.this,Single_user.class);
                         intent.putExtra("post_k",post_key);
                         startActivity(intent);
                     }
                 });

                viewHolder.lkbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mProcessLike = true;
                        mDatabaseLike.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(mProcessLike){

                                    if(dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())){
                                        mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                        mProcessLike = false;
                                    }
                                    else{
                                        mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("Random");
                                        mProcessLike = false;

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }
        };
        bloglist.setAdapter(firebaseRecyclerAdapter);

    }
    private void checkuser() {
        final String User_id = mAuth.getCurrentUser().getUid();
        mrefer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(User_id)){
                    Intent intent = new Intent(MainActivity.this,Setup_Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static class blogholder extends RecyclerView.ViewHolder{

        View mView;
        ImageButton lkbutton;
        DatabaseReference mDatabaselike;
        FirebaseAuth mAtuth;
        public blogholder(View itemView) {
            super(itemView);
            mView = itemView;
            lkbutton = (ImageButton)mView.findViewById(R.id.likebtn);
            mAtuth = FirebaseAuth.getInstance();
            mDatabaselike = FirebaseDatabase.getInstance().getReference().child("Likes");
            mDatabaselike.keepSynced(true);
        }

        public void setTitle(Context ctx,String title){

            TextView post_title = (TextView)mView.findViewById(R.id.title);
            Typeface custom_font = Typeface.createFromAsset(ctx.getAssets(),  "fonts/DancingScript.otf");

            post_title.setTypeface(custom_font);
            post_title.setText(title);
        }

        public void setlkbutton(final String post_key){
            if(mAtuth.getCurrentUser()!=null){
            mDatabaselike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(post_key).hasChild(mAtuth.getCurrentUser().getUid())){
                        lkbutton.setImageResource(R.drawable.ic_thumb_up_red_24dp);
                    }
                    else{
                         lkbutton.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }}


        public void setDesc(Context ctx,String desc){
            Typeface custom_font = Typeface.createFromAsset(ctx.getAssets(),  "fonts/Remachine.ttf");

            TextView post_desc = (TextView)mView.findViewById(R.id.password);
            post_desc.setTypeface(custom_font);
            post_desc.setText(desc);
        }

        public void setImage(Context ctx, String img){
            PhotoView post_image = (PhotoView) mView.findViewById(R.id.img);
            Picasso.with(ctx).load(img).into(post_image);
        }

        public void setUsername(Context ctx,String username){
            Typeface custom_font = Typeface.createFromAsset(ctx.getAssets(),  "fonts/Remachine.ttf");
            TextView user = (TextView)mView.findViewById(R.id.username);
            user.setTypeface(custom_font);
            user.setText(username);
        }

        public void setProPic(Context ctx,String proPic){

            CircleImageView propic = (CircleImageView)mView.findViewById(R.id.profile_image);
            Picasso.with(ctx).load(proPic).into(propic);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                return true;
            case R.id.action_newpost:
                startActivity(new Intent(MainActivity.this,CreatePost.class));
                return true;
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

