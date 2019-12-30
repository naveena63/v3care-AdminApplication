package com.ashesha.V3Care.SideNavAllJobs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashesha.V3Care.R;

import java.util.List;

public class AllEmployeesAdapter extends RecyclerView.Adapter<AllEmployeesAdapter.ViewHolder> {

List<AllEmployeesModel> allEmployeesModelList;
Context context;

 public AllEmployeesAdapter(AllEmployeesActivity allEmployeesActivity, List<AllEmployeesModel>  allEmployeesModelList)
{
    this.allEmployeesModelList=allEmployeesModelList;

}



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
 context=viewGroup.getContext();
 View view=LayoutInflater.from(context).inflate(R.layout.all_emp_layout,viewGroup,false);
 return new ViewHolder(view);
  }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
       viewHolder.empId.setText("Employee Id:"+allEmployeesModelList.get(i).getEmpId());
       viewHolder.empName.setText("Employee Name:"+allEmployeesModelList.get(i).getEmpName());
       viewHolder.empPhoneNum.setText("Employee PhoneNumber:"+allEmployeesModelList.get(i).getEmpMobileNum());
       viewHolder.empAddress.setText("Employee Address:"+allEmployeesModelList.get(i).getEmpAddress());
    }

    @Override
    public int getItemCount() {
        return allEmployeesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
      TextView empId,empName,empPhoneNum,empAddress;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            empId = itemView.findViewById(R.id.empid);
            empName = itemView.findViewById(R.id.empName);
            empPhoneNum = itemView.findViewById(R.id.empPhoneNum);
            empAddress = itemView.findViewById(R.id.empAddres);
        }
    }
}
