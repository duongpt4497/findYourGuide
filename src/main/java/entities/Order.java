package entities;

import lombok.Data;

import java.util.Date;

@Data
public class Order {
    private long order_id;
    private long traveler_id;
    private long guider_id;
    private long post_id;
    private Date begin_date;
    private Date finish_date;
    private int adult_quantity;
    private int children_quantity;
    private long fee_paid;
    private boolean canceled;

    public Order() {
    }

    public Order(long order_id, long traveler_id, long guider_id, long post_id, Date begin_date, Date finish_date, int adult_quantity, int children_quantity, long fee_paid, boolean canceled) {
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
    }

    public Order(long guider_id,long traveler_id,long fee_paid){
        this.guider_id = guider_id;
        this.fee_paid = fee_paid;
        this.traveler_id = traveler_id;
    }

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public long getTraveler_id() {
        return traveler_id;
    }

    public void setTraveler_id(long traveler_id) {
        this.traveler_id = traveler_id;
    }

    public long getGuider_id() {
        return guider_id;
    }

    public void setGuider_id(long guider_id) {
        this.guider_id = guider_id;
    }

    public long getPost_id() {
        return post_id;
    }

    public void setPost_id(long post_id) {
        this.post_id = post_id;
    }

    public Date getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(Date begin_date) {
        this.begin_date = begin_date;
    }

    public Date getFinish_date() {
        return finish_date;
    }

    public void setFinish_date(Date finish_date) {
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

    public long getFee_paid() {
        return fee_paid;
    }

    public void setFee_paid(long fee_paid) {
        this.fee_paid = fee_paid;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
