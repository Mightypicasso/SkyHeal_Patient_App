package com.example.latestassignment.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.latestassignment.DoctorHelperClass;
//import com.example.latestassignment.Email.JavaMailApi;
import com.example.latestassignment.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder>{

    private Context context;
    private List<DoctorHelperClass> doctorList;

    public DoctorAdapter(Context context, List<DoctorHelperClass> doctorList) {
        this.context = context;
        this.doctorList = doctorList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.doctor_display_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final DoctorHelperClass doctorHelperClass = doctorList.get(position);

        holder.DisplayFullName.setText(doctorHelperClass.getFullname());
        holder.DisplaySpecialization.setText(doctorHelperClass.getSpecialization());
        holder.DisplayYearsOfEx.setText(doctorHelperClass.getYearsOfExperience());
        holder.DisplayEmail.setText(doctorHelperClass.getEmail());

        Glide.with(context).load(doctorHelperClass.getImageUrl()).into(holder.DisplayProfilePic);

        final String doctorId = doctorHelperClass.getUserID();
        final String name = doctorHelperClass.getFullname();

        // send email
        holder.makeAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("SEND EMAIL")
                        .setMessage("Send email to " + doctorHelperClass.getFullname() + "?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                                        .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String user_name = snapshot.child("fullname").getValue().toString();
                                        String user_IDnum = snapshot.child("idnum").getValue().toString();
                                        String user_gender = snapshot.child("gender").getValue().toString();
                                        String user_dob = snapshot.child("dob").getValue().toString();
                                        String user_email = snapshot.child("email").getValue().toString();
                                        String user_phoneNumber = snapshot.child("phoneNumber").getValue().toString();

                                        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference()
                                                .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("HealthStatus");
                                        ref1.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()){
                                                    String bloodGlucoseLevel = snapshot.child("bloodGlucoseLevel").getValue().toString();
                                                    String bloodOxygenLevel = snapshot.child("bloodOxygenLevel").getValue().toString();
                                                    String bloodPressure = snapshot.child("bloodPressure").getValue().toString();
                                                    String bmi = snapshot.child("bmi").getValue().toString();
                                                    String deepSleepHours = snapshot.child("deepSleepHours").getValue().toString();
                                                    String heartRate = snapshot.child("heartRate").getValue().toString();
                                                    String metabolicAge = snapshot.child("metabolicAge").getValue().toString();
//                                                    String docEmail = doctorHelperClass.getEmail();
                                                    String mSubject = "Appointment From Patient";
                                                    String mMessage = "Hello " + name +", " + user_name +
                                                            " would like to make an appointment with you. Here's his/her details:\n"
                                                            +"Name: " + user_name + "\n"+
                                                            "IC Number: " + user_IDnum + "\n"+
                                                            "Gender: " + user_gender + "\n"+
                                                            "Date Of Birth: " + user_dob + "\n"+
                                                            "Email Address: " + user_email + "\n"+
                                                            "Phone Number: " + user_phoneNumber + "\n\n"+
                                                            "Health Status: " + "\n"+
                                                            "Blood Glucose Level: " + bloodGlucoseLevel + "\n"+
                                                            "Blood Oxygen Level: " + bloodOxygenLevel + "\n"+
                                                            "Blood Pressure: " + bloodPressure + "\n"+
                                                            "BMI: " + bmi + "\n"+
                                                            "Deep Sleep Hours: " + deepSleepHours + "\n"+
                                                            "Heart Rate: " + heartRate + "\n"+
                                                            "Metabolic Age: " + metabolicAge + "\n\n"+
                                                            "Kindly reach out to him/her to arrange a suitable day. Thank You!\n\n"+
                                                            "Regards,\nSkyHeal";
                                                    Intent intent = new Intent(Intent.ACTION_SEND);
//                                                  intent.putExtra(Intent.EXTRA_EMAIL, (String) docEmail);
                                                    intent.putExtra(Intent.EXTRA_SUBJECT, mSubject);
                                                    intent.putExtra(Intent.EXTRA_TEXT, mMessage);

                                                    intent.setType("message/rfc822");
                                                    context.startActivity(Intent.createChooser(intent, "Choose an email client").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                                                }else {
//                                                    String docEmail = doctorHelperClass.getEmail();
                                                    String mSubject = "Appointment From Patient";
                                                    String mMessage = "Hello " + name +", " + user_name +
                                                            " would like to make an appointment with you. Here's his/her details:\n"
                                                            +"Name: " + user_name + "\n"+
                                                            "IC Number: " + user_IDnum + "\n"+
                                                            "Gender: " + user_gender + "\n"+
                                                            "Date Of Birth: " + user_dob + "\n"+
                                                            "Email Address: " + user_email + "\n"+
                                                            "Phone Number: " + user_phoneNumber + "\n\n"+
                                                            "Kindly reach out to him/her to arrange a suitable day. Thank You!\n\n"+
                                                            "Regards,\nSkyHeal";
                                                    Intent intent = new Intent(Intent.ACTION_SEND);
//                                                  intent.putExtra(Intent.EXTRA_EMAIL, (String) docEmail);
                                                    intent.putExtra(Intent.EXTRA_SUBJECT, mSubject);
                                                    intent.putExtra(Intent.EXTRA_TEXT, mMessage);

                                                    intent.setType("message/rfc822");
                                                    context.startActivity(Intent.createChooser(intent, "Choose an email client").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

//                                        (Not Working)
//                                        String mEmail = doctorHelperClass.getEmail();
//                                        String mSubject = "Appointment From Patient";
//                                        String mMessage = "Hello " + name +", " + user_name +
//                                                " would like to make an appointment with you. Here's his/her details:\n"
//                                                +"Name: " + user_name + "\n"+
//                                                "IC Number: " + user_IDnum + "\n"+
//                                                "Gender: " + user_gender + "\n"+
//                                                "Date Of Birth: " + user_dob + "\n"+
//                                                "Email Address: " + user_email + "\n"+
//                                                "Phone Number: " + user_phoneNumber + "\n\n"+
//                                                "Kindly reach out to him/her to arrange a suitable day. Thank You!\n"+
//                                                "Regards,\nSkyHeal";
//
//                                        JavaMailApi javaMailApi = new JavaMailApi(context, mEmail, mSubject, mMessage);
//                                        javaMailApi.execute();



                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView DisplayProfilePic;
        public TextView DisplayFullName, DisplaySpecialization, DisplayYearsOfEx, DisplayEmail;
        public Button makeAppointment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            DisplayProfilePic = itemView.findViewById(R.id.displayProfilePic);
            DisplayFullName = itemView.findViewById(R.id.displayFullName);
            DisplaySpecialization = itemView.findViewById(R.id.displaySpecialization);
            DisplayYearsOfEx = itemView.findViewById(R.id.displayYearsOfEx);
            DisplayEmail = itemView.findViewById(R.id.displayEmail);
            makeAppointment = itemView.findViewById(R.id.makeAppointment);

        }
    }

}
