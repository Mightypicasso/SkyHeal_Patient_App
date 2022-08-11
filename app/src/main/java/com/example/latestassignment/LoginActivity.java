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
import android.graphics.Bitmap;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {

    //validation
    private TextInputEditText inputFullName, inputUsername, inputIDNumber,inputEmail, inputPhoneNum,inputPassword1,inputPassword2
            ,loginEmail, loginPassword;
    private AutoCompleteTextView selectedGender;
    private ProgressDialog loader;
    private CircleImageView profilePic;
    
    Button dateFormat,logInButton, SignUpButton;
    private TextView signUp, login, forgetPas;
    LinearLayout signUpLayout, loginLayout;

    private Uri resultUri;

    //datePicker
    DatePickerDialog.OnDateSetListener onDateSetListener;

    //firebase
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;
//    private FirebaseAuth.AuthStateListener authStateListener;

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
        setContentView(R.layout.activity_login);

        //datePicker
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        dateFormat = findViewById(R.id.dateFormat);

        dateFormat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        LoginActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
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

        //Login & Sign Up
        signUp = findViewById(R.id.signUp);
        login = findViewById(R.id.login);
        signUpLayout = findViewById(R.id.signUpLayout);
        loginLayout = findViewById(R.id.loginLayout);
        logInButton = findViewById(R.id.logInButton);

        //Sign Up
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp.setBackground(getResources().getDrawable(R.drawable.switch_trcks,null));
                signUp.setTextColor(getResources().getColor(R.color.black));
                login.setBackground(null);
                signUpLayout.setVisibility(View.VISIBLE);
                loginLayout.setVisibility(View.GONE);
                login.setTextColor(getResources().getColor(R.color.black));
            }
        });

        //Login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp.setBackground(null);
                signUp.setTextColor(getResources().getColor(R.color.black));
                login.setBackground(getResources().getDrawable(R.drawable.switch_trcks,null));
                signUpLayout.setVisibility(View.GONE);
                loginLayout.setVisibility(View.VISIBLE);
                login.setTextColor(getResources().getColor(R.color.black));
            }
        });

        //LogInButton
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        forgetPas = findViewById(R.id.forgetPas);
        mAuth = FirebaseAuth.getInstance();

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginEmailInput = loginEmail.getText().toString().trim();
                String loginPasInput = loginPassword.getText().toString().trim();

                if (!validateUsernameEmail() | !validateLoginPas()){
                    return;
                } else if (loginEmail.getText().toString().equals("admin@gmail.com") && loginPassword.getText().toString().equals("admin123")){
                    Intent intent = new Intent(LoginActivity.this, DoctorPortal.class);
                    startActivity(intent);
                    finish();

                } else{
                    mAuth.signInWithEmailAndPassword(loginEmailInput,loginPasInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(LoginActivity.this, main.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        //validation
        inputFullName = findViewById(R.id.inputFullName);
        inputUsername = findViewById(R.id.inputUsername);
        inputIDNumber = findViewById(R.id.inputIDNumber);
        dateFormat = findViewById(R.id.dateFormat);
        inputEmail = findViewById(R.id.inputEmail);
        inputPhoneNum = findViewById(R.id.inputPhoneNum);
        inputPassword1 = findViewById(R.id.inputPassword1);
        inputPassword2 = findViewById(R.id.inputPassword2);
        profilePic = findViewById(R.id.profile_image);
        loader = new ProgressDialog(this);

        // select profile picture
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startResult.launch(intent);
            }
        });


