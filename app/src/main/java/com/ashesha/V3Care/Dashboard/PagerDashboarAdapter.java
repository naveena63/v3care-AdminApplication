package com.ashesha.V3Care.Dashboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ashesha.V3Care.AllLeads.PastLeadFragment;
import com.ashesha.V3Care.AllLeads.TodayLeadFragment;
import com.ashesha.V3Care.AllLeads.UpcomingLeadFragment;

public class PagerDashboarAdapter extends FragmentPagerAdapter {

    int numberOfTabs;

    public PagerDashboarAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.numberOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TodayLeadFragment todayLeadFragment =new TodayLeadFragment();
                return todayLeadFragment;

            case 1:
                UpcomingLeadFragment upcomingLeadFragment =new UpcomingLeadFragment();
                return upcomingLeadFragment;
            case 2:
                PastLeadFragment pastLeadFragment =new PastLeadFragment();
                return pastLeadFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}