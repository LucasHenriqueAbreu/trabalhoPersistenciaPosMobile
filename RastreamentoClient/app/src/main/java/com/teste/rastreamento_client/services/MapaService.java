package com.teste.rastreamento_client.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.teste.rastreamento_client.ConexaoInternet.ConexaoInternet;
import com.teste.rastreamento_client.ConexaoInternet.JSONConversor;
import com.teste.rastreamento_client.enums.Url;
import com.teste.rastreamento_client.model.LocalizacaoVeiculo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Paulo on 13/11/2017.
 */

public class MapaService extends Service {
    /**
     * SAÍDA
     */
    public static final String ACTION_ATUALIZAR_PONTOS = "com.teste.rastreamento_client.MapaActivity.action.MAPA_ATUALIZAR_PONTOS";
    /**
     * ENTRADA
     */
    public static final String ACTION_SET_VEICULOS = "com.teste.rastreamento_client.MapaActivity.action.MAPA_SET_VEICULOS";

    public static boolean SERVICE_CONNECTED = false;
    private String veiculos = "1,2,3";
    private Timer timer;
    private Context context;
    private LocationManager locationManager;
    private IBinder binder = new MapaBinder();
    private LocationProvider locProvider;
    private Gson gson;
    private JSONConversor jsonConversor;
    Map<Long, LocalizacaoVeiculo> localizacoes;
    private Callback callback;

    private final BroadcastReceiver mapaReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            switch (arg1.getAction()) {
                case ACTION_SET_VEICULOS:
                    veiculos = arg1.getStringExtra("veiculos");
                    break;
            }
        }
    };

    @Override
    public void onCreate(){
        this.context = getBaseContext();
        MapaService.SERVICE_CONNECTED = true;
        setFilter();
        timer = new Timer();
        this.localizacoes = new HashMap<Long, LocalizacaoVeiculo>();
        this.gson = new Gson();
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
        super.onDestroy();
        MapaService.SERVICE_CONNECTED = false;
        unregisterReceiver(mapaReceiver);
        timer.cancel();
    }

    private void setFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SET_VEICULOS);
        context.registerReceiver(mapaReceiver, filter);
    }

    public void setCallback(Callback callback){this.callback = callback;}

    public class MapaBinder extends Binder {
        public MapaService getService() {
            return MapaService.this;
        }
    }

    public void startService(long milis){
        timer.scheduleAtFixedRate(new atualizarMapa(), 0, milis);
    }

    public void stop(){

    }

    private class atualizarMapa extends TimerTask {
        @Override
        public void run() {
            ConexaoInternet conn = new ConexaoInternet();
            conn.buscarPacote(context, Request.Method.GET, Url.LOCALIZACAO + veiculos, listenerLocalizacoes, listenerError);
        }
    }


    private Response.Listener listenerObject = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            Toast.makeText(getBaseContext(), "Envio OK...", Toast.LENGTH_SHORT).show();
        }
    };

    private Response.Listener listenerLocalizacoes = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            /**
             * Realizar o processamento dos dados...
             */
            try {
                /**
                 * Executar o callback
                 */
                Map<String, Object>  lista = new HashMap<>();
                lista.put("pontos", response);

                if(callback!=null) callback.executar(lista);
                else Toast.makeText(context, "Sem callback", Toast.LENGTH_SHORT).show();

            } catch(Exception e){
                e.printStackTrace();
            }
            //Toast.makeText(getBaseContext(), "Envio OK...", Toast.LENGTH_SHORT).show();
        }
    };

    private Response.ErrorListener listenerError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getBaseContext(), "Erro ao receber...", Toast.LENGTH_SHORT).show();
        }
    };

    public interface Callback {
        void executar(Map<String, Object> args0);
    }
}
