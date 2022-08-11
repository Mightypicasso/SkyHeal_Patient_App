package com.example.latestassignment;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditPerInfo extends AppCompatActivity {

    private CircleImageView profilePic;
    private TextInputEditText  editFullName, editUserName, editIdNum, editGender, editDateFormat, editemail, editphoneNum;
    private Button saveBtn;
    ImageButton closeBtn;

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
        setContentView(R.layout.activity_edit_per_info);

        closeBtn = findViewById(R.id.closeBtn);
        profilePic = findViewById(R.id.profile_image);
        saveBtn = findViewById(R.id.saveChanges);
        editFullName = findViewById(R.id.editFullName);
        editUserName = findViewById(R.id.editUserName);
        editIdNum = findViewById(R.id.editIdNum);
        editGender = findViewById(R.id.editGender);
        editDateFormat = findViewById(R.id.editDateFormat);
        editemail = findViewById(R.id.email);
        editphoneNum = findViewById(R.id.phoneNum);

        mAuth = FirebaseAuth.getInstance();

        userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditPerInfo.this, UserSetting.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startResult.launch(intent);
            }
        });

        userDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String name = (String) snapshot.child("fullname").getValue();
                    editFullName.setText(name);

                    String username = (String)snapshot.child("username").getValue();
                    editUserName.setText(username);

                    String idNum = (String)snapshot.child("idnum").getValue();
                    editIdNum.setText(idNum);

                    String gender = (String)snapshot.child("gender").getValue();
                    editGender.setText(gender);

                    String dob = (String)snapshot.child("dob").getValue();
                    editDateFormat.setText(dob);

                    String email = (String)snapshot.child("email").getValue();
                    editemail.setText(email);

                    String phoneNum = (String)snapshot.child("phoneNumber").getValue();
                    editphoneNum.setText(phoneNum);

                    String imageUrl = (String)snapshot.child("imageUrl").getValue();
                    Glide.with(getApplicationContext()).load(imageUrl).into(profilePic);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfilePic();
                String usernameEdit = editUserName.getText().toString().trim();
                String emailEdit = editemail.getText().toString().trim();
                String phoneNumEdit = editphoneNum.getText().toString().trim();
                String currentUserID = mAuth.getCurrentUser().getUid();

                Map<String,Object> user = new HashMap<>();
                user.put("username", usernameEdit);
                user.put("email", emailEdit);
                user.put("phoneNumber", phoneNumEdit);

                userDatabaseRef.child(currentUserID).updateChildren(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        startActivity(getIntent());
                        Toast.makeText(EditPerInfo.this, "Data saved", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        startActivity(getIntent());
                        Toast.makeText(EditPerInfo.this, "Error, cant save data", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void uploadProfilePic() {
        mAuth = FirebaseAuth.getInstance();
        userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users");
        String currentUserID = mAuth.getCurrentUser().getUid();
        if (resultUri != null){
            StorageReference reference = FirebaseStorage.getInstance().getReference().child("ProfilePic").child(".jpg");
            reference.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();
                            String currentUserID = mAuth.getCurrentUser().getUid();

                            Map<String,Object> user = new HashMap<>();
                            user.put("ProfileImage", imageUrl);

                            userDatabaseRef.child(currentUserID).updateChildren(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(EditPerInfo.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EditPerInfo.this, "Not Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditPerInfo.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
        }

//        **(had error)**
//        if(resultUri != null){
////            FirebaseUser user = mAuth.getCurrentUser();
////            String currentUserId = user.getUid();
//            DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("users");
//            StorageReference filepath = FirebaseStorage.getInstance().getReference().child("ProfilePic");
//            final StorageReference image = filepath.child(".jpg");
//
//            Bitmap bitmap = null;
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
//            byte[] data = byteArrayOutputStream.toByteArray();
//            UploadTask uploadTask = image.putBytes(data);
//
//            uploadTask.addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(EditPerInfo.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    if (taskSnapshot.getMetadata() != null && taskSnapshot.getMetadata().getReference() != null){
//                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
//                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                String downloadUrl = uri.toString();
//
//                                HashMap<String, Object> hashMap=new HashMap<>();
//                                hashMap.put("ProfileImage", downloadUrl);
//
//                                UsersRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()){
//                                            Toast.makeText(EditPerInfo.this, "Image Successfully Added", Toast.LENGTH_SHORT).show();
//                                        }
//                                        else{
//                                            Toast.makeText(EditPerInfo.this, "Error", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//
//                            }
//                        });
//                    }
//                }
//            });
//
//        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}