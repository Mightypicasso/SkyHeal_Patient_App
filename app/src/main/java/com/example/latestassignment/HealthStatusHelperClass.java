package com.example.latestassignment;

public class HealthStatusHelperClass {

    String heartRate, bloodPressure,metabolicAge, bloodGlucoseLevel,bloodOxygenLevel,deepSleepHours,BMI;

    public HealthStatusHelperClass() {
    }

    public HealthStatusHelperClass(String heartRate, String bloodPressure, String metabolicAge, String bloodGlucoseLevel, String bloodOxygenLevel, String deepSleepHours, String BMI) {
        this.heartRate = heartRate;
        this.bloodPressure = bloodPressure;
        this.metabolicAge = metabolicAge;
        this.bloodGlucoseLevel = bloodGlucoseLevel;
        this.bloodOxygenLevel = bloodOxygenLevel;
        this.deepSleepHours = deepSleepHours;
        this.BMI = BMI;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getMetabolicAge() {
        return metabolicAge;
    }

    public void setMetabolicAge(String metabolicAge) {
        this.metabolicAge = metabolicAge;
    }

    public String getBloodGlucoseLevel() {
        return bloodGlucoseLevel;
    }

    public void setBloodGlucoseLevel(String bloodGlucoseLevel) {
        this.bloodGlucoseLevel = bloodGlucoseLevel;
    }

    public String getBloodOxygenLevel() {
        return bloodOxygenLevel;
    }

    public void setBloodOxygenLevel(String bloodOxygenLevel) {
        this.bloodOxygenLevel = bloodOxygenLevel;
    }

    public String getDeepSleepHours() {
        return deepSleepHours;
    }

    public void setDeepSleepHours(String deepSleepHours) {
        this.deepSleepHours = deepSleepHours;
    }

    public String getBMI() {
        return BMI;
    }

    public void setBMI(String BMI) {
        this.BMI = BMI;
    }
}
