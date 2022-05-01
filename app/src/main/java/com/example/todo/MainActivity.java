package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView nav_view;
    private Button Logout;

    private CircleImageView nav_profile_image;
private TextView nav_fullname,nav_email;
private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        toolbar=findViewById(R.id.toolbar);
       /* setSupportActionBar(toolbar);*/


        drawerLayout = findViewById(R.id.drawerLayout);
        nav_view=findViewById(R.id.nav_view);
        Logout=findViewById(R.id.logoutButton);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout,
                toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

nav_view.setNavigationItemSelectedListener(this);


        nav_profile_image = nav_view.getHeaderView(0).findViewById(R.id.nav_user_image);
        nav_fullname = nav_view.getHeaderView(0).findViewById(R.id.nav_user_fullname);
        nav_email = nav_view.getHeaderView(0).findViewById(R.id.nav_user_email);

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
if(snapshot.exists()){
    String name = snapshot.child("name").getValue().toString();
    nav_fullname.setText(name);
    String email = snapshot.child("email").getValue().toString();
    nav_email.setText(email);

if(snapshot.hasChild("profilepictureurl")){
    String imageUrl = snapshot.child("profilepictureurl").getValue(String.class);
    Glide.with(getApplicationContext()).load(imageUrl).into(nav_profile_image);
}else
{
    nav_profile_image.setImageResource(R.drawable.profile_image);
}


}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


Logout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }
});


    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Intent intent = null;
        if (item.getItemId() == R.id.profile) {
            intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.addGroup) {
            intent = new Intent(MainActivity.this, AddGroupActivity.class);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.mygroup) {
            intent = new Intent(MainActivity.this, MyGroupActivity.class);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.setting) {
            intent = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent);
            return true;
        }




        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
}