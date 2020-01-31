package com.devsebastian.gtbit;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecentBillAdapter extends RecyclerView.Adapter<RecentBillAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Bill> dataSet;

    public void setBills(ArrayList<Bill> dataSet) {
        this.dataSet = dataSet;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView shopName, billSummary;

        public MyViewHolder(View itemView) {
            super(itemView);
            shopName = itemView.findViewById(R.id.shop_name_tv);
            billSummary = itemView.findViewById(R.id.bill_summary);
        }
    }

    public RecentBillAdapter(ArrayList<Bill> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_recycler_view_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Bill bill = dataSet.get(position);
        holder.shopName.setText(bill.getShopName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BillingActivity.class);
                intent.putExtra("id", bill.getId());
                context.startActivity(new Intent(context, BillingActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
