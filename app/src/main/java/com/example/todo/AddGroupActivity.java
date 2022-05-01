package com.example.todo;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import static android.content.ContentValues.TAG;

public class AddGroupActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView nav_view;
    private Button Logout;

    private CircleImageView nav_profile_image;
    private TextView nav_fullname,nav_email;
    private DatabaseReference userRef;






    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private TextInputEditText nameGroup;
    private Button AddButton;
    private Button AddButton2;
    private ListView listView;
   private int licznik=0;
    private String licz;
    final Random generator = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addgroup);



        toolbar=findViewById(R.id.toolbar);
        /* setSupportActionBar(toolbar);*/


        drawerLayout = findViewById(R.id.drawerLayout);
        nav_view=findViewById(R.id.nav_view2);
        Logout=findViewById(R.id.logoutButton);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(AddGroupActivity.this,drawerLayout,
                toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nav_view.setNavigationItemSelectedListener( this);


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

















        nameGroup = findViewById(R.id.groupname);
        AddButton = findViewById(R.id.addgroupButton);
        AddButton2 = findViewById(R.id.addgroupButton2);
listView = findViewById(R.id.listView);
        mAuth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = mAuth.getCurrentUser().getUid();
       final ArrayList <Grupa> Lista = new ArrayList<>();
        final ArrayList <String> List = new ArrayList<>();
final ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_item , List);
listView.setAdapter(adapter);


        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("grupy");
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Lista.clear();
                List.clear();
                for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                    Grupa n = childDataSnapshot.getValue(Grupa.class);
                    Grupa w =new Grupa("n.getName()",123 );
                    String txt=n.getName()+ " : " + n.getId();
                    List.add(txt);
                    Lista.add(n);
                }
adapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });


        AddButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                Integer numer;

              numer = Integer.parseInt(nameGroup.getText().toString());

               // numer = nameGroup.getText().toString();

                for(Integer i=0;i<Lista.size();i++)
                {
                  //  databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).child("listaGrup").child("11").setValue(q);

                    if(Lista.get(i).getId().equals(numer))
                    {

                        Grupa q = new Grupa(Lista.get(i).getName(),Lista.get(i).getId());
                        databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).child("listaGrup").child(nameGroup.getText().toString()).setValue(q);

                    }
                }






                // Grupa2();


            }
        });
      Grupa();












    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Intent intent = null;
        if (item.getItemId() == R.id.profile) {
            intent = new Intent(AddGroupActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.addGroup) {
            intent = new Intent(AddGroupActivity.this, AddGroupActivity.class);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.mygroup) {
            intent = new Intent(AddGroupActivity.this, MyGroupActivity.class);
            startActivity(intent);
            return true;
        }





        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }


    public void Grupa2(){

   /*     Lista.clear();
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("grupy");
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Grupa w = childDataSnapshot.getValue(Grupa.class);
                    Lista.add(w);
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });


*/





  /*      DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("grupy").child(numer);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String type = snapshot.child("id").getValue().toString();




                        if (type.equals(numer)){

                           String nazwa = snapshot.child("name").getValue().toString();

                            Grupa c = new Grupa(nazwa,numer);


                            databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).child("listaGrup").child(nameGroup.getText().toString()).setValue(c);

                        }else {
                            //  result = "donor";
                        }













            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        */






    }



public void Grupa(){




    AddButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Integer w = generator.nextInt(10000000);
            Grupa c = new Grupa(nameGroup.getText().toString(),w);

            databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).child("listaGrup").child(w.toString()).setValue(c);
            databaseReference.child("grupy").child(w.toString()).setValue(c);



        }
    });


}






        }






