package com.speedo.omen.ultradatapracticalcodingassessment.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.speedo.omen.ultradatapracticalcodingassessment.R;
import com.speedo.omen.ultradatapracticalcodingassessment.models.AccountInfo;

import java.util.List;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.MyViewHolder> {
    private List<AccountInfo> infoList;
    private Context mContext;
    public SummaryAdapter (List<AccountInfo> infoList, Context mContext) {
        this.infoList = infoList;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.summary_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        AccountInfo acInfo = infoList.get(position);
        holder.account_title.setText(acInfo.getAccountLabel());
        holder.account_number.setText(acInfo.getAccountNumber());
        holder.available_amount.setText("$"+acInfo.getAvailableBalance());
        holder.current_amount.setText("$"+acInfo.getCurrentBalance());
        holder.f_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Clicked: "+ String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView account_title, account_number, available_amount,current_amount;
        public LinearLayout f_view;

        public MyViewHolder(View view) {
            super(view);
            account_title = (TextView) view.findViewById(R.id.account_title);
            account_number = (TextView) view.findViewById(R.id.account_number);
            available_amount = (TextView) view.findViewById(R.id.available_amount);
            current_amount = (TextView) view.findViewById(R.id.current_amount);
            f_view = (LinearLayout) view.findViewById(R.id.right_index);
        }
    }
}
