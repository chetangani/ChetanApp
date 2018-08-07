package com.chetangani.myapp.fragments.expenses;

public class GetSet_Expenses {
    public static final int MONTH_TYPE = 0;
    public static final int DATE_TYPE = 1;

    private String exp_date, exp_month, exp_year, exp_time, exp_mode, exp_credit, exp_debit, exp_description, exp_card, exp_category;
    private String day_exp_mode, day_exp_card, day_exp_category, day_exp_description, day_exp_amount;
    private int type;
    private String value;

    public GetSet_Expenses() {
    }

    public String getExp_date() {
        return exp_date;
    }

    public void setExp_date(String exp_date) {
        this.exp_date = exp_date;
    }

    public String getExp_month() {
        return exp_month;
    }

    public void setExp_month(String exp_month) {
        this.exp_month = exp_month;
    }

    public String getExp_year() {
        return exp_year;
    }

    public void setExp_year(String exp_year) {
        this.exp_year = exp_year;
    }

    public String getExp_time() {
        return exp_time;
    }

    public void setExp_time(String exp_time) {
        this.exp_time = exp_time;
    }

    public String getExp_mode() {
        return exp_mode;
    }

    public void setExp_mode(String exp_mode) {
        this.exp_mode = exp_mode;
    }

    public String getExp_credit() {
        return exp_credit;
    }

    public void setExp_credit(String exp_credit) {
        this.exp_credit = exp_credit;
    }

    public String getExp_debit() {
        return exp_debit;
    }

    public void setExp_debit(String exp_debit) {
        this.exp_debit = exp_debit;
    }

    public String getExp_description() {
        return exp_description;
    }

    public void setExp_description(String exp_description) {
        this.exp_description = exp_description;
    }

    public String getExp_card() {
        return exp_card;
    }

    public void setExp_card(String exp_card) {
        this.exp_card = exp_card;
    }

    public String getExp_category() {
        return exp_category;
    }

    public void setExp_category(String exp_category) {
        this.exp_category = exp_category;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDay_exp_mode() {
        return day_exp_mode;
    }

    public void setDay_exp_mode(String day_exp_mode) {
        this.day_exp_mode = day_exp_mode;
    }

    public String getDay_exp_card() {
        return day_exp_card;
    }

    public void setDay_exp_card(String day_exp_card) {
        this.day_exp_card = day_exp_card;
    }

    public String getDay_exp_category() {
        return day_exp_category;
    }

    public void setDay_exp_category(String day_exp_category) {
        this.day_exp_category = day_exp_category;
    }

    public String getDay_exp_description() {
        return day_exp_description;
    }

    public void setDay_exp_description(String day_exp_description) {
        this.day_exp_description = day_exp_description;
    }

    public String getDay_exp_amount() {
        return day_exp_amount;
    }

    public void setDay_exp_amount(String day_exp_amount) {
        this.day_exp_amount = day_exp_amount;
    }
}
