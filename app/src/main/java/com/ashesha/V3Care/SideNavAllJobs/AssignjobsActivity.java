package com.ashesha.V3Care.SideNavAllJobs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ashesha.V3Care.Dashboard.NavDashboardActivity;
import com.ashesha.V3Care.R;
import com.ashesha.V3Care.Utils.AppConstants;
import com.ashesha.V3Care.Utils.PrefManager;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssignjobsActivity extends AppCompatActivity
{
    List<AssignModel> assignModelList;
    AssignAdapter assignAdapter;
    AssignModel assignModel;
    RecyclerView recyclerView;
    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignjobs);
        recyclerView = findViewById(R.id.recyclerView);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("AssignJobs");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(AssignjobsActivity.this));
        assignJob();
    }

    private void assignJob() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.ADMIN_ASSIGN_JOB, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                assignModelList = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    Log.i("reasonForCancel", "Reason Cancel" + response);

                    if (status.equals("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("assigned_jobs");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject json = jsonArray.getJSONObject(i);
                            String emp_name = json.getString("emp_name");
                            String custmorName = json.getString("name");
                            String order_id = json.getString("order_id");

                            assignModel = new AssignModel();
                            assignModel.setCustmorName(custmorName);
                            assignModel.setEmpName(emp_name);
                            assignModel.setOrderId(order_id);
                            assignModelList.add(assignModel);
                        }
                        assignAdapter = new AssignAdapter(AssignjobsActivity.this, assignModelList);
                        recyclerView.setAdapter(assignAdapter);
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(AssignjobsActivity.this);
        requestQueue.add(stringRequest);

    }
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent (AssignjobsActivity.this, NavDashboardActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
