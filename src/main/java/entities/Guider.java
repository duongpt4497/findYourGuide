package entities;

import lombok.Data;

import java.sql.Array;

@Data
public class Guider {
    private long guider_id;
    private long contract_id;
    private long account_id;
    private String first_name;
    private String last_name;
    private int age;
    private String about_me;
    private long contribution_point;
    private String city;
    private String[] available_langauge;

    public Guider(long guider_id, long contract_id, long account_id, String first_name, String last_name, int age, String about_me, long contribution_point, String city, String[] available_langauge) {
        this.guider_id = guider_id;
        this.contract_id = contract_id;
        this.account_id = account_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.age = age;
        this.about_me = about_me;
        this.contribution_point = contribution_point;
        this.city = city;
        this.available_langauge = available_langauge;
    }

    public long getGuider_id() {
        return guider_id;
    }

    public long getContract_id() {
        return contract_id;
    }

    public long getAccount_id() {
        return account_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public int getAge() {
        return age;
    }

    public String getAbout_me() {
        return about_me;
    }

    public long getContribution_point() {
        return contribution_point;
    }

    public String getCity() {
        return city;
    }

    public String[] getAvailable_langauge() {
        return available_langauge;
    }
}
