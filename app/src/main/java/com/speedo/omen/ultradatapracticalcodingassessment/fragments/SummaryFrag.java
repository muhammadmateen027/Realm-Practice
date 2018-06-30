package com.speedo.omen.ultradatapracticalcodingassessment.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.speedo.omen.ultradatapracticalcodingassessment.MainActivity;
import com.speedo.omen.ultradatapracticalcodingassessment.R;
import com.speedo.omen.ultradatapracticalcodingassessment.adapter.SummaryAdapter;
import com.speedo.omen.ultradatapracticalcodingassessment.adapter.ViewPagerAdapter;
import com.speedo.omen.ultradatapracticalcodingassessment.interfaces.DataInterface;
import com.speedo.omen.ultradatapracticalcodingassessment.interfaces.DataProcess;
import com.speedo.omen.ultradatapracticalcodingassessment.models.AccountInfo;
import com.speedo.omen.ultradatapracticalcodingassessment.networkcalls.VollyCall;
import com.speedo.omen.ultradatapracticalcodingassessment.db.MyRealm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

//First fragment to show data list
public class SummaryFrag extends Fragment implements DataInterface, DataProcess {

    final private String mUrl = "http://www.mocky.io/v2/5abb1042350000580073a7ea";
    final private String TAG = "SummaryFrag";
    private VollyCall vCall;
    private MyRealm myRealm;

    private RecyclerView recyclerView;
    private SummaryAdapter summaryAdapter;
    private ViewPager viewPager;
    public SummaryFrag() {

        // Required empty public constructor
    }

//    views initialized
    private void init(View mView) {
        recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        objects are created to call network calls and retrive data from realm
        myRealm = new MyRealm(SummaryFrag.this);
        vCall = new VollyCall(getContext(), SummaryFrag.this);
//        to get value from api
        vCall.getDataFromServer(mUrl);

        View mView =  inflater.inflate(R.layout.fragment_summary, container, false);
        init(mView);
        return mView;
    }

//    when api have data, then you can receive it here and can convert it into objects and called
// the save methode to store data in realm
    @Override
    public void onDataRetrived(String response) {
        JSONObject jsonObject = null;
//        Json parsing over here
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

//                    function call to store object
                    myRealm.save(ac);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // if you are succeded in storing data with in realm, then this mothed is call to get value
    // and pass to adapter to show in a recyclerview
    @Override
    public void onSuccess() {
        List<AccountInfo> list = myRealm.getAll();
        summaryAdapter = new SummaryAdapter(list, getContext(),viewPager);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(summaryAdapter);
    }

//    if you are failed to store data in realm, anyhow I'm not doing anything
    @Override
    public void onFailure() {

    }
}