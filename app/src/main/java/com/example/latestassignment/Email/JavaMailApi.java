//======================(Not using Anymore)======================

//package com.example.latestassignment.Email;
//
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.os.AsyncTask;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//
//import com.example.latestassignment.R;
//
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//
//import java.util.Properties;
//
//public class JavaMailApi extends AsyncTask<Void, Void, Void> {
//
//    private Context context;
//    private Session session;
//    private String email, subject, message;
//
//    public JavaMailApi(Context context, String email, String subject, String message) {
//        this.context = context;
//        this.email = email;
//        this.subject = subject;
//        this.message = message;
//    }
//
//    ProgressDialog progressDialog;
//
//    @Override
//    protected void onPreExecute() {
//        progressDialog = new ProgressDialog(context);
//        progressDialog.setMessage("Sending email...");
//        progressDialog.setTitle("Sending Email to Doctor");
//        progressDialog.show();
//        super.onPreExecute();
//    }
//
//    @Override
//    protected Void doInBackground(Void... voids) {
//        Properties properties = new Properties();
//        properties.put("mail.smtp.host", "smtp.gmail.com");
//        properties.put("mail.smtp.socketFactory.port", "465");
//        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//        properties.put("mail.smtp.auth", "true");
//        properties.put("mail.smtp.port", "465");
//
//        session = Session.getDefaultInstance(properties, new javax.mail.Authenticator(){
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(Util.Email, Util.PASSWORD);
//            }
//        });
//
//        MimeMessage mimeMessage = new MimeMessage(session);
//
//        try {
//            mimeMessage.setFrom(new InternetAddress(Util.Email));
//            mimeMessage.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(email)));
//            mimeMessage.setSubject(subject);
//            mimeMessage.setText(message);
//            Transport.send(mimeMessage);
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(Void unused) {
//        progressDialog.dismiss();
//        startAlertDialog();
//        super.onPostExecute(unused);
//    }
//
//    private void startAlertDialog() {
//        AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View myView = inflater.inflate(R.layout.email_sent_layout, null);
//        myDialog.setView(myView);
//
//        final AlertDialog dialog = myDialog.create();
//        dialog.setCancelable(false);
//
//        Button closeButton = myView.findViewById(R.id.close_Btn);
//        closeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
//
//    }
//
//
//}
