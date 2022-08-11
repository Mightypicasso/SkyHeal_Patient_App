package com.example.latestassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomePage extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    ImageButton backBtn;
    TextView adKey;
    EditText inputAdKey;
    Button patientReg, continueBtn;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        patientReg = findViewById(R.id.patientReg);
//        doctorReg = findViewById(R.id.doctorReg);
        backBtn = findViewById(R.id.backBtn);
        adKey = findViewById(R.id.adKey);
        inputAdKey = findViewById(R.id.inputAdKey);
        continueBtn = findViewById(R.id.continueBtn);
        mAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null){
                    Intent intent = new Intent(WelcomePage.this, main.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        patientReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomePage.this, LoginActivity.class);
                startActivity(intent);
            }
        });

//        doctorReg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                createNewContactDialog();
//            }
//        });

    }

//    public void createNewContactDialog() {
//        dialogBuilder = new AlertDialog.Builder(this);
//        final View contactPopUpView = getLayoutInflater().inflate(R.layout.pop_up, null);
//        backBtn = (ImageButton) contactPopUpView.findViewById(R.id.backBtn);
//        adKey = (TextView) contactPopUpView.findViewById(R.id.adKey);
//        inputAdKey = (EditText) contactPopUpView.findViewById(R.id.inputAdKey);
//        continueBtn = (Button) contactPopUpView.findViewById(R.id.continueBtn);
//
//        dialogBuilder.setView(contactPopUpView);
//        dialog = dialogBuilder.create();
//        dialog.show();
//
//        continueBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (inputAdKey.getText().toString().equals("admin123")){
//                    Intent intent = new Intent(WelcomePage.this, DoctorRegistration.class);
//                    startActivity(intent);
//                }
//                else{
//                    Toast.makeText(WelcomePage.this, "Wrong Admin Key", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        backBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//    }

    //system back button
    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }

}