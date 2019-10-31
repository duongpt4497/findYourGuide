package entities;

import lombok.Data;

@Data
public class Guider {
    private long guider_id;
    //private long contract_id;
    //private long account_id;
    private String first_name;
    private String last_name;
    private int age;
    private String about_me;
    private long contribution_point;
    private String city;
    private boolean active;
    private String[] languages;

    public Guider() {
    }

    public Guider(long guider_id, String first_name, String last_name, int age, String about_me, long contribution_point, String city, boolean active, String[] languages) {
        this.guider_id = guider_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.age = age;
        this.about_me = about_me;
        this.contribution_point = contribution_point;
        this.city = city;
        this.active = active;
        this.languages = languages;
    }

    public Guider(long guider_id,long contribution_point){
        this.guider_id = guider_id;
        this.contribution_point = contribution_point;
    }

    public long getGuider_id() {
        return guider_id;
    }

    public void setGuider_id(long guider_id) {
        this.guider_id = guider_id;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAbout_me() {
        return about_me;
    }

    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }

    public long getContribution_point() {
        return contribution_point;
    }

    public void setContribution_point(long contribution_point) {
        this.contribution_point = contribution_point;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String[] getLanguages() {
        return languages;
    }

    public void setLanguages(String[] languages) {
        this.languages = languages;
    }
}
