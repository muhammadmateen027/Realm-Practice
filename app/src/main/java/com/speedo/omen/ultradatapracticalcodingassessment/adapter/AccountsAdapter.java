package com.speedo.omen.ultradatapracticalcodingassessment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.speedo.omen.ultradatapracticalcodingassessment.R;
import com.speedo.omen.ultradatapracticalcodingassessment.models.TransactionInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AccountsAdapter extends ArrayAdapter<TransactionInfo> {
    private List<TransactionInfo> dataSet;
    private Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView date_tv;
        View view_one;
        TextView desc_tv;
        TextView amount_tv;
    }

    public AccountsAdapter(List<TransactionInfo> data, Context context) {
        super(context, R.layout.list_items, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TransactionInfo dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_items, parent, false);
            viewHolder.date_tv = (TextView) convertView.findViewById(R.id.date_tv);
            viewHolder.view_one = (View) convertView.findViewById(R.id.view_one);
            viewHolder.desc_tv = (TextView) convertView.findViewById(R.id.desc_tv);
            viewHolder.amount_tv = (TextView) convertView.findViewById(R.id.amount_tv);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position!= 0 && dataModel.getTransDate().equalsIgnoreCase(dataSet.get(position-1).getTransDate())) {
            viewHolder.date_tv.setVisibility(View.GONE);
            viewHolder.view_one.setVisibility(View.GONE);
        } else {
            viewHolder.date_tv.setVisibility(View.VISIBLE);
            viewHolder.view_one.setVisibility(View.VISIBLE);
            viewHolder.date_tv.setText(convertDate(dataModel.getTransDate()));
        }

        if (dataModel.getTransAmount().startsWith("-")) {
            String value = dataModel.getTransAmount().substring(1);
            viewHolder.amount_tv.setText("-$"+value);
        } else {
            viewHolder.amount_tv.setText("$"+dataModel.getTransAmount());
        }
        viewHolder.desc_tv.setText(dataModel.getTransDesc());
        // Return the completed view to render on screen
        return convertView;
    }

//    function is used to convert the date recieved into Simple Date Format
    private String convertDate(String dt){
        String cd = "";
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
            Date date = inputFormat.parse(dt);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
            System.out.println(outputFormat.format(date));
            cd = outputFormat.format(date);
        } catch (Exception e) {
            //cannot happen in this example
        }
        return cd;
    }
}
