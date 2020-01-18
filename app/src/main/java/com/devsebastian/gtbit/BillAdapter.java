package com.devsebastian.gtbit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.MyViewHolder> {

    private ArrayList<BillItem> items;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView id, title, quantity, cost;

        public MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.item_id);
            title = (TextView) view.findViewById(R.id.item_title);
            quantity = (TextView) view.findViewById(R.id.item_quantity);
            cost = (TextView) view.findViewById(R.id.item_cost);
        }
    }


    public BillAdapter(ArrayList<BillItem> items) {
        this.items = items;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bill_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BillItem movie = items.get(position);
        holder.title.setText(movie.getTitle());
        holder.quantity.setText(movie.getQuantity().toString());
        holder.cost.setText("₹ " + movie.getCost().toString());
        holder.id.setText(movie.getId().toString());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}