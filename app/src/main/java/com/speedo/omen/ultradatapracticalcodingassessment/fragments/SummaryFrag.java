package com.speedo.omen.ultradatapracticalcodingassessment.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.speedo.omen.ultradatapracticalcodingassessment.R;
import com.speedo.omen.ultradatapracticalcodingassessment.adapter.SummaryAdapter;
import com.speedo.omen.ultradatapracticalcodingassessment.interfaces.DataInterface;
import com.speedo.omen.ultradatapracticalcodingassessment.interfaces.DataProcess;
import com.speedo.omen.ultradatapracticalcodingassessment.models.AccountInfo;
import com.speedo.omen.ultradatapracticalcodingassessment.networkcalls.VollyCall;
import com.speedo.omen.ultradatapracticalcodingassessment.db.MyRealm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SummaryFrag extends Fragment implements DataInterface, DataProcess {

    final private String mUrl = "http://www.mocky.io/v2/5abb1042350000580073a7ea";
    final private String TAG = "SummaryFrag";
    private VollyCall vCall;
    private MyRealm myRealm;

    private RecyclerView recyclerView;
    private SummaryAdapter summaryAdapter;

    public SummaryFrag() {
        // Required empty public constructor
        Log.d(TAG, "SummaryFrag()");
    }

    private void init(View mView) {
        recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle savedInstanceState)");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        // Inflate the layout for this fragment
            myRealm = new MyRealm(SummaryFrag.this);
            vCall = new VollyCall(getContext(), SummaryFrag.this);
            vCall.getDataFromServer(mUrl);

        View mView =  inflater.inflate(R.layout.fragment_summary, container, false);
        init(mView);
//        List<AccountInfo> list = myRealm.getAll();
        return mView;
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            Log.d(TAG, "Fragment is visible.");
////            myRealm = new MyRealm(SummaryFrag.this);
////            vCall = new VollyCall(getContext(), SummaryFrag.this);
////            vCall.getDataFromServer(mUrl);
//        }
//        else
//            Log.d(TAG, "Fragment is not visible.");
//    }

    @Override
    public void onDataRetrived(String response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            if (!jsonObject.getString("accounts").isEmpty()){
                JSONArray jsonArray = jsonObject.getJSONArray("accounts");
                for (int i=0; i < jsonArray.length(); i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    AccountInfo ac = new AccountInfo();
                    ac.accountNumber = obj.getString("accountNumber");
                    ac.accountBsb = obj.getString("accountBsb");
                    ac.accountLabel = obj.getString("accountLabel");
                    ac.currentBalance = obj.getString("currentBalance");
                    ac.availableBalance = obj.getString("availableBalance");
                    ac.transactions = obj.getString("transactions");
                    myRealm.save(ac);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess() {
        List<AccountInfo> list = myRealm.getAll();
        summaryAdapter = new SummaryAdapter(list, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(summaryAdapter);
    }

    @Override
    public void onFailure() {

    }
}