package com.ashesha.V3Care.CancelReason;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

import com.ashesha.V3Care.R;
import com.ashesha.V3Care.Utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class CancelReasonAdapter extends RecyclerView.Adapter {
    PrefManager prefManager;
    Context context;
    List<CancelReasonModel> cancelReasonModelList=new ArrayList<>();





    CheckBox radioButton;

    public CancelReasonAdapter(Context context, List<CancelReasonModel> cancelReasonModelList) {
        this.context = context;
        this.cancelReasonModelList = cancelReasonModelList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view= layoutInflater.inflate(R.layout.cancel_reasoncustom_layout,viewGroup,false);
        CancelReasonViewHolder cancelReasonViewHolder=new CancelReasonViewHolder(view);
        prefManager = new PrefManager(context);



        return cancelReasonViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        radioButton.setText(cancelReasonModelList.get(position).getDescription());
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  ((CancelReasonActivity)context).saveCancelReason();
                prefManager.setReson_id(cancelReasonModelList.get(position).getId());

                Log.e("hhdhdh","dhdhd"+cancelReasonModelList.get(position).getId());


            }
        });

    }


    @Override
    public int getItemCount() {
        return cancelReasonModelList.size();
    }

    private class CancelReasonViewHolder extends RecyclerView.ViewHolder {
        public CancelReasonViewHolder(@NonNull View itemView) {
            super(itemView);

            radioButton=itemView.findViewById(R.id.rb);


        }

    }
}
