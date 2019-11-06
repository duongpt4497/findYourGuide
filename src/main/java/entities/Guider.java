package entities;

import lombok.Data;

@Data
public class Guider {
    private long guider_id;
    //private long contract_id;
    private String first_name;
    private String last_name;
    private int age;
    private String about_me;
    private long contribution;
    private String city;
    private String[] languages;
    private boolean active;
    private long rated;
    private String avatar;
    private String passion;

    public Guider() {
    }

    public Guider(long guider_id, String first_name, String last_name, int age, String about_me, long contribution, String city, String[] languages, boolean active, long rated, String avatar, String passion) {
        this.guider_id = guider_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.age = age;
        this.about_me = about_me;
        this.contribution = contribution;
        this.city = city;
        this.languages = languages;
        this.active = active;
        this.rated = rated;
        this.avatar = avatar;
        this.passion = passion;
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

    public long getContribution() {
        return contribution;
    }

    public void setContribution(long contribution) {
        this.contribution = contribution;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String[] getLanguages() {
        return languages;
    }

    public void setLanguages(String[] languages) {
        this.languages = languages;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getRated() {
        return rated;
    }

    public void setRated(long rated) {
        this.rated = rated;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassion() {
        return passion;
    }

    public void setPassion(String passion) {
        this.passion = passion;
    }
}
