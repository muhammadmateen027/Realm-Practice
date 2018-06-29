package com.speedo.omen.ultradatapracticalcodingassessment.networkcalls;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.speedo.omen.ultradatapracticalcodingassessment.interfaces.DataInterface;

import org.json.JSONObject;


public class VollyCall {
    private Context mContext;
    final private String TAG = "VollyCall";
    private DataInterface dataInterface;

    public VollyCall (Context mContext, DataInterface dataInterface) {
        this.mContext = mContext;
        this.dataInterface = dataInterface;
    }

    public void getDataFromServer(String GET_URL) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest postRequest = new StringRequest(Request.Method.POST, GET_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.w(TAG, "Response: "+ response);
                        dataInterface.onDataRetrived(response);

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d(TAG, "Error.Response:"+ String.valueOf(error));
                    }
                }
        );
        queue.add(postRequest);
    }
}