//        if (mAuth.getCurrentUser() != null){
//            startActivity(new Intent(getApplicationContext(), sample.class));
//            finish();
//        }

        SignUpButton = findViewById(R.id.SignUpButton);
        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullnameInput = inputFullName.getText().toString().trim();
                String usernameInput = inputUsername.getText().toString().trim();
                String emailInput = inputEmail.getText().toString().trim();
                String idNumInput = inputIDNumber.getText().toString().trim();
                String phoneNumInput = inputPhoneNum.getText().toString().trim();
                String pass1Input = inputPassword1.getText().toString().trim();
                String pass2Input = inputPassword2.getText().toString().trim();
                String sGender = selectedGender.getText().toString();
                String sDate = dateFormat.getText().toString();

                if (!validateFullName() | !validateEmail() | !validateUsername() | !validateIDNum() |
                        !validatePhoneNum() | !validatePassword1() | !validatePassword2()) {
                    return;
                }
                if (sGender.isEmpty()){
                    alert("Select Gender!");
                }
                if (sDate.isEmpty()){
                    alert("Select Date!");
                }
                else {
                    loader.setMessage("Registering...");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth = FirebaseAuth.getInstance();
                    userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users");

                    mAuth.createUserWithEmailAndPassword(emailInput, pass2Input).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String currentUserID = mAuth.getCurrentUser().getUid();

                                if (resultUri != null){
                                    StorageReference reference = FirebaseStorage.getInstance().getReference().child("ProfilePic").child(currentUserID+".jpg");
                                    reference.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    String imageUrl = uri.toString();
                                                    String currentUserID = mAuth.getCurrentUser().getUid();

                                                    UserHelperClass userHelperClass = new UserHelperClass(fullnameInput,usernameInput,idNumInput,sGender,sDate
                                                            ,phoneNumInput,emailInput,pass1Input,pass2Input, imageUrl);

                                                    userDatabaseRef.child(currentUserID).setValue(userHelperClass);

                                                    loader.dismiss();
                                                }
                                            });

                                        }
                                    });
                                } else {
                                    UserHelperClass userHelperClass = new UserHelperClass(fullnameInput,usernameInput,idNumInput,sGender,sDate
                                            ,phoneNumInput,emailInput,pass1Input,pass2Input, "no image");

                                    userDatabaseRef.child(currentUserID).setValue(userHelperClass);

                                    loader.dismiss();
                                }

                                startActivity(getIntent());
                                loader.dismiss();
                                Toast.makeText(LoginActivity.this, "User Created", Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(LoginActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

//                    (had some error while uploading photo)
//                    mAuth.createUserWithEmailAndPassword(emailInput, pass2Input).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if(task.isSuccessful()){
//                                String currentUserID = mAuth.getCurrentUser().getUid();
//                                userDatabaseRef = FirebaseDatabase.getInstance().getReference()
//                                                .child("users").child(currentUserID);
//
//                                Map<String,Object> user = new HashMap<>();
//
////                                HashMap userInfo = new HashMap();
//                                user.put("ID", currentUserID);
//                                user.put("Fullname", fullnameInput);
//                                user.put("Username", usernameInput);
//                                user.put("ID Number", idNumInput);
//                                user.put("Gender", sGender);
//                                user.put("Date Of Birth", sDate);
//                                user.put("Phone Number", phoneNumInput);
//                                user.put("Email", emailInput);
//                                user.put("Password", pass1Input);
//                                user.put("Confirm Password", pass2Input);
//
//                                userDatabaseRef.updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
//                                    @Override
//                                    public void onComplete(@NonNull Task task) {
//                                        if (task.isSuccessful()){
//                                            Toast.makeText(LoginActivity.this, "User Created", Toast.LENGTH_SHORT).show();
//                                        }else {
//                                            Toast.makeText(LoginActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                        }
//
//                                        finish();
//                                        startActivity(getIntent());
//                                        loader.dismiss();
//                                    }
//                                });
//
//                            }else{
//                                Toast.makeText(LoginActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                loader.dismiss();
//                            }
//                        }
//                    });

                }
            }
        });

    }

    //system back button
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginActivity.this, WelcomePage.class);
        startActivity(intent);
    }

    //validation
    private void alert(String message){
        AlertDialog dlg = new AlertDialog.Builder(LoginActivity.this).setTitle("Message").setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dlg.show();
    }
    private boolean validateUsernameEmail() {
        String inputEmail = loginEmail.getText().toString().trim();

        if (inputEmail.isEmpty()){
            loginEmail.setError("Username or Email is Required");
            return false;
        }
        else{
            loginEmail.setError(null);
            return true;
        }
    }
    private boolean validateLoginPas() {
        String inputLoginPas = loginPassword.getText().toString().trim();

        if (inputLoginPas.isEmpty()){
            loginPassword.setError("Password is Required");
            return false;
        }
        else{
            loginPassword.setError(null);
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
    private boolean validateUsername() {
        String usernameInput = inputUsername.getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";

        if (usernameInput.isEmpty()){
            inputUsername.setError("Field can't be empty!");
            return false;
        }
        else if (usernameInput.length()>20){
            inputUsername.setError("Username is too long!");
            return false;
        }
        else if (!usernameInput.matches(checkspaces)){
            inputUsername.setError("No spaces allowed!");
            return false;
        }
        else{
            inputUsername.setError(null);
            return true;
        }
    }
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