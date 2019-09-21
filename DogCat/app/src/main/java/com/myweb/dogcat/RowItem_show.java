package com.myweb.dogcat;

public class RowItem_show {
    //public int Pet_profile;
    public String pet_name="";
    public String pet_breed="";
    public String pet_age="";
    public String pet_profile ="";


    public RowItem_show(String pet_name, String pet_breed, String pet_age, String pet_profile) {
        this.pet_name = pet_name;
        this.pet_breed = pet_breed;
        this.pet_age = pet_age;
        this.pet_profile = pet_profile;
    }


//    public int getPet_profile() {
//        return Pet_profile;
//    }

    public String getPet_profile() {
        return pet_profile;
    }

    public String getPet_name() {
        return pet_name;
    }

    public String getPet_age() {
        return pet_age;
    }

    public String getPet_breed() {
        return pet_breed;
    }

}
