package com.speedo.omen.ultradatapracticalcodingassessment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.speedo.omen.ultradatapracticalcodingassessment.adapter.ViewPagerAdapter;
import com.speedo.omen.ultradatapracticalcodingassessment.fragments.SummaryFrag;
import com.speedo.omen.ultradatapracticalcodingassessment.fragments.PaymentFrag;
import com.speedo.omen.ultradatapracticalcodingassessment.fragments.AccountsFrag;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        view pager to show fragments on it
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

//        tab layout is used to play with view pager through tabs
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
//    it sets the view pages on pager
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SummaryFrag(), "SUMMARY");
        adapter.addFragment(new AccountsFrag(), "ACCOUNTS");
        adapter.addFragment(new PaymentFrag(), "PAYMENTS");
        viewPager.setAdapter(adapter);
    }

}