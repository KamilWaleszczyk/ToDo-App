package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;




public class ProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView name,email;
    private CircleImageView profileimage;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.toolbar);
      /*  setSupportActionBar(toolbar); */
        getSupportActionBar().setTitle("Profil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        profileimage = findViewById(R.id.profile_image);



        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

reference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
if (snapshot.exists()){
    name.setText(snapshot.child("name").getValue().toString());
    email.setText(snapshot.child("email").getValue().toString());

    if(snapshot.hasChild("profilepictureurl")){
        String imageUrl = snapshot.child("profilepictureurl").getValue(String.class);
        Glide.with(getApplicationContext()).load(R.drawable.profile_image).into(profileimage);
    }else
    {
        profileimage.setImageResource(R.drawable.profile_image);
    }





}
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}