package com.devsebastian.gtbit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    ImageView saveLocationBtn, lastSavedSpotBtn, profileBtn;

    private Double userLat = 0d, userLng = 0d;

    private FusedLocationProviderClient client;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(MainActivity.this, BillingActivity.class);
                intent.putExtra("data", result.getContents());
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.toolbar_menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.send_sos:
                        final long date = System.currentTimeMillis();

                        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                databaseReference.child("sosDetails").child("1").child("date").setValue(date);
                                databaseReference.child("sosDetails").child("1").child("lat").setValue(location.getLatitude());
                                databaseReference.child("sosDetails").child("1").child("lng").setValue(location.getLongitude());

                                Dialog dialog = onCreateDialogSOS();
                                dialog.show();
                            }
                        });

                }

                return false;
            }
        });

        client = LocationServices.getFusedLocationProviderClient(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(MainActivity.this).initiateScan();
            }
        });

        saveLocationBtn = findViewById(R.id.save_location_btn);
        lastSavedSpotBtn = findViewById(R.id.find_last_spot);
        profileBtn = findViewById(R.id.profile);

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });

        saveLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Log.d("Location", "onSuccess: Latitude " + location.getLatitude());
                            userLat = location.getLatitude();
                            userLng = location.getLongitude();
                            databaseReference.child("parkedLocation").child("1").child("lat").setValue(userLat);
                            databaseReference.child("parkedLocation").child("1").child("lng").setValue(userLng);

                            Dialog dialog = onCreateDialogEdited("Parking spot saved");
                            dialog.show();
                        }
                    }
                });

            }
        });

        databaseReference.child("parkedLocation").child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    lastSavedSpotBtn.setVisibility(View.GONE);
                } else
                    lastSavedSpotBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        lastSavedSpotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MapActivity.class));
            }
        });
    }


    private Dialog onCreateDialogEdited(String text) {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(true);

        TextView textView = dialog.findViewById(R.id.update_tv);
        textView.setText(text);

        Button continueBtn = dialog.findViewById(R.id.check_btn);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this, MapActivity.class));
            }
        });

        return dialog;
    }

    private Dialog onCreateDialogSOS() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(true);

        ImageView imageView = dialog.findViewById(R.id.comment_tv);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_sos));

        TextView textView = dialog.findViewById(R.id.update_tv);
        textView.setText("SOS Sent");

        Button continueBtn = dialog.findViewById(R.id.check_btn);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        return dialog;
    }
}
