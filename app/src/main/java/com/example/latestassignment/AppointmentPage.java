package com.example.latestassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.latestassignment.Adapter.DoctorAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AppointmentPage extends AppCompatActivity {

    private RecyclerView recyclerView;

    private ArrayList<DoctorHelperClass> doctorList;
    private ImageButton back;
    private DoctorAdapter doctorAdapter;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_page);

        back = findViewById(R.id.returnBtn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppointmentPage.this, main.class);
                startActivity(intent);
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        doctorList = new ArrayList<>();
        doctorAdapter = new DoctorAdapter(AppointmentPage.this, doctorList);

        recyclerView.setAdapter(doctorAdapter);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("doctors");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DoctorHelperClass doctorHelperClass = dataSnapshot.getValue(DoctorHelperClass.class);
                    doctorList.add(doctorHelperClass);
                }
                doctorAdapter.notifyDataSetChanged();

                if (doctorList.isEmpty()) {
                    Toast.makeText(AppointmentPage.this, "No Doctor Is Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        finish();
    }
}