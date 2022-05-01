package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MyGroupActivity extends AppCompatActivity {

    ListView listViewGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mygroup);

        listViewGroup = findViewById(R.id.listViewGroup);
        final ArrayList<Grupa> Lista = new ArrayList<>();
        final ArrayList <String> List = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
                android.R.id.text1,List);

        listViewGroup.setAdapter(adapter);

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("grupy");
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Lista.clear();
                List.clear();
                for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                    Grupa n = childDataSnapshot.getValue(Grupa.class);

                    String txt=n.getName();
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





        listViewGroup.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view,  int position, long id)
            {
                Intent i= new Intent(MyGroupActivity.this,HomeActivity.class);

                i.putExtra("grupaa",listViewGroup.getItemAtPosition(position).toString());
                startActivity(i);
            }
        });
    }
}
