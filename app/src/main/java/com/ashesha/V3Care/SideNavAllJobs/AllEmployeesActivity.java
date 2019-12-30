package com.ashesha.V3Care.SideNavAllJobs;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ashesha.V3Care.R;
import com.ashesha.V3Care.Utils.AppConstants;
import com.ashesha.V3Care.Utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllEmployeesActivity extends AppCompatActivity {

    List<AllEmployeesModel> allEmployeesModelList;
    AllEmployeesAdapter allEmployeesAdapter;
  private   RecyclerView recyclerView;
    AllEmployeesModel allEmployeesModel;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String logintype;
    String PREFERENCE = "AGENT";
    Context context;
    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_employees);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("All Employees");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        context = AllEmployeesActivity.this;

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(AllEmployeesActivity.this));
        empData();
        sharedpreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        logintype = sharedpreferences.getString("login_type", "");
        if (logintype.equalsIgnoreCase("admin")) {
            empData();
        }
        if (logintype.equalsIgnoreCase("employee")) {

        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if (logintype.equalsIgnoreCase("admin")) {
            empData();
        }
        if (logintype.equalsIgnoreCase("employee")) {

        }

    }


    private void empData() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.ADMIN_Employees, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                allEmployeesModelList = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("employees");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                Log.e("admindetails","fg"+response);
                                allEmployeesModel = new AllEmployeesModel();
                                allEmployeesModel.setEmpId(jsonObject1.getString("emp_id"));
                                allEmployeesModel.setEmpName(jsonObject1.getString("emp_name"));
                                allEmployeesModel.setEmpMobileNum(jsonObject1.getString("emp_num"));
                                allEmployeesModel.setEmpAddress(jsonObject1.getString("emp_adrs"));
                                allEmployeesModelList.add(allEmployeesModel);
                            }
                            allEmployeesAdapter = new AllEmployeesAdapter(AllEmployeesActivity.this, allEmployeesModelList);
                        recyclerView.setAdapter(allEmployeesAdapter);

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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

}
