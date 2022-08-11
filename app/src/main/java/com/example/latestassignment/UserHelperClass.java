package com.example.latestassignment;

public class UserHelperClass {

    String Fullname, Username, IDnum, gender, dob, PhoneNumber, email, password, ConfirmationPassword, imageUrl;

    public UserHelperClass() {
    }

    public UserHelperClass(String Fullname, String Username, String IDnum, String gender, String dob,
                             String PhoneNumber, String email, String password, String ConfirmationPassword, String imageUrl) {
        this.Fullname = Fullname;
        this.Username = Username;
        this.IDnum = IDnum;
        this.gender = gender;
        this.dob = dob;
        this.PhoneNumber = PhoneNumber;
        this.email = email;
        this.password = password;
        this.ConfirmationPassword = ConfirmationPassword;
        this.imageUrl = imageUrl;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = "Fullname";
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getIDnum() {
        return IDnum;
    }

    public void setIDnum(String IDnum) {
        this.IDnum = IDnum;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmationPassword() {
        return ConfirmationPassword;
    }

    public void setConfirmationPassword(String confirmationPassword) {
        ConfirmationPassword = confirmationPassword;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

