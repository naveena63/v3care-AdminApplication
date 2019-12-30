package com.ashesha.V3Care.Dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ashesha.V3Care.Logins.MainLoginsActivity;
import com.ashesha.V3Care.Profile.ProfileActivity;
import com.ashesha.V3Care.R;
import com.ashesha.V3Care.SideNavAllJobs.AllEmployeesActivity;
import com.ashesha.V3Care.SideNavAllJobs.AssignjobsActivity;
import com.ashesha.V3Care.SideNavAllJobs.CancelledJobActivity;
import com.ashesha.V3Care.SideNavAllJobs.ClosedJobsActivity;
import com.ashesha.V3Care.SideNavAllJobs.OpenJobsActivity;
import com.ashesha.V3Care.SideNavAllJobs.PaymentPendingJobsActivity;
import com.ashesha.V3Care.SideNavAllJobs.PendingJobsActivity;
import com.ashesha.V3Care.Utils.AppConstants;
import com.ashesha.V3Care.Utils.PrefManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NavDashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String EMP_ID = "emp_id";

   // TextView nameTv, ratingTv, openJobsTv, empAmount, navEmpName;
    TextView navEmpName;
    PrefManager prefManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String logintype;
    String PREFERENCE = "AGENT";
    Context context;
    ImageView navEmpImageView;
    public static int selected_spinner_pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_dashboard);
        context = NavDashboardActivity.this;
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        prefManager = new PrefManager(this);
        View hView = navigationView.getHeaderView(0);


       navEmpImageView=(ImageView)findViewById(R.id.navEmpImg);
        navEmpName = (TextView) findViewById(R.id.Admin);
