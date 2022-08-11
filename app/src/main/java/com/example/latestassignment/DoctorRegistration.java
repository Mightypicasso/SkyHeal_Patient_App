package com.example.latestassignment;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorRegistration extends AppCompatActivity {

    private TextInputEditText inputFullName, inputIDNumber,inputEmail, inputPhoneNum,inputPassword1,inputPassword2, inputSpecialization;
    private AutoCompleteTextView selectedGender, yearsOfEx;
    private ProgressDialog loader;
    private CircleImageView profilePic;

    Button dateFormat, SignUpButton;

    //datePicker
    DatePickerDialog.OnDateSetListener onDateSetListener;

    //firebase
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;

    private Uri resultUri;

    ActivityResultLauncher<Intent> startResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK){
                        Intent data = result.getData();
                        if(data !=null){
                            resultUri = data.getData();
                            profilePic.setImageURI(resultUri);
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        dateFormat = findViewById(R.id.dateFormat);

        dateFormat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        DoctorRegistration.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        onDateSetListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

            }
        });
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth+"/"+month+"/"+year;
                dateFormat.setText(date);
            }
        };

        //Gender
        String[] gender = getResources().getStringArray(R.array.Gender);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(
                this,
                R.layout.dropdown_menu, gender
        );
        selectedGender=findViewById(R.id.gender);
        selectedGender.setAdapter(adapter);

        //Years of Experience
        String[] experience = getResources().getStringArray(R.array.Experience);
        ArrayAdapter<String> adapter1=new ArrayAdapter<>(
                this,
                R.layout.dropdown_menu, experience
        );
        yearsOfEx=findViewById(R.id.yearsOfExperience);
        yearsOfEx.setAdapter(adapter1);

        //validation
        inputFullName = findViewById(R.id.inputFullName);
//        inputUsername = findViewById(R.id.inputUsername);
        inputIDNumber = findViewById(R.id.inputIDNumber);
        dateFormat = findViewById(R.id.dateFormat);
        inputEmail = findViewById(R.id.inputEmail);
        inputSpecialization = findViewById(R.id.inputSpecialization);
        inputPhoneNum = findViewById(R.id.inputPhoneNum);
        inputPassword1 = findViewById(R.id.inputPassword1);
        inputPassword2 = findViewById(R.id.inputPassword2);
        profilePic = findViewById(R.id.profile_image);
        loader = new ProgressDialog(this);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startResult.launch(intent);
            }
        });

        SignUpButton = findViewById(R.id.SignUpButton);
        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullnameInput = inputFullName.getText().toString().trim();
//                String usernameInput = inputUsername.getText().toString().trim();
                String specializationInput = inputSpecialization.getText().toString().trim();
                String emailInput = inputEmail.getText().toString().trim();
                String idNumInput = inputIDNumber.getText().toString().trim();
                String phoneNumInput = inputPhoneNum.getText().toString().trim();
                String pass1Input = inputPassword1.getText().toString().trim();
                String pass2Input = inputPassword2.getText().toString().trim();
                String sGender = selectedGender.getText().toString();
                String sExperience = yearsOfEx.getText().toString();
                String sDate = dateFormat.getText().toString();

                if (!validateFullName() | !validateEmail() | !validateIDNum() |
                        !validateSpecialization() | !validatePhoneNum() | !validatePassword1() | !validatePassword2()) {
                    return;
                }
                if (sGender.isEmpty()){
                    alert("Select Gender!");
                }
                if (sDate.isEmpty()){
                    alert("Select Date!");
                }
                if (sExperience.isEmpty()){
                    alert("Select Years of Experience!");
                }
                else {
                    loader.setMessage("Registering...");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth = FirebaseAuth.getInstance();
                    userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("doctors");

                    mAuth.createUserWithEmailAndPassword(emailInput, pass2Input).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String currentDoctorID = mAuth.getCurrentUser().getUid();
                                if (resultUri != null){
                                    StorageReference reference = FirebaseStorage.getInstance().getReference().child("ProfilePic").child(currentDoctorID+".jpg");
                                    reference.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    String imageUrl = uri.toString();
                                                    String currentDoctorID = mAuth.getCurrentUser().getUid();

                                                    DoctorHelperClass doctorHelperClass = new DoctorHelperClass(fullnameInput,idNumInput,currentDoctorID,sGender,sDate
                                                            ,specializationInput,sExperience,phoneNumInput,emailInput,pass1Input,pass2Input, imageUrl);

                                                    userDatabaseRef.child(currentDoctorID).setValue(doctorHelperClass);

                                                    loader.dismiss();
                                                }
                                            });

                                        }
                                    });
                                } else {
                                    DoctorHelperClass doctorHelperClass = new DoctorHelperClass(fullnameInput,idNumInput,currentDoctorID,sGender,sDate,specializationInput,
                                            sExperience,phoneNumInput,emailInput,pass1Input,pass2Input, "no image");

                                    userDatabaseRef.child(currentDoctorID).setValue(doctorHelperClass);

                                    loader.dismiss();
                                }

                                loader.dismiss();
                                Toast.makeText(DoctorRegistration.this, "User Created", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DoctorRegistration.this, DoctorPortal.class);
                                startActivity(intent);
                                finish();

                            }else {
                                Toast.makeText(DoctorRegistration.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
            }
        });

    }

    private void alert(String message){
        AlertDialog dlg = new AlertDialog.Builder(DoctorRegistration.this).setTitle("Message").setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dlg.show();
    }
    private boolean validateSpecialization() {
        String specialization = inputSpecialization.getText().toString().trim();

        if (specialization.isEmpty()){
            inputSpecialization.setError("Field can't be empty");
            return false;
        }
        else{
            inputSpecialization.setError(null);
            return true;
        }
    }
    private boolean validateFullName() {
        String usernameInput = inputFullName.getText().toString().trim();

        if (usernameInput.isEmpty()){
            inputFullName.setError("Field can't be empty");
            return false;
        }
        else{
            inputFullName.setError(null);
            return true;
        }
    }
