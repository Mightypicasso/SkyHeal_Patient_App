package com.example.latestassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CheckIn extends AppCompatActivity {

    private TextView fullname, id;
    private ImageButton backBtn;
    private Button checkInBtn;

    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        fullname = findViewById(R.id.name);
        id = findViewById(R.id.ID);
        backBtn = findViewById(R.id.backButton);
        checkInBtn = findViewById(R.id.check_in_btn);

        mAuth = FirebaseAuth.getInstance();
        userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckIn.this, main.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        userDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = (String) snapshot.child("fullname").getValue();
                fullname.setText(name);

                String idNum = (String)snapshot.child("idnum").getValue();
                id.setText(idNum);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        checkInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getFullname = fullname.getText().toString().trim();
                String getId = id.getText().toString().trim();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("checkIn");

                Map<String,Object> user = new HashMap<>();
                user.put("fullname", getFullname);
                user.put("id", getId);
                user.put("dateAndTime", getTimeDate());

                ref.child(getTimeDate()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(CheckIn.this, "Successfully Checked In", Toast.LENGTH_SHORT).show();
                    }
                });

                finish();
            }
        });

    }

    public static String getTimeDate() { // without parameter argument
        try{
            Date netDate = new Date(); // current time from here
            SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
            return sfd.format(netDate);
        } catch(Exception e) {
            return "date";
        }
    }

}