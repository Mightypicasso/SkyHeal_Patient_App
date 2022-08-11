package com.example.latestassignment;

public class DoctorHelperClass {

    String fullname, IDnum, userID, gender, dob, specialization, yearsOfExperience, PhoneNumber, email, password, ConfirmationPassword, imageUrl;

    public DoctorHelperClass() {
    }

    public DoctorHelperClass(String fullname, String IDnum, String userID, String gender, String dob, String specialization, String yearsOfExperience,
                             String PhoneNumber, String email, String password, String ConfirmationPassword, String imageUrl) {
        this.fullname = fullname;
        this.IDnum = IDnum;
        this.userID = userID;
        this.gender = gender;
        this.dob = dob;
        this.specialization = specialization;
        this.yearsOfExperience = yearsOfExperience;
        this.PhoneNumber = PhoneNumber;
        this.email = email;
        this.password = password;
        this.ConfirmationPassword = ConfirmationPassword;
        this.imageUrl = imageUrl;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getIDnum() {
        return IDnum;
    }

    public void setIDnum(String IDnum) {
        this.IDnum = IDnum;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
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

    public String getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(String yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
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
