package com.devsebastian.gtbit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    Button saveLocationBtn;
    LocationManager locationManager;

    private Double userLat = 0d, userLng = 0d;

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


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(MainActivity.this).initiateScan();
            }
        });

        saveLocationBtn = findViewById(R.id.floating_action_button);

        saveLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("parkedLocation").child("1").child("lat").setValue(userLat);
                databaseReference.child("parkedLocation").child("1").child("lng").setValue(userLng);

                Dialog dialog = onCreateDialogEdited();
                dialog.show();

            }
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                userLat = location.getLatitude();
                userLng = location.getLongitude();
                Log.d("devishan", "onLocationChanged: success");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("devishan", "onStatusChanged: success");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("devishan", "onProviderEnabled: success");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("devishan", "onProviderDisabled: success");
            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0,
                0, locationListener);
    }


    private Dialog onCreateDialogEdited() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(false);

        TextView textView = dialog.findViewById(R.id.update_tv);
        textView.setText("Parking spot saved");

        Button continueBtn = dialog.findViewById(R.id.check_btn);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this,MapActivity.class));
            }
        });

        return dialog;
    }

}
