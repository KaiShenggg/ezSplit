package my.edu.utar.ezsplit;

import java.io.Serializable;

public class Ower implements Serializable {

    private char firstLetterOfName;
    private String name;
    private double amount = 0;
    private int ratio = 0;
    private boolean isChecked = false;

    private static int checkedCount = 0;
    private static int totalRatio = 0;

    public Ower(String name) {
        this.firstLetterOfName = Character.toUpperCase(name.charAt(0));
        this.name = name;
    }

    public char getFirstLetterOfName() {
        return firstLetterOfName;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getRatio() {
        return ratio;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public static int getCheckedCount() {
        return checkedCount;
    }

    public static void incrementCheckedCount() {
        checkedCount++;
    }

    public static void decrementCheckedCount() {
        checkedCount--;
    }

    public static void resetCheckedCount() {
        checkedCount = 0;
    }

    public static int getTotalRatio() {
        return totalRatio;
    }

    public static void modifyTotalRatio(int ratio) {
        totalRatio += ratio;
    }

    public static void resetTotalRatio() {
        totalRatio = 0;
    }

}

