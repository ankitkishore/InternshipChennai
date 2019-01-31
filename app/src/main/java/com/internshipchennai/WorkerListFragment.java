package com.internshipchennai;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WorkerListFragment extends Fragment {

    ListView workerlist;
    String username,email;
    ArrayList<String> worker_name;
    SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        worker_name = new ArrayList<>();

        View root =  inflater.inflate(R.layout.fragment_worker_list, container, false);

        workerlist = root.findViewById(R.id.workerlist);
        swipeRefreshLayout = root.findViewById(R.id.pullToRefresh);


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("mysharedpref12", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username","");
        email =sharedPreferences.getString("useremail","");

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                update();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        update();

        workerlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                Log.e("yo1",name);

                Intent intent = new Intent(getContext(), WorkerDetails.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });

        return root;
    }


    public void update(){
        worker_name.clear();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_ADMIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("yo", "response"+ response);

                        try {
                            JSONArray ar = new JSONArray(response);
                            for(int i = 0; i < ar.length(); i++){
                                JSONObject obj = ar.getJSONObject(i);
                                worker_name.add(obj.getString("username"));
                            }

                            ArrayAdapter adapter = new ArrayAdapter<>(getContext(),
                                    R.layout.listview, worker_name);
                            workerlist.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(
                                getContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("email", email);
                return params;
            }

        };


        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
    }


}
