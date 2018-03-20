package com.teste.rastreamento_client.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Created by Paulo on 11/11/2017.
 */

public class GPSService extends Service implements LocationListener{

    public static final String ACTION_GPS_LOCATION_CHANGE = "com.teste.rastreamento_client.MapaActivity.action.GPS_LOCATION_CHANGE";
    public static final String ACTION_GPS_PROVIDER_ENABLED = "com.teste.rastreamento_client.MapaActivity.action.GPS_PROVIDER_ENABLED";
    public static final String ACTION_GPS_PROVIDER_DISABLED = "com.teste.rastreamento_client.MapaActivity.action.GPS_PROVIDER_DISABLED";
    public static final String ACTION_GPS_STATUS_CHANGE = "com.teste.rastreamento_client.MapaActivity.action.GPS_STATUS_CHANGE";
    public static final String ACTION_GPS_START = "com.teste.rastreamento_client.MapaActivity.action.GPS_START";
    public static final String ACTION_GPS_STOP = "com.teste.rastreamento_client.MapaActivity.action.GPS_STOP";


    public static boolean SERVICE_CONNECTED = false;
    private LocationManager locationManager;
    private IBinder binder = new GPSBinder();
    private Context context;

    /************* Called after each 3 sec **********/
    @Override
    public void onLocationChanged(Location location) {
        Intent intent = new Intent(ACTION_GPS_LOCATION_CHANGE);
        intent.putExtra("lati", location.getLatitude());
        intent.putExtra("long", location.getLongitude());
        context.sendBroadcast(intent);
    }

    @Override
    public void onProviderDisabled(String provider) {
        context.sendBroadcast(new Intent(ACTION_GPS_PROVIDER_DISABLED));
    }

    @Override
    public void onProviderEnabled(String provider) {
        context.sendBroadcast(new Intent(ACTION_GPS_PROVIDER_ENABLED));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Intent intent = new Intent(ACTION_GPS_STATUS_CHANGE);
        intent.putExtra("status", status);
        intent.putExtras(extras);
        context.sendBroadcast(intent);
    }

    /************* Service **********/

    private final BroadcastReceiver gpsReceiver = new BroadcastReceiver() {
        public void onReceive(Context arg0, Intent arg1) {
            switch (arg1.getAction()){
                case ACTION_GPS_START:
                    start(arg1.getLongExtra("milis", (long)1000), arg1.getLongExtra("minDist", (long)0));
                    break;
                case ACTION_GPS_STOP:
                    stop();
                    break;
            }
        }
    };

    @Override
    public void onCreate(){
        this.context = getBaseContext();
        SERVICE_CONNECTED = true;
        setFilter();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(context, "GPS service finalizado", Toast.LENGTH_SHORT).show();
        unregisterReceiver(gpsReceiver);
        SERVICE_CONNECTED = false;
        super.onDestroy();
    }

    private void setFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_GPS_START);
        filter.addAction(ACTION_GPS_STOP);
        registerReceiver(gpsReceiver, filter);
    }

    public class GPSBinder extends Binder {
        public GPSService getService() {
            return GPSService.this;
        }
    }

    @SuppressLint("MissingPermission")
    public void start(long milis, long dist){
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, milis, dist, this);
    }

    public void stop(){
        locationManager.removeUpdates(this);
    }

    public boolean isConnected(){
        return SERVICE_CONNECTED;
    }

}


