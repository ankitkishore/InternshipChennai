package com.internshipchennai;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WorkerDetails extends AppCompatActivity {

    TextView name,work;
    String username,email;
    ImageView edit;
    String workername,id;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_details);

        SharedPreferences sharedPreferences = this.getSharedPreferences("mysharedpref12", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username","");
        email =sharedPreferences.getString("useremail","");
        Bundle bundle = getIntent().getExtras();
        workername = bundle.getString("name");

        name = findViewById(R.id.name);
        work = findViewById(R.id.work);
        edit = findViewById(R.id.edit);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(WorkerDetails.this);

                //tell the Dialog to use the dialog.xml as it's layout description
                dialog.setContentView(R.layout.dialog);
                dialog.setTitle("Android Custom Dialog Box");

                final EditText txt = dialog.findViewById(R.id.changework);

                txt.setText(work.getText().toString());

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButton);

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateWork(txt.getText().toString());
                    }
                });

                dialog.show();
            }
        });
        getWork();
    }

    public void getWork(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_WORK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("yo", "response"+ response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            name.setText(workername);
                            work.setText(obj.getString("work"));
                            id = obj.getString("id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(
                                getApplicationContext(),
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
                params.put("workername", workername);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void updateWork(final String updatedwork){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_UPDATEWORK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(WorkerDetails.this,"Sussesfull",Toast.LENGTH_LONG).show();
                        getWork();
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(
                                getApplicationContext(),
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
                params.put("id", id);
                params.put("updatedwork", updatedwork);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }
}
