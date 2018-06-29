package com.speedo.omen.ultradatapracticalcodingassessment.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class AccountInfo extends RealmObject {
    public String accountNumber;
    public String accountBsb;
    public String accountLabel;
    public String currentBalance;
    public String availableBalance;
    public String transactions;

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountBsb() {
        return accountBsb;
    }

    public String getAccountLabel() {
        return accountLabel;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public String getAvailableBalance() {
        return availableBalance;
    }

    public String getTransactions() {
        return transactions;
    }

}
