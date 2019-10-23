/**
 * Assessment purpose:  Record cow breed into a database
 * Assessment number:   2
 * Unit Coordinator:    Ron Balsys
 * Lecture & Tutor:     Partha Gangavalli
 * File name:           CowLogs.java
 * File purpose:        CowLogs class to create object of a cow.
 */

package com.jljim.cowlogs;

public class CowLogs {

    // six attributes for the CowLogs class and the 2 attributes of longitude and latitude
    private String breed;
    private String id;
    private String date;
    private String weight;
    private String age;
    private String condition;
    private String longitude;
    private String latitude;


    // default constructor
    public CowLogs(){
        this.breed = "";
        this.id = "";
        this.date = "";
        this.weight = "";
        this.age = "";
        this.condition = "";
        this.longitude = "";
        this.latitude = "";
    }

    //parametrised constructor
    public CowLogs(String breed, String id, String date, String weight, String age, String condition, String longitude, String latitude) {
        this.breed = breed;
        this.id = id;
        this.date = date;
        this.weight = weight;
        this.age = age;
        this.condition = condition;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    // Get methods of the eight attributes of CowLogs class
    public String getBreed() {
        return this.breed;
    }
    public String getId(){
        return this.id;
    }
    public String getDate(){
        return this.date;
    }
    public String getWeight(){
        return this.weight;
    }
    public String getAge(){
        return this.age;
    }
    public String getCondition(){
        return this.condition;
    }
    public String getLongitude(){
        return this.longitude;
    }
    public String getLatitude(){
        return this.latitude;
    }

    //Set methods of the eight attributes of CowLogs class
    public void setBreed (String breed) {
        this.breed = breed;
    }
    public void setId(String id){
        this.id = id;
    }
    public void setDate(String date){
        this.date = date;
    }
    public void setWeight(String weight){
        this.weight = weight;
    }
    public void setAge(String age){
        this.age = age;
    }
    public void setCondition(String condition){
        this.condition = condition;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    //Overwrite the to String method to print the object as a string
    @Override
    public String toString(){
        return condition+" "+date+" "+longitude+" "+latitude+" "+id+" "+weight+" "+age;
    }

}
