package entities;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class TransactionRecord {
    private long transaction_id;
    private String payment_id;
    private String payer_id;
    private String description;
    private Timestamp date_of_transaction;
    private boolean success;

    public TransactionRecord() {
    }

    public TransactionRecord(long transaction_id, String payment_id, String payer_id, String description, Timestamp date_of_transaction, boolean success) {
        this.transaction_id = transaction_id;
        this.payment_id = payment_id;
        this.payer_id = payer_id;
        this.description = description;
        this.date_of_transaction = date_of_transaction;
        this.success = success;
    }

    public long getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(long transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getPayer_id() {
        return payer_id;
    }

    public void setPayer_id(String payer_id) {
        this.payer_id = payer_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getDate_of_transaction() {
        return date_of_transaction;
    }

    public void setDate_of_transaction(Timestamp date_of_transaction) {
        this.date_of_transaction = date_of_transaction;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
