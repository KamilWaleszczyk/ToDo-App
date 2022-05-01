package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SelectRegistrationActivity extends AppCompatActivity {

    private Button RejButton;
    private TextView BackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_registration);
        RejButton = findViewById(R.id.RejButton);
        BackButton = findViewById(R.id.backButton);

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectRegistrationActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        RejButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(SelectRegistrationActivity.this,RegistrationActivity.class);
            startActivity(intent);
            }
        });
    }
}