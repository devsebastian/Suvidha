package com.devsebastian.gtbit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class BillingActivity extends AppCompatActivity {
    String json;
    String shopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        TextView billTitle = findViewById(R.id.bill_title);
        TextView billTotalCost = findViewById(R.id.total_cost);
        TextView billTotalUnits = findViewById(R.id.total_units);

        final ArrayList<BillItem> items = new ArrayList<>();
        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra("data") != null) {
            json = getIntent().getStringExtra("data");

            try {
                JSONObject bill = new JSONObject(json);
                shopName = bill.getString("name");
                billTitle.setText(shopName);
                JSONArray jsonItems = bill.getJSONArray("items");
                int total = 0;
                for (int i = 0; i < jsonItems.length(); i++) {
                    JSONObject item = jsonItems.getJSONObject(i);
                    String id = item.getString("id");
                    final BillItem billItem = new BillItem();
                    billItem.setId(Integer.parseInt(id));
                    int quantity = item.getInt("quantity");
                    billItem.setQuantity(quantity);
                    items.add(billItem);
                    total += quantity;
                }
                billTotalUnits.setText(total + " units");

                String key = FirebaseDatabase.getInstance().getReference().child("bills").child("1").push().getKey();
                Bill b = new Bill(shopName, items);
                FirebaseDatabase.getInstance().getReference().child("bills").child("1").child(key).setValue(b);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            FirebaseDatabase.getInstance().getReference().child("bills").child("1").child(getIntent().getStringExtra("id")).child("items").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Integer id = snapshot.child("id").getValue(Integer.class);
                        final Item item = new Item();
                        FirebaseDatabase.getInstance().getReference().child("items").child(id + "").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                item.setTitle(dataSnapshot.child("name").getValue(String.class));
                                item.setDeal(dataSnapshot.child("deal").getValue(String.class));
                                item.setImgUrl(dataSnapshot.child("imgurl").getValue(String.class));
                                item.setCost(dataSnapshot.child("Price").getValue(Integer.class));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        RecyclerView recyclerView = findViewById(R.id.bill_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new BillAdapter(this, items, billTotalCost));
    }
}
