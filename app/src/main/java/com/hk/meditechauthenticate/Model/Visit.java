package com.hk.meditechauthenticate.Model;

public class Visit {
    String patientId, name, address,image;

    public Visit() {
    }

    public Visit(String patientId, String name, String address,String image) {
        this.patientId = patientId;
        this.name = name;
        this.address = address;
        this.image = image;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
