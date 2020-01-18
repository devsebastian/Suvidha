package com.devsebastian.gtbit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BillingActivity extends AppCompatActivity {
    String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);


        ArrayList<BillItem> items = new ArrayList<>();
        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra("data") != null) {
            json = getIntent().getStringExtra("data");
        }
        try {
            JSONObject bill = new JSONObject(json);
            String shopName = bill.getString("name");
            JSONArray jsonItems = bill.getJSONArray("items");
            for (int i = 0; i < jsonItems.length(); i++) {
                JSONObject item = jsonItems.getJSONObject(i);
                String title = item.getString("name");
                Integer quantity = item.getInt("quantity");
                items.add(new BillItem(1231231232, title, "", quantity, 1233.4));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RecyclerView recyclerView = findViewById(R.id.bill_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new BillAdapter(items));
    }
}
