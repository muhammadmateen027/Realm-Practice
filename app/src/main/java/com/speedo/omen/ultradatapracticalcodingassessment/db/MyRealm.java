package com.speedo.omen.ultradatapracticalcodingassessment.db;

import android.util.Log;

import com.speedo.omen.ultradatapracticalcodingassessment.interfaces.DataProcess;
import com.speedo.omen.ultradatapracticalcodingassessment.models.AccountInfo;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class MyRealm {
    private Realm realm;
    final private String TAG = "MyRealm";
    private DataProcess dataProcess;
    public MyRealm(DataProcess dataProcess) {
        realm = Realm.getDefaultInstance();
        this.dataProcess = dataProcess;
    }
//    function to store or update the data with in realm
    public void save(final AccountInfo acInfo){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                if (checkIfExists(bgRealm, acInfo.accountNumber)){
                    AccountInfo realmAccountInfo = bgRealm.where(AccountInfo.class).equalTo("accountNumber", acInfo.accountNumber).findFirst();
                    realmAccountInfo.accountBsb = acInfo.accountBsb;
                    realmAccountInfo.accountNumber = acInfo.accountNumber;
                    realmAccountInfo.accountLabel = acInfo.accountLabel;
                    realmAccountInfo.currentBalance = acInfo.currentBalance;
                    realmAccountInfo.availableBalance = acInfo.availableBalance;
                    realmAccountInfo.transactions = acInfo.transactions;
                } else {
                    AccountInfo realmAccountInfo = bgRealm.createObject(AccountInfo.class);
                    realmAccountInfo.accountNumber = acInfo.accountNumber;
                    realmAccountInfo.accountBsb = acInfo.accountBsb;
                    realmAccountInfo.accountLabel = acInfo.accountLabel;
                    realmAccountInfo.currentBalance = acInfo.currentBalance;
                    realmAccountInfo.availableBalance = acInfo.availableBalance;
                    realmAccountInfo.transactions = acInfo.transactions;
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.w(TAG,"Realm.Transaction.successful");
                dataProcess.onSuccess();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.w(TAG,"Realm transaction unsuccessful  "+ error.getMessage());
            }
        });

    }
//  method to check weather the provided account number exists or not
    private boolean checkIfExists(Realm realmM, String accountNumber){
        RealmQuery<AccountInfo> query = realmM.where(AccountInfo.class).equalTo("accountNumber", accountNumber);
        return query.count() != 0;
    }

//    method to retrive data from realm
    public List<AccountInfo> getAll(){
        RealmResults<AccountInfo> results = realm.where(AccountInfo.class).sort("accountNumber", Sort.ASCENDING).findAll();
        List<AccountInfo> retVal = new ArrayList<AccountInfo>();
        for (int i=0; i<results.size(); i++) {
            retVal.add(results.get(i));
        }
        return retVal;
    }

//    this function is currently not in use, but can be used to delete data from realm
    public void deleteData(){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.deleteAll();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
//                dataProcess.onProcessSuccess();
                Log.d(TAG,"Realm.Transaction.OnSuccess() - Deleted");

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.d(TAG,"onError(Throwable error)");
            }
        });
    }
}
