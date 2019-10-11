package entities;

import lombok.Data;

import java.util.Arrays;
import java.util.Date;

@Data
public class Traveler {

    private long traveler_id;

    private String first_name;

    private String last_name;

    private String phone;

    private String email;

    private int gender;

    private Date date_of_birth;

    private String street;

    private String house_number;

    private String postal_code;

    private String slogan;

    private String about_me;

    private String[] language;

    private String country;

    private String city;

    public Traveler() {
    }

    public Traveler(long traveler_id, String first_name, String last_name, String phone, String email, int gender, Date date_of_birth, String street, String house_number, String postal_code, String slogan, String about_me, String[] language, String country, String city) {
        this.traveler_id = traveler_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.street = street;
        this.house_number = house_number;
        this.postal_code = postal_code;
        this.slogan = slogan;
        this.about_me = about_me;
        this.language = language;
        this.country = country;
        this.city = city;
    }

    public long getTraveler_id() {
        return traveler_id;
    }

    public void setTraveler_id(long traveler_id) {
        this.traveler_id = traveler_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse_number() {
        return house_number;
    }

    public void setHouse_number(String house_number) {
        this.house_number = house_number;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getAbout_me() {
        return about_me;
    }

    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }

    public String[] getLanguage() {
        return language;
    }

    public void setLanguage(String[] language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Traveler{" +
                "traveler_id=" + traveler_id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                ", date_of_birth=" + date_of_birth +
                ", street='" + street + '\'' +
                ", house_number='" + house_number + '\'' +
                ", postal_code='" + postal_code + '\'' +
                ", slogan='" + slogan + '\'' +
                ", about_me='" + about_me + '\'' +
                ", language=" + Arrays.toString(language) +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