//    private boolean validateUsername() {
//        String usernameInput = inputUsername.getText().toString().trim();
//        String checkspaces = "\\A\\w{1,20}\\z";
//
//        if (usernameInput.isEmpty()){
//            inputUsername.setError("Field can't be empty!");
//            return false;
//        }
//        else if (usernameInput.length()>20){
//            inputUsername.setError("Username is too long!");
//            return false;
//        }
//        else if (!usernameInput.matches(checkspaces)){
//            inputUsername.setError("No spaces allowed!");
//            return false;
//        }
//        else{
//            inputUsername.setError(null);
//            return true;
//        }
//    }
    private boolean validateEmail() {
        String emailInput = inputEmail.getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if (emailInput.isEmpty()){
            inputEmail.setError("Field can't be empty!");
            return false;
        }
        else if (!emailInput.matches(checkEmail)){
            inputEmail.setError("Invalid Email!");
            return false;
        }
        else{
            inputEmail.setError(null);
            return true;
        }
    }
    private boolean validateIDNum() {
        String idNumInput = inputIDNumber.getText().toString().trim();
        String checkspaces = "\\A\\w{1,12}\\z";

        if (idNumInput.isEmpty()){
            inputIDNumber.setError("Field can't be empty!");
            return false;
        }
        else if (idNumInput.length()!=12){
            inputIDNumber.setError("Invalid ID!");
            return false;
        }
        else if (!idNumInput.matches(checkspaces)){
            inputIDNumber.setError("No spaces allowed!");
            return false;
        }
        else{
            inputIDNumber.setError(null);
            return true;
        }
    }
    private boolean validatePhoneNum() {
        String phoneNumInput = inputPhoneNum.getText().toString().trim();
        String checkspaces = "\\A\\w{1,10}\\z";

        if (phoneNumInput.isEmpty()){
            inputPhoneNum.setError("Field can't be empty!");
            return false;
        }
        else if (phoneNumInput.length()!=10){
            inputPhoneNum.setError("Invalid Phone Number!");
            return false;
        }
        else if (!phoneNumInput.matches(checkspaces)){
            inputPhoneNum.setError("No spaces allowed!");
            return false;
        }
        else{
            inputPhoneNum.setError(null);
            return true;
        }
    }
    private boolean validatePassword1() {
        String pass1Input = inputPassword1.getText().toString().trim();

        if (pass1Input.isEmpty()){
            inputPassword1.setError("Field can't be empty!");
            return false;
        }
        else{
            inputPassword1.setError(null);
            return true;
        }
    }
    private boolean validatePassword2() {
        String pass2Input = inputPassword2.getText().toString().trim();
        String pass1Input = inputPassword1.getText().toString().trim();

        if (pass2Input.isEmpty()){
            inputPassword2.setError("Field can't be empty!");
            return false;
        }
        else if(!(pass2Input.length() == pass1Input.length())){
            inputPassword2.setError("Password Does Not Match!");
            return false;
        }
        else{
            inputPassword2.setError(null);
            return true;
        }
    }
}