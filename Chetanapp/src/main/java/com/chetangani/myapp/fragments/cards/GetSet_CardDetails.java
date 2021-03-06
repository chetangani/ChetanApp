package com.chetangani.myapp.fragments.cards;

import java.io.Serializable;

public class GetSet_CardDetails implements Serializable {
    private String card_type, card_number, card_expiry, card_cvv, card_name, bank_code, login_id, login_pass, trans_pass, card_id;

    public GetSet_CardDetails(String card_id, String card_type, String card_number, String card_expiry, String card_cvv, String card_name,
                              String bank_code, String login_id, String login_pass, String trans_pass) {
        this.card_id = card_id;
        this.card_type = card_type;
        this.card_number = card_number;
        this.card_expiry = card_expiry;
        this.card_cvv = card_cvv;
        this.card_name = card_name;
        this.bank_code = bank_code;
        this.login_id = login_id;
        this.login_pass = login_pass;
        this.trans_pass = trans_pass;
    }

    public String getCard_id() {
        return card_id;
    }

    public String getCard_type() {
        return card_type;
    }

    public String getCard_number() {
        return card_number;
    }

    public String getCard_expiry() {
        return card_expiry;
    }

    public String getCard_cvv() {
        return card_cvv;
    }

    public String getCard_name() {
        return card_name;
    }

    public String getBank_code() {
        return bank_code;
    }

    public String getLogin_id() {
        return login_id;
    }

    public String getLogin_pass() {
        return login_pass;
    }

    public String getTrans_pass() {
        return trans_pass;
    }
}
