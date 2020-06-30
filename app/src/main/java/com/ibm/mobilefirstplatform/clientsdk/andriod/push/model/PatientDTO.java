package com.ibm.mobilefirstplatform.clientsdk.andriod.push.model;

public class PatientDTO {
    private String gender;
    private int age;
    private int temp;
    private int respRate;
    private int pulseRate;
    private int oxySaturation;

    public PatientDTO(String gender, int age, int temp, int respRate, int pulseRate, int oxySaturation) {
        this.gender = gender;
        this.age = age;
        this.temp = temp;
        this.respRate = respRate;
        this.pulseRate = pulseRate;
        this.oxySaturation = oxySaturation;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getRespRate() {
        return respRate;
    }

    public void setRespRate(int respRate) {
        this.respRate = respRate;
    }

    public int getPulseRate() {
        return pulseRate;
    }

    public void setPulseRate(int pulseRate) {
        this.pulseRate = pulseRate;
    }

    public int getOxySaturation() {
        return oxySaturation;
    }

    public void setOxySaturation(int oxySaturation) {
        this.oxySaturation = oxySaturation;
    }
}
