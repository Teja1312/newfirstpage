package com.example.project.newfirstpage;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class TrackingService extends Service{
    private static final String TAG = TrackingService.class.getSimpleName();
    UserSession userSession;

    Map<String, Object> storedLocation;

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        userSession = new UserSession(this);
        buildNotification();
        loginToFirebase();
    }

    private void buildNotification() {
        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this).setContentTitle(getString(R.string.app_name)).setContentText(getString(R.string.tracking_enabled_notif)).setOngoing(true).setContentIntent(broadcastIntent).setSmallIcon(R.drawable.tracking_enabled);
        startForeground(1, builder.build());
    }

    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            unregisterReceiver(stopReceiver);
            stopSelf();
        }
    };

    private void loginToFirebase() {

        String email = userSession.getEmail();
        String password = userSession.getPassword();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    requestLocationUpdates();
                } else {
                    Log.d(TAG, "Firebase authentication failed");
                }
            }
        });
    }

    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();
        request.setInterval(10000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

       // final String path = getString(R.string.firebase_path);
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission == PackageManager.PERMISSION_GRANTED) {

            FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

            client.requestLocationUpdates(request, new LocationCallback() {

                @Override
                public void onLocationResult(LocationResult locationResult) {
                    final Location location = locationResult.getLastLocation();
                    if (location != null) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("location");
                        ref.setValue(location);
                        DatabaseReference fetchData =FirebaseDatabase.getInstance().getReference().child("location");

                       fetchData.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                              storedLocation = (Map<String, Object>)dataSnapshot.getValue();
                              //  Toast.makeText(getApplicationContext(),"latitude"+storedLocation.get("latitude")+"longitude"+storedLocation.get("longitude"),Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(),"Failed"+databaseError.getCode(),Toast.LENGTH_SHORT).show();
                            }
                        }

                       );
                       }

                }
            }, null);

        }
    }


}
