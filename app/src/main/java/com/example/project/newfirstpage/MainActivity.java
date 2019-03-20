package com.example.project.newfirstpage;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Map;

import static com.example.project.newfirstpage.R.drawable.ic_menu_three_horizontal_lines_symbol;
import static com.example.project.newfirstpage.R.drawable.ic_sms_black_24dp;

public class MainActivity extends AppCompatActivity {

    String SENT = "com.yourapp.sms_send";
    String DELIVERED = "com.yourapp.sms_delivered";
    BroadcastReceiver sendingBroadcastReceiver;
    BroadcastReceiver deliveryBroadcastReceiver;
    Map<String,Object> storedLocation;
    String mLatitude,mLongitude;
    UserSession userSession;

   DatabaseReference ref =FirebaseDatabase.getInstance().getReference().child("location");

    static int i=0;

    PendingIntent sentPI;
    PendingIntent deliveredPI;
    String message;

    String imei ;

    private DrawerLayout drawerLayout;
    Intent intent;

    private static final int PERMISSIONS_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        final Context context = this;
        NavigationView navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        switch (menuItem.getItemId()) {
                            case R.id.home_menu:
                                startActivity(new Intent(context, MainActivity.class));
                                break;
                            case R.id.update_menu:
                                startActivity(new Intent(context, UpdateProfile.class));
                                break;
                            case R.id.emg_num_menu:
                                startActivity(new Intent(context, Emergency_number_list.class));
                                break;
                            case R.id.blog_menu:
                                startActivity(new Intent(context, blogs.class));
                                break;
                        }
                        return true;
                    }
                }
        );

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //finish();
            Toast.makeText(this, "GPS provider is not enabled", Toast.LENGTH_SHORT).show();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show();
            startTrackerService();
        } else {
            Toast.makeText(this, "Permissions requesting", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST);
        }

    }
        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {
                i++;
                if(i==3){
                    danger(null );
                }
            }
            return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();}
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permisssions requested", Toast.LENGTH_SHORT).show();
            startTrackerService();
        } else {
            Toast.makeText(this, "Please enable location services to allow GPS tracking", Toast.LENGTH_SHORT).show();
        }
    }

    private void startTrackerService() {
        startService(new Intent(this, TrackingService.class));
        Toast.makeText(this, "GPS tracking enabled", Toast.LENGTH_SHORT).show();
        //finish();
    }

    public void danger(View view) {

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSIONS_REQUEST);
        }
        userSession = new UserSession(this);


        broadCast();
        imei= telephonyManager.getDeviceId();
        getLocationFromFirebase();
        String messageToSend = "EMERGENCY..I'm in trouble. Please help me. "+"\nLocation :"+"http://maps.google.com/?q="+userSession.getLatitude()+","+userSession.getLongitude()+"\nIMEI number is :"+imei;
        Log.i("mymessage","message:"+messageToSend);
        String number = "9870830412";


        sendingBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        message="Alert SMS sent..!";
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        message="Error..Alert SMS not sent..!";
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        message="No service to send alert SMS..!";
                        break;
                }

                NotificationCompat.Builder mBuilder;
                if (getResultCode()==Activity.RESULT_OK)
                {
                    mBuilder = new NotificationCompat.Builder(MainActivity.this).setSmallIcon(R.drawable.ic_sms_black_24dp).setContentTitle("Getting notification").setContentText("Alert msg sent..!");
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(001, mBuilder.build());
                }
                else
                {
                    mBuilder = new NotificationCompat.Builder(MainActivity.this).setSmallIcon(R.drawable.ic_sms_failed_black_24dp).setContentTitle("Getting notification").setContentText("Message not sent..!");
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(001, mBuilder.build());
                }
                // Toast.makeText(MainActivity.this, getResultCode(), Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
                MainActivity.this.unregisterReceiver(this);
            }

        };
        this.registerReceiver(sendingBroadcastReceiver, new IntentFilter(SENT));
        deliveryBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        message="Alert SMS Received..!";
                        break;
                    case Activity.RESULT_CANCELED:
                        message="Alert SMS cancelled.!";
                        break;
                }
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
                MainActivity.this.unregisterReceiver(this);
            }
        };

        this.registerReceiver(deliveryBroadcastReceiver, new IntentFilter(DELIVERED));
        SmsManager.getDefault().sendTextMessage(number, null, messageToSend, sentPI, deliveredPI);


    }


    public void safe(View view){
        broadCast();
        String messageToSend = "RELAX...I'm safe. Thanks for being there for me..!";
        String number = "9870830412";

        sendingBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        message="Safe SMS sent..!";
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        message="Error..Safe SMS not sent..!";
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        message="No service to send Safe SMS..!";
                        break;
                }

                NotificationCompat.Builder mBuilder;
                if (getResultCode()==Activity.RESULT_OK)
                {
                    mBuilder = new NotificationCompat.Builder(MainActivity.this).setSmallIcon(R.drawable.ic_sms_black_24dp).setContentTitle("Getting notification").setContentText("Alert msg sent..!");
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(001, mBuilder.build());
                }
                else
                {
                    mBuilder = new NotificationCompat.Builder(MainActivity.this).setSmallIcon(R.drawable.ic_sms_failed_black_24dp).setContentTitle("Getting notification").setContentText("Message not sent..!");
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(001, mBuilder.build());
                }
                // Toast.makeText(MainActivity.this, getResultCode(), Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
                MainActivity.this.unregisterReceiver(this);
            }

        };
        this.registerReceiver(sendingBroadcastReceiver, new IntentFilter(SENT));
        deliveryBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        message="Safe SMS Received..!";
                        break;
                    case Activity.RESULT_CANCELED:
                        message="Safe SMS cancelled.!";
                        break;
                }
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
                MainActivity.this.unregisterReceiver(this);
            }
        };

        this.registerReceiver(deliveryBroadcastReceiver, new IntentFilter(DELIVERED));
        SmsManager.getDefault().sendTextMessage(number, null, messageToSend, sentPI, deliveredPI);

    }


    public void emgNum(View view)
    {
        Toast.makeText(this, "Emergency List ", Toast.LENGTH_LONG).show();
        intent =new Intent(getApplicationContext(),Emergency_number_list.class);
        startActivity(intent);
    }

    public void update(View view)
    {
        Toast.makeText(this, "Update Records", Toast.LENGTH_LONG).show();
        intent =new Intent(getApplicationContext(),UpdateProfile.class);
        startActivity(intent);
    }

    public void blog(View view)
    {
        Toast.makeText(this, "Blogs",Toast.LENGTH_SHORT).show();
        intent = new Intent(getApplicationContext(),blogs.class);
        startActivity(intent);
    }

    public void complaint(View view)
    {
        Toast.makeText(this,"Complaint box",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(),complaintBox.class));
    }


    public void broadCast()
    {
        try {
            sentPI      = PendingIntent.getBroadcast(this, 0, new Intent(SENT),     0);
            deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);
            //RESULT_ON=1;
        }
        catch (Exception ex)
        {
            Log.d("exception is",""+ex.getMessage());
        }
    }



    public void getLocationFromFirebase()
    {

        ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                storedLocation = (Map<String, Object>) dataSnapshot.getValue();
                                            //    Toast.makeText(getApplicationContext(), "latitude" + storedLocation.get("latitude") + "longitude" + storedLocation.get("longitude"), Toast.LENGTH_LONG).show();
                                                mLatitude = storedLocation.get("latitude").toString();
                                                mLongitude = storedLocation.get("longitude").toString();
                                                userSession.setLatitude(mLatitude);
                                                userSession.setLongitude(mLongitude);

                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                Toast.makeText(getApplicationContext(),"Failed"+databaseError.getCode(),Toast.LENGTH_SHORT).show();
                                            }
                                        }

        );
   }


}



