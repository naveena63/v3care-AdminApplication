package com.ashesha.V3Care.AllLeads;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ashesha.V3Care.R;

import com.ashesha.V3Care.Utils.AppConstants;
import com.ashesha.V3Care.Utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class UpcomingLeadFragment extends Fragment {
    private ArrayList<String> serviceId;
    private ArrayList<String> name;
    private ArrayList<String> serviceDate;
    ArrayList<AllLeadModel> arrayList;
    List<AllLeadModel> upcomingLeadModelList;
    AllLeadModel upcomingLeadModel;

    RecyclerView recyclerView;
    UpcominLeadAdapter upcominLeadAdapter;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String logintype;
    RequestQueue requestQueue;
    String PREFERENCE = "AGENT";
    Context context;
    PrefManager prefManager;
    TextView no_packages_available;
    public UpcomingLeadFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming_jobs, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        prefManager=new PrefManager(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        upcomingLeadModelList = new ArrayList<>();
        requestQueue=Volley.newRequestQueue(getActivity());
        name = new ArrayList<>();
        serviceId = new ArrayList<>();
        serviceDate = new ArrayList<>();
        no_packages_available=view.findViewById(R.id.no_packages_available);
        context = getActivity();
        sharedpreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        logintype = sharedpreferences.getString("login_type", "");
        if (logintype.equalsIgnoreCase("admin")) {
            adminloadData();
        }
        if (logintype.equalsIgnoreCase("employee")) {
            emploadData();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (logintype.equalsIgnoreCase("admin")) {
            adminloadData();
        }
        if (logintype.equalsIgnoreCase("employee")) {
            emploadData();
        }
        //adminloadData();
        // emploadData();
    }
    private void adminloadData() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, AppConstants.ADMIN_UPCOMING_LEADS, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        arrayList = new ArrayList<>();
                        try {
                            String status = response.getString("status");
                            if (status.equalsIgnoreCase("success")) {
                                JSONArray jsonArrayadminPresentLeads = response.getJSONArray("adminUpcomingLeads");
                                Log.e("upcomingResponse", "upcoming res" + response);
                                for (int i = 0; i < jsonArrayadminPresentLeads.length(); i++) {
                                    JSONObject jsonObject = jsonArrayadminPresentLeads.getJSONObject(i);
                                    String order_id = jsonObject.getString("order_id");
                                    String name = jsonObject.getString("name");
                                    String service_date = jsonObject.getString("service_date");
                                    upcomingLeadModel = new AllLeadModel();
                                    upcomingLeadModel.setOrder_id(order_id);
                                    upcomingLeadModel.setName(name);
                                    upcomingLeadModel.setService_date(service_date);
                                    Log.e("dhdhdhd", "adminPresentLeads " + order_id);
                                    Log.e("dhdhdhdupcoming", "adminPresentLeads " + name);

                                    JSONArray jsonArrayOrder_products = jsonObject.getJSONArray("order_products");
                                    for (int j = 0; j < jsonArrayOrder_products.length(); j++) {
                                        JSONObject jsonObjectData = jsonArrayOrder_products.getJSONObject(j);
                                        String catNAme = jsonObjectData.getString("category_name");

                                        String time = jsonObjectData.getString("time_slot_name");
                                        upcomingLeadModel.setCategory_name(catNAme);

                                        upcomingLeadModel.setTime_slot(time);

                                        Log.e("dhdhdhd", "order_products " + catNAme);
                                        Log.e("dhdhdhd", "order_products " + service_date);
                                    }
                                    arrayList.add(upcomingLeadModel);
                                }

                                if (!arrayList.isEmpty() && arrayList.size() > 0) {
                                    upcominLeadAdapter = new UpcominLeadAdapter(getActivity(), arrayList);
                                    upcominLeadAdapter.notifyDataSetChanged();
                                    recyclerView.setAdapter(upcominLeadAdapter);
                                    no_packages_available.setVisibility(View.GONE);
                                }
                            } else {
                                no_packages_available.setText("there are no orders in future");
                                no_packages_available.setVisibility(View.VISIBLE);

                            }

                            /*todayLeadAdapter = new TodayLeadAdapter(getActivity(), arrayList);
                            recyclerView.setAdapter(todayLeadAdapter);*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("dhdhdhd", "try " + e.toString());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("dhdhdhd", "error" + error.toString());

            }
        });
        requestQueue.add(jsonObjectRequest);
    }



    private void emploadData() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.EMP_UPCOMING_LEADS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                arrayList = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    Log.e("upcoimgtemp", "upcomingempleads" + response);
                    if (status.equals("success")) {
                        JSONArray jsonarryEmpData = jsonObject.getJSONArray("adminUpcomingLeads");
                        for (int i = 0; i < jsonarryEmpData.length(); i++) {
                            JSONObject jsonobjectEmpdata = jsonarryEmpData.getJSONObject(i);
                            String order_id = jsonobjectEmpdata.getString("order_id");
                            String name = jsonobjectEmpdata.getString("name");
                            String service_date = jsonobjectEmpdata.getString("service_date");
                            upcomingLeadModel = new AllLeadModel();
                            upcomingLeadModel.setOrder_id(order_id);
                            upcomingLeadModel.setName(name);
                            upcomingLeadModel.setService_date(service_date);
                            Log.e("empupcomingname", "order_products " + name);

                            JSONArray jsonArrayOrder_products = jsonobjectEmpdata.getJSONArray("order_products");
                            for (int j = 0; j < jsonArrayOrder_products.length(); j++) {
                                JSONObject jsonObjectData = jsonArrayOrder_products.getJSONObject(j);
                                String catNAme = jsonObjectData.getString("category_name");

                                String time = jsonObjectData.getString("time_slot_name");
                                upcomingLeadModel.setCategory_name(catNAme);

                                upcomingLeadModel.setTime_slot(time);

                                Log.e("upcomingproduct", "order_products " + catNAme);
                                Log.e("upcomingproduct", "order_products " + service_date);
                            }
                            arrayList.add(upcomingLeadModel);
                        }

                        if (arrayList.size() > 0) {
                            upcominLeadAdapter = new UpcominLeadAdapter(getActivity(), arrayList);
                            upcominLeadAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(upcominLeadAdapter);
                            no_packages_available.setVisibility(View.GONE);
                        }
                    } else {
                        no_packages_available.setText("there are no orders in future");
                        no_packages_available.setVisibility(View.VISIBLE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("error", "try " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("error", "error" + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id", prefManager.getEmployeeId());
                Log.d("upcoming EmpId", "empId" + new PrefManager(getActivity()).getEmployeeId());
                return params;
            }
        };
        requestQueue.add(stringRequest);


    }}