/*        navEmpName = (TextView) hView.findViewById(R.id.Admin);
        navEmpImageView = (ImageView) hView.findViewById(R.id.navEmpImg);*/
       /* nameTv = findViewById(R.id.nameTv);
        ratingTv = findViewById(R.id.ratingTv);
        openJobsTv = findViewById(R.id.openJobsTv);
        empAmount = findViewById(R.id.empAmount);


        openJobsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intetn = new Intent(NavDashboardActivity.this, OpenJobsActivity.class);
                startActivity(intetn);
            }
        });*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setIcon(R.drawable.logo);
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Todays"));
        tabLayout.addTab(tabLayout.newTab().setText("Upcoming"));
        tabLayout.addTab(tabLayout.newTab().setText("Past"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(R.color.black, R.color.red);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerDashboarAdapter adapter = new PagerDashboarAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        setSupportActionBar(toolbar);
        context = this;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        logintype = sharedPreferences.getString("login_type", "");
        if (logintype.equalsIgnoreCase("admin")) {
            //ratingTv.setVisibility(View.INVISIBLE);
            //adminDetails();
            adminProfile();

        }
        if (logintype.equalsIgnoreCase("employee")) {
            //ratingTv.setVisibility(View.VISIBLE);
            //empDetails();
            imageDisplay();
        }


    }


    @Override
    public void onResume() {
        super.onResume();
        if (logintype.equalsIgnoreCase("admin")) {
           // ratingTv.setVisibility(View.INVISIBLE);
            //adminDetails();
            adminProfile();

        }
        if (logintype.equalsIgnoreCase("employee")) {
            //ratingTv.setVisibility(View.VISIBLE);
           // empDetails();
            imageDisplay();
        }
    }
    private void adminProfile() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.ADMIN_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("employee_data");
                        for (int i = 0; i < jsonArray.length(); i++) {


                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String name = jsonObject1.getString("name");
                            String image = jsonObject1.getString("profile_pic");
                            navEmpName.setText(name);

                            Glide.with(NavDashboardActivity.this)
                                    .load("http://v3care.com/" + image)
                                    .apply(RequestOptions.placeholderOf(R.drawable.ic_launcher_background).
                                            error(R.drawable.ic_launcher_background))
                                    .into(navEmpImageView);

                            Log.e("imagepath", "http://v3care.com/" + image);
                        }
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
                params.put("emp_id", new PrefManager(NavDashboardActivity.this).getEmployeeId());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void imageDisplay() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.EMP_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("employee_data");
                        String name = jsonObject1.getString("emp_name");
                        String image = jsonObject1.getString("emp_image");
                        navEmpName.setText(name);

                        Glide.with(NavDashboardActivity.this)
                                .load("http://v3care.com/" + image)
                                .apply(RequestOptions.placeholderOf(R.drawable.ic_launcher_background).
                                        error(R.drawable.ic_launcher_background))
                                .into(navEmpImageView);

                        Log.e("imagepath", "http://v3care.com/" + image);

                        navEmpName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intetn = new Intent(NavDashboardActivity.this, ProfileActivity.class);
                                startActivity(intetn);
                            }
                        });  navEmpImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intetn = new Intent(NavDashboardActivity.this, ProfileActivity.class);
                                startActivity(intetn);
                            }
                        });
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
                params.put("emp_id", new PrefManager(NavDashboardActivity.this).getEmployeeId());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

   /* //Admin Service Call Starts Here
    private void adminDetails() {
        final RequestQueue requestQueue = Volley.newRequestQueue(NavDashboardActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.ADMIN_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("employee_data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String name = jsonObject1.getString("name");
                            Log.e("empdetails", "empdetails" + response);
                            nameTv.setText(name);
                        }
                    }
                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                //    map.put(EMP_ID, prefManager.getEmployeeId());
                return map;
            }

            ;
        };
        requestQueue.add(stringRequest);
    }

    private void empDetails() {
        final RequestQueue requestQueue = Volley.newRequestQueue(NavDashboardActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.EMPLOYEE_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {

                        JSONObject jsonObject1 = jsonObject.getJSONObject("employee_data");
                        Log.e("empdetails", "empdetails" + response);
                        String name = jsonObject1.getString("name");
                        String money = jsonObject1.getString("account_money");
                        String rate = jsonObject1.getString("rating");

                        nameTv.setText(name);
                        empAmount.setText(money);
                        ratingTv.setText(rate);
                    }
                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put(EMP_ID, prefManager.getEmployeeId());
                return map;
            }

            ;
        };
        requestQueue.add(stringRequest);
    }*/
    private void refresh(Context context) {
        Intent intent=new Intent(this,NavDashboardActivity.class);
        startActivity(intent);
        finish();

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_refresh: {
                ((NavDashboardActivity)context).refresh(context);


                return true;
            }
           /* case R.id.notifications: {
                return true;
            }*/
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.open_jobs) {
            Intent intent = new Intent(NavDashboardActivity.this, OpenJobsActivity.class);
            startActivity(intent);
        } else if (id == R.id.closed_jobs) {
            Intent intent = new Intent(NavDashboardActivity.this, ClosedJobsActivity.class);
            startActivity(intent);
        } else if (id == R.id.canceld_jobs) {
            Intent intent = new Intent(NavDashboardActivity.this, CancelledJobActivity.class);
            startActivity(intent);
        } else if (id == R.id.pending_jobs) {
            Intent intent = new Intent(NavDashboardActivity.this, OpenJobsActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_assign) {
            if (logintype.equalsIgnoreCase("admin")) {
                Intent intent = new Intent(NavDashboardActivity.this, AssignjobsActivity.class);
                startActivity(intent);
            }
            if (logintype.equalsIgnoreCase("employee")) {
                Toast.makeText(context, "This Field  Not Availble For Employee", Toast.LENGTH_SHORT).show();
            }
        }
        else if (id == R.id.nav_paymentPending) {
            if (logintype.equalsIgnoreCase("admin")) {
                Intent intent = new Intent(NavDashboardActivity.this, PaymentPendingJobsActivity.class);
                startActivity(intent);
            }
            if (logintype.equalsIgnoreCase("employee")) {
                Toast.makeText(context, "This Field  Not Availble For Employee", Toast.LENGTH_SHORT).show();
            }
        }
        else if (id == R.id.nav_emp) {
            if (logintype.equalsIgnoreCase("admin")) {
                Intent intent = new Intent(NavDashboardActivity.this, AllEmployeesActivity.class);
                startActivity(intent);
            }
            if (logintype.equalsIgnoreCase("employee")) {
                Toast.makeText(context, "This Field  Not Availble For Employee", Toast.LENGTH_SHORT).show();
            }

        }
        else if (id == R.id.nav_logout) {
            prefManager.logout();
            Intent intent = new Intent(NavDashboardActivity.this, MainLoginsActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}