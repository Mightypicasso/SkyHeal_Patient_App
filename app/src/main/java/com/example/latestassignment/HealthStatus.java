package com.example.latestassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class HealthStatus extends AppCompatActivity {

    EditText HeartRate,BloodPressure,BloodGlucoseLvl,BloodOxygenLvl,DeepSleep,BMI,MetabolicAge;
    ImageView backbtn;
    Button BtnInsertData;
    FirebaseAuth mAuth;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_status);

        backbtn = findViewById(R.id.backBTN_HS);
        HeartRate=(EditText) findViewById(R.id.hRate);
        BloodPressure=(EditText) findViewById(R.id.bPressure);
        BloodGlucoseLvl=(EditText) findViewById(R.id.bGlucoseLvl);
        BloodOxygenLvl=(EditText)  findViewById(R.id.bOxyLvl);
        DeepSleep=(EditText) findViewById(R.id.deepSleep);
        BMI=(EditText)findViewById(R.id.bmi);
        MetabolicAge=(EditText)findViewById(R.id.metabolicAge);
        BtnInsertData=(Button) findViewById(R.id.insert_data_btn);

        mAuth = FirebaseAuth.getInstance();

        ref = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("HealthStatus");

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HealthStatus.this, main.class);
                startActivity(intent);
                finish();
            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String bloodGlucoseLevel = (String) snapshot.child("bloodGlucoseLevel").getValue();
                    BloodGlucoseLvl.setText(bloodGlucoseLevel);

                    String bloodOxygenLevel = (String) snapshot.child("bloodOxygenLevel").getValue();
                    BloodOxygenLvl.setText(bloodOxygenLevel);

                    String bloodPressure = (String) snapshot.child("bloodPressure").getValue();
                    BloodPressure.setText(bloodPressure);

                    String bmi = (String) snapshot.child("bmi").getValue();
                    BMI.setText(bmi);

                    String deepSleepHours = (String) snapshot.child("deepSleepHours").getValue();
                    DeepSleep.setText(deepSleepHours);

                    String heartRate = (String) snapshot.child("heartRate").getValue();
                    HeartRate.setText(heartRate);

                    String metabolicAge = (String) snapshot.child("metabolicAge").getValue();
                    MetabolicAge.setText(metabolicAge);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        BtnInsertData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentUserID = mAuth.getCurrentUser().getUid();
                String heartRate = HeartRate.getText().toString().trim();
                String bloodPressure = BloodPressure.getText().toString().trim();
                String metabolicAge = MetabolicAge.getText().toString().trim();
                String bloodGlucoseLvl = BloodGlucoseLvl.getText().toString().trim();
                String bloodOxygenLvl = BloodOxygenLvl.getText().toString().trim();
                String deepSleep = DeepSleep.getText().toString().trim();
                String bmi = BMI.getText().toString().trim();

//                HealthStatusHelperClass healthStatusHelperClass = new HealthStatusHelperClass(heartRate,bloodPressure,metabolicAge,bloodGlucoseLvl,
//                        bloodOxygenLvl, deepSleep, bmi);
//
//                ref.setValue(healthStatusHelperClass);

                Map<String,Object> user = new HashMap<>();
                user.put("bloodGlucoseLevel", bloodGlucoseLvl);
                user.put("bloodOxygenLevel", bloodOxygenLvl);
                user.put("bloodPressure", bloodPressure);
                user.put("bmi", bmi);
                user.put("deepSleepHours", deepSleep);
                user.put("heartRate", heartRate);
                user.put("metabolicAge", metabolicAge);

                ref.updateChildren(user);

                Intent intent = new Intent(HealthStatus.this, main.class);
                startActivity(intent);
                Toast.makeText(HealthStatus.this, "Data Saved", Toast.LENGTH_SHORT).show();

            }
        });

    }
}