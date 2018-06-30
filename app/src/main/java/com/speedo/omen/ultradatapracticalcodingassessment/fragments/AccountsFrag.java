package com.speedo.omen.ultradatapracticalcodingassessment.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

//class to get and show values in a listview
public class AccountsFrag extends Fragment implements DataProcess, DataInterface {

    final private String TAG = "AccountsFrag";
    private ImageView btn_pre;
    private ImageView btn_next;
    private TextView account_title;
    private TextView account_number;
    private TextView available_amount;
    private List<AccountInfo> acList;
    private ListView listview;
    private LinearLayout tv_lv;
    private LinearLayout data_lv;

    private MyRealm myRealm;
    static int count=0;
    private VollyCall vollyCall;
    private AccountsAdapter adapter;
    private SharedPreferences sharedPref;

    public AccountsFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = getActivity().getPreferences(getContext().MODE_PRIVATE);
    }

//    a function is created so that all the views can be initialized
    private void init(View mView){
        btn_pre = (ImageView) mView.findViewById(R.id.btn_pre);
        btn_next = (ImageView) mView.findViewById(R.id.btn_next);
        account_title = (TextView) mView.findViewById(R.id.account_title);
        account_number = (TextView) mView.findViewById(R.id.account_number);
        available_amount = (TextView) mView.findViewById(R.id.available_amount);
        listview = (ListView) mView.findViewById(R.id.listview);

        tv_lv = (LinearLayout) mView.findViewById(R.id.one);
        data_lv = (LinearLayout) mView.findViewById(R.id.top);

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
//        if realm have data then call to show or populate on the screen
        if (acList.size() != 0) {
            showData(count);
        }
        return mView;
    }

//    if you are in current fragment then this method called, and it retrive data from realm
        @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            int mIndex = sharedPref.getInt(getString(R.string.m_index), 0);
            count = mIndex;
            Log.d(TAG, "Fragment is visible.");
            acList = myRealm.getAll();
            Log.d("AccountsArg", String.valueOf(acList.size()));
            if (acList.size() != 0) {
                showData(count);
                tv_lv.setVisibility(View.GONE);
                data_lv.setVisibility(View.VISIBLE);
            } else {
                tv_lv.setVisibility(View.VISIBLE);
                data_lv.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure() {

    }

//    this method is used to show or populate the data on screen w.r.t position
    private void showData(int position) {
        account_title.setText(acList.get(position).getAccountLabel());
        account_number.setText(acList.get(position).getAccountNumber());
        available_amount.setText("$"+acList.get(position).getAvailableBalance());
        vollyCall.getDataFromServer(acList.get(position).getTransactions());
    }

//    on volly call, this method proved the data from network call
//    and to populate the account info
    @Override
    public void onDataRetrived(String response) {
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

//        populate the listview
        adapter = new AccountsAdapter(listInfo, getContext());
        listview.setAdapter(adapter);
    }

    @Override
    public void onNetworkFail() {

    }
}