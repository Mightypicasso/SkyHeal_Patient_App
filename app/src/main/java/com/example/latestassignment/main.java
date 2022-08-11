package com.example.latestassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class main extends AppCompatActivity {

    private long backPressedTime;
    private LinearLayout toUserSettingPage, checkInPage, toHealthStatusPage, toAppointmentPage, toHealthEducationPage, toFaqPage;
    private CircleImageView profileImage;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toFaqPage = findViewById(R.id.faqPage);
        toHealthEducationPage = findViewById(R.id.toHealthEducation);
        toAppointmentPage = findViewById(R.id.appointmentPage);
        toHealthStatusPage = findViewById(R.id.healthStatusPage);
        toUserSettingPage = findViewById(R.id.toUserSetting);
        checkInPage = findViewById(R.id.checkIn);
        profileImage = findViewById(R.id.profile_image);
        mAuth = FirebaseAuth.getInstance();
        userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );

//        **(for me to check whether user is signed in)**
//        FirebaseUser firebaseUser = mAuth.getCurrentUser();
//        if (firebaseUser == null) {
//            Toast.makeText(this, "not signed in", Toast.LENGTH_SHORT).show();
//
//        } else {
//            Toast.makeText(this, "signed in", Toast.LENGTH_SHORT).show();
//        }

        userDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String imageUrl = (String)snapshot.child("imageUrl").getValue();
                Glide.with(getApplicationContext()).load(imageUrl).into(profileImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        toAppointmentPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main.this, AppointmentPage.class);
                startActivity(intent);
            }
        });

        toHealthStatusPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main.this, HealthStatus.class);
                startActivity(intent);
            }
        });

        checkInPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main.this, CheckIn.class);
                startActivity(intent);
            }
        });

        toHealthEducationPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main.this, HealthEducation.class);
                startActivity(intent);
            }
        });

        toFaqPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main.this, FAQ.class);
                startActivity(intent);
            }
        });

        toUserSettingPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main.this, UserSetting.class);
                startActivity(intent);
            }
        });
    }

    //system back button
    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()){
            finishAffinity();
        }else{
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}