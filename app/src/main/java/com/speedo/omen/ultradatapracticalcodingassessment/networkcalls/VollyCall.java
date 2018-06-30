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

//class to make network calls to interact with apis
public class VollyCall {
    private Context mContext;
    final private String TAG = "VollyCall";
    private DataInterface dataInterface;

    public VollyCall (Context mContext, DataInterface dataInterface) {
        this.mContext = mContext;
        this.dataInterface = dataInterface;
    }
//    function where to pass URL and get results
    public void getDataFromServer(String GET_URL) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest postRequest = new StringRequest(Request.Method.POST, GET_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
//                        on results, this call back method give you data where you called it
                        dataInterface.onDataRetrived(response);

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        dataInterface.onNetworkFail();
                        Log.d(TAG, "Error.Response:"+ String.valueOf(error));
                    }
                }
        );
        queue.add(postRequest);
    }
}
