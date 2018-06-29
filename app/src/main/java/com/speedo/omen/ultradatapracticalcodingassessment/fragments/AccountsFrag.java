package com.speedo.omen.ultradatapracticalcodingassessment.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.speedo.omen.ultradatapracticalcodingassessment.R;
import com.speedo.omen.ultradatapracticalcodingassessment.adapter.AccountsAdapter;
import com.speedo.omen.ultradatapracticalcodingassessment.db.MyRealm;
import com.speedo.omen.ultradatapracticalcodingassessment.interfaces.DataInterface;
import com.speedo.omen.ultradatapracticalcodingassessment.interfaces.DataProcess;
import com.speedo.omen.ultradatapracticalcodingassessment.models.AccountInfo;
import com.speedo.omen.ultradatapracticalcodingassessment.models.TransactionInfo;
import com.speedo.omen.ultradatapracticalcodingassessment.networkcalls.VollyCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class AccountsFrag extends Fragment implements DataProcess, DataInterface {

    final private String TAG = "AccountsFrag";
    private ImageView btn_pre;
    private ImageView btn_next;
    private TextView account_title;
    private TextView account_number;
    private TextView available_amount;
    private List<AccountInfo> acList;
    private ListView listview;
    private MyRealm myRealm;
    static int count=0;
    private VollyCall vollyCall;
    private AccountsAdapter adapter;


    public AccountsFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init(View mView){
        btn_pre = (ImageView) mView.findViewById(R.id.btn_pre);
        btn_next = (ImageView) mView.findViewById(R.id.btn_next);
        account_title = (TextView) mView.findViewById(R.id.account_title);
        account_number = (TextView) mView.findViewById(R.id.account_number);
        available_amount = (TextView) mView.findViewById(R.id.available_amount);
        listview = (ListView) mView.findViewById(R.id.listview);

        btn_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("btn_pre: ", String.valueOf(count));
                if (count > 0) {
                    --count;
                    showData(count);
                    Log.d("Account: ", String.valueOf(count));
                }
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("btn_next: ", String.valueOf(count));
                if (count < acList.size()-1) {

                    count++;
                    showData(count);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.frag_accounts, container, false);
        acList = new ArrayList<AccountInfo>();
        init(mView);
        myRealm = new MyRealm(AccountsFrag.this);
        acList = myRealm.getAll();
        vollyCall = new VollyCall(getContext(), AccountsFrag.this);
        Log.d("AccountsArg", String.valueOf(acList.size()));
        if (acList.size() != 0) {
            showData(count);
        } else {

        }
        return mView;
    }

        @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
//            vollyCall = new VollyCall(getContext(), AccountsFrag.this);
            Log.d(TAG, "Fragment is visible.");
            acList = myRealm.getAll();
            Log.d("AccountsArg", String.valueOf(acList.size()));
            if (acList.size() != 0) {
                showData(count);
            }
        } else {
            count = 0;
        }
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure() {

    }

    private void showData(int position) {
        Log.d("ac", String.valueOf(acList.size())+ ", position: "+String.valueOf(position));
        account_title.setText(acList.get(position).getAccountLabel());
        account_number.setText(acList.get(position).getAccountNumber());
        available_amount.setText("$"+acList.get(position).getAvailableBalance());
        Log.d("OnDataRe", acList.get(position).getTransactions());
        vollyCall.getDataFromServer(acList.get(position).getTransactions());
    }

    @Override
    public void onDataRetrived(String response) {
        Log.d("onDataRetrived", response);
        List<TransactionInfo> listInfo = new ArrayList<TransactionInfo>();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            if (!jsonObject.getString("transactions").isEmpty()){
                JSONArray jsonArray = jsonObject.getJSONArray("transactions");
                for (int i=0; i < jsonArray.length(); i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    TransactionInfo transInfo = new TransactionInfo();
                    transInfo.transDate = obj.getString("date");
                    transInfo.transDesc = obj.getString("description");
                    transInfo.transAmount = obj.getString("amount");

                    listInfo.add(transInfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new AccountsAdapter(listInfo, getContext());
        listview.setAdapter(adapter);
    }
}