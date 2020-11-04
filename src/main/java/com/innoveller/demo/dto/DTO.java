package com.innoveller.demo.dto;

public class DTO {
    private double amount;
    private String transactionType;
    public  Long bankAccId;
    private String status;

    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Long getBankAccId() {
        return bankAccId;
    }

    public void setBankAccId(Long bankAccId) {
        this.bankAccId = bankAccId;
    }

    @Override
    public String toString() {
        return "DTO{" +
                "amount=" + amount +
                ", transactionType='" + transactionType + '\'' +
                ", bankAccId=" + bankAccId +
                ", status='" + status + '\'' +
                '}';
    }
}
