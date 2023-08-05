package my.edu.utar.ezsplit;

import java.util.List;

public class Expense {

    private String purpose;
    private String payer;
    private double totalAmount;
    private String breakDownMethod;
    private List<String> ower;
    private List<String> amountOwed;
    private String date;

    public Expense(String purpose, String payer, double totalAmount, String breakDownMethod, List<String> ower, List<String> amountOwed, String date) {
        this.purpose = purpose;
        this.payer = payer;
        this.totalAmount = totalAmount;
        this.breakDownMethod = breakDownMethod;
        this.ower = ower;
        this.amountOwed = amountOwed;
        this.date = date;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getPayer() {
        return payer;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getBreakDownMethod() {
        return breakDownMethod;
    }

    public List<String> getOwer() {
        return ower;
    }

    public List<String> getAmountOwed() {
        return amountOwed;
    }

    public String getDate() {
        return date;
    }

}
