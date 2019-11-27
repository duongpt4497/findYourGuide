package entities;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Contract {
    private long contract_id;
    private String name;
    private String nationality;
    private Date date_of_birth;
    private int gender;
    private String hometown;
    private String address;
    private String identity_card_number;
    private Date card_issued_date;
    private String card_issued_province;
    private LocalDateTime account_active_date;
    private LocalDateTime account_deactive_date;

    public Contract() {
    }

    public Contract(long contract_id, String name, String nationality, Date date_of_birth, int gender, String hometown, String address, String identity_card_number, Date card_issued_date, String card_issued_province, LocalDateTime account_active_date, LocalDateTime account_deactive_date) {
        this.contract_id = contract_id;
        this.name = name;
        this.nationality = nationality;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.hometown = hometown;
        this.address = address;
        this.identity_card_number = identity_card_number;
        this.card_issued_date = card_issued_date;
        this.card_issued_province = card_issued_province;
        this.account_active_date = account_active_date;
        this.account_deactive_date = account_deactive_date;
    }

    public Contract(String name, String nationality, Date date_of_birth, int gender, String hometown, String address, String identity_card_number, Date card_issued_date, String card_issued_province, LocalDateTime account_active_date, LocalDateTime account_deactive_date) {
        this.name = name;
        this.nationality = nationality;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.hometown = hometown;
        this.address = address;
        this.identity_card_number = identity_card_number;
        this.card_issued_date = card_issued_date;
        this.card_issued_province = card_issued_province;
        this.account_active_date = account_active_date;
        this.account_deactive_date = account_deactive_date;
    }

    public long getContract_id() {
        return contract_id;
    }

    public void setContract_id(long contract_id) {
        this.contract_id = contract_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdentity_card_number() {
        return identity_card_number;
    }

    public void setIdentity_card_number(String identity_card_number) {
        this.identity_card_number = identity_card_number;
    }

    public Date getCard_issued_date() {
        return card_issued_date;
    }

    public void setCard_issued_date(Date card_issued_date) {
        this.card_issued_date = card_issued_date;
    }

    public String getCard_issued_province() {
        return card_issued_province;
    }

    public void setCard_issued_province(String card_issued_province) {
        this.card_issued_province = card_issued_province;
    }

    public LocalDateTime getAccount_active_date() {
        return account_active_date;
    }

    public void setAccount_active_date(LocalDateTime account_active_date) {
        this.account_active_date = account_active_date;
    }

    public LocalDateTime getAccount_deactive_date() {
        return account_deactive_date;
    }

    public void setAccount_deactive_date(LocalDateTime account_deactive_date) {
        this.account_deactive_date = account_deactive_date;
    }
}
