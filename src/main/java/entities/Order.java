package entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Order {
    private int order_id;
    private int traveler_id;
    private int guider_id;
    private int post_id;

    @JsonFormat(pattern = "MM/dd/yyyy HH:mm")
    private LocalDateTime begin_date;

    @JsonFormat(pattern = "MM/dd/yyyy HH:mm")
    private LocalDateTime finish_date;

    private int adult_quantity;
    private int children_quantity;
    private double fee_paid;
    private boolean canceled;
    private String transaction_id;
    private boolean status;

    public Order() {
    }

    public Order(int order_id, int traveler_id, int guider_id, int post_id, LocalDateTime begin_date, LocalDateTime finish_date, int adult_quantity, int children_quantity, double fee_paid, boolean canceled, String transaction_id, boolean status) {
        this.order_id = order_id;
        this.traveler_id = traveler_id;
        this.guider_id = guider_id;
        this.post_id = post_id;
        this.begin_date = begin_date;
        this.finish_date = finish_date;
        this.adult_quantity = adult_quantity;
        this.children_quantity = children_quantity;
        this.fee_paid = fee_paid;
        this.canceled = canceled;
        this.transaction_id = transaction_id;
        this.status = status;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getTraveler_id() {
        return traveler_id;
    }

    public void setTraveler_id(int traveler_id) {
        this.traveler_id = traveler_id;
    }

    public int getGuider_id() {
        return guider_id;
    }

    public void setGuider_id(int guider_id) {
        this.guider_id = guider_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public LocalDateTime getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(LocalDateTime begin_date) {
        this.begin_date = begin_date;
    }

    public LocalDateTime getFinish_date() {
        return finish_date;
    }

    public void setFinish_date(LocalDateTime finish_date) {
        this.finish_date = finish_date;
    }

    public int getAdult_quantity() {
        return adult_quantity;
    }

    public void setAdult_quantity(int adult_quantity) {
        this.adult_quantity = adult_quantity;
    }

    public int getChildren_quantity() {
        return children_quantity;
    }

    public void setChildren_quantity(int children_quantity) {
        this.children_quantity = children_quantity;
    }

    public double getFee_paid() {
        return fee_paid;
    }

    public void setFee_paid(double fee_paid) {
        this.fee_paid = fee_paid;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
