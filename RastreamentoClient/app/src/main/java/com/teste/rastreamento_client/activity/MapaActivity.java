package com.teste.rastreamento_client.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.teste.rastreamento_client.ConexaoInternet.ConexaoInternet;
import com.teste.rastreamento_client.ConexaoInternet.JSONConversor;
import com.teste.rastreamento_client.R;
import com.teste.rastreamento_client.enums.Url;
import com.teste.rastreamento_client.extras.ExpandableListAdapter;
import com.teste.rastreamento_client.extras.LatLngInterpolator;
import com.teste.rastreamento_client.extras.MarkerAnimation;
import com.teste.rastreamento_client.extras.RotasSpinnerLayout;
import com.teste.rastreamento_client.extras.VeiculoSpinnerLayout;
import com.teste.rastreamento_client.model.LocalizacaoVeiculo;
import com.teste.rastreamento_client.model.Rota;
import com.teste.rastreamento_client.model.Veiculo;
import com.teste.rastreamento_client.repository.RotaController;
import com.teste.rastreamento_client.repository.VeiculoController;
import com.teste.rastreamento_client.services.GPSService;
import com.teste.rastreamento_client.services.MapaService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Paulo on 12/11/2017.
 */

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mapa;
    private MapaService mapaService;
    private GPSService gpsService;
    private final LatLng padrao = new LatLng(-26.227209, -52.671724);
    private Bitmap marcador_on;
    private Bitmap marcador_off;
    private Thread atualizar;
    private JSONConversor jsonConversor;
    private Map<Long, LocalizacaoVeiculo> mapaLocalizacao;
    private Map<Long, Marker> mapaMarcador;
    private Map<Long, Veiculo> mapaVeiculo;
    private RotaController rotaController;
    private List<Rota> listaRota;
    private VeiculoController veiculoController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        marcador_on = getBitmap(R.drawable.ic_onibus_on_24dp);
        marcador_off = getBitmap(R.drawable.ic_onibus_off_24dp);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_mapa);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return false;
            }
        });

        this.mapaMarcador = new HashMap<>();
        this.mapaLocalizacao = new HashMap<>();
        this.mapaVeiculo = new HashMap<>();
        this.veiculoController = new VeiculoController(getBaseContext());
        this.rotaController = new RotaController(getBaseContext());

        /*List<LatLng> listaPontos = new ArrayList<>();
        listaPontos.add(new LatLng(-26.223860, -52.678183));
        listaPontos.add(new LatLng(-26.230405, -52.674299));
        listaPontos.add(new LatLng(-26.229933, -52.673355));
        listaPontos.add(new LatLng(-26.230029, -52.672722));
        listaPontos.add(new LatLng(-26.228787, -52.672609));
        listaPontos.add(new LatLng(-26.227314, -52.673467));
        listaPontos.add(new LatLng(-26.226395, -52.673939));
        listaPontos.add(new LatLng(-26.224446, -52.675098));
        listaPontos.add(new LatLng(-26.222923, -52.676072));

        Rota rot = rotaController.buscar("ID = ?", Arrays.<Object>asList(2)).get(0);
        rot.setPontos(listaPontos);
        rotaController.atualizar(rot);*/


        FloatingActionButton btnAux = (FloatingActionButton) findViewById(R.id.fab);
        btnAux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Rota> rota = rotaController.buscar(null, null);

                /*Veiculo vei = new Veiculo();
                vei.setCod("Veic.3");
                vei.setId(3L);
                veiculoController.inserir(vei);*/
                //mapa.addPolyline(new PolylineOptions().addAll(rota.getPontos()).color(Color.BLUE).jointType(JointType.DEFAULT).clickable(false));


            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initMenuLateral();
    }

    private void initMenuLateral(){
        List<String> listaHeaders = new ArrayList<String>();
        Map<String, List<String>> mapaMenus = new HashMap<String, List<String>>();
        Map<String, Object> mapaObjetos = new HashMap<String, Object>();

        /**
         * Header
         */
        listaHeaders.add("Rotas");
        mapaMenus.put("Rotas", new ArrayList<String>());
        listaHeaders.add("Veículos");
        mapaMenus.put("Veículos", new ArrayList<String>());

        /**
         * Menus
         */
        Spinner spRotas = new Spinner(this);
        spRotas.setAdapter(new RotasSpinnerLayout(this, rotaController.buscar(null, null)));
        spRotas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private Polyline rota;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /**
                 * Atualizar os Veiculos do outro Spinner...
                 */
                Rota obj = ((Rota)parent.getItemAtPosition(position));
                Toast.makeText(getBaseContext(), "Rotas-:"+obj.getDescricao(), Toast.LENGTH_SHORT).show();

                if(this.rota != null) rota.remove();
                this.rota = mapa.addPolyline(new PolylineOptions().addAll(obj.getPontos()).color(Color.BLUE).jointType(JointType.DEFAULT).clickable(false));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mapaObjetos.put("Rotas/Bairros", spRotas);

        Spinner spVeiculos = new Spinner(this);
        spVeiculos.setAdapter(new VeiculoSpinnerLayout(this, veiculoController.buscar(null, null)));
        spVeiculos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /**
                 * Atualizar os Veiculos do outro Spinner...
                 */
                Veiculo veiculo = (Veiculo)parent.getItemAtPosition(position);
                Toast.makeText(getBaseContext(), "Veiculos-:"+veiculo.getCod(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mapaObjetos.put("Veículos", spVeiculos);

        /**
         * Mapa dos Menus
         */
        mapaMenus.get("Rotas").add("Rotas/Bairros");
        mapaMenus.get("Veículos").add("Veículos");


        ExpandableListView Menus = findViewById(R.id.elvMenuLateral);
        Menus.setAdapter(new ExpandableListAdapter(this, listaHeaders, mapaMenus, mapaObjetos));
        for(int s = 0; s < listaHeaders.size();s++) Menus.expandGroup(s);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        setMapaFilters();
        startService(GPSService.class, gpsConnection, null);
        startService(MapaService.class, mapaConnection, null);
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopService(new Intent(getBaseContext(), MapaService.class));
        stopService(new Intent(getBaseContext(), GPSService.class));
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(mapaReceiver);
        unbindService(mapaConnection);
        unbindService(gpsConnection);
        stopService(new Intent(getBaseContext(), MapaService.class));
        stopService(new Intent(getBaseContext(), GPSService.class));
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.mapa = map;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(padrao, 13));
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        setMapaFilters();
        startService(MapaService.class, mapaConnection, null);
        if (requestCode == 1) {
            int aux = 0;
            for (int i : grantResults) {
                switch (i) {
                    case PackageManager.PERMISSION_GRANTED:
                        Log.d("PermissionGRANTED:", permissions[aux++]);
                        setMapaFilters();
                        startService(GPSService.class, gpsConnection, null);
                        mapa.setMyLocationEnabled(true);
                        break;
                    case PackageManager.PERMISSION_DENIED:
                        Log.d("PermissionDENIED:", permissions[aux++]);
                        break;
                }
            }
        }
    }

    // Convert a view to bitmap
    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(50, 50, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    private void setMapaFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(MapaService.ACTION_ATUALIZAR_PONTOS);
        filter.addAction(GPSService.ACTION_GPS_PROVIDER_ENABLED);
        filter.addAction(GPSService.ACTION_GPS_PROVIDER_DISABLED);
        filter.addAction(GPSService.ACTION_GPS_STATUS_CHANGE);
        registerReceiver(mapaReceiver, filter);
    }


    public void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (service.equals(MapaService.class) && !MapaService.SERVICE_CONNECTED) {
            Intent startService = new Intent(this.getBaseContext(), service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            startService(startService);
        } else if (service.equals(GPSService.class) && !GPSService.SERVICE_CONNECTED) {
            Intent startService = new Intent(this.getBaseContext(), service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            startService(startService);
        }
        Intent bindingIntent = new Intent(this.getBaseContext(), service);
        bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private final ServiceConnection mapaConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            mapaService = ((MapaService.MapaBinder) arg1).getService();
            mapaService.setCallback(callbackAtualizarPontos);
            mapaService.startService(5000);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mapaService = null;
        }
    };

    private final ServiceConnection gpsConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            gpsService = ((GPSService.GPSBinder) arg1).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            gpsService = null;
        }
    };

    private final BroadcastReceiver mapaReceiver = new BroadcastReceiver() {
        public void onReceive(Context arg0, Intent arg1) {
            switch (arg1.getAction()){
                case Manifest.permission.ACCESS_FINE_LOCATION:
                    //start(arg1.getLongExtra("milis", 1000), arg1.getLongExtra("minDist", 0));
                    if(arg1.getExtras().getBoolean(Manifest.permission.ACCESS_FINE_LOCATION)){
                        Toast.makeText(getBaseContext(), "Permissão aceita", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getBaseContext(), "Permissão negada", Toast.LENGTH_LONG).show();
                    }
                    break;
                case MapaService.ACTION_ATUALIZAR_PONTOS:

                    break;
            }
        }
    };

    private Bitmap getBitmap(int drawableRes){
        try {
            Drawable draw = getResources().getDrawable(drawableRes);
            Canvas canvas = new Canvas();
            Bitmap bitmap = Bitmap.createBitmap(draw.getIntrinsicWidth(), draw.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            canvas.setBitmap(bitmap);
            draw.setBounds(0, 0, draw.getIntrinsicWidth(), draw.getIntrinsicHeight());
            draw.draw(canvas);
            return bitmap;
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private Response.Listener listenerObject = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            /*Intent intent = new Intent(ACTION_CONNECTION_RESPONSE);
            intent.putExtra("tipo", "object");
            intent.putExtra("pacote", response.toString());
            contexto.sendBroadcast(intent);*/
            Toast.makeText(getBaseContext(), "Envio OK...", Toast.LENGTH_SHORT).show();
        }
    };

    private Response.Listener listenerArray = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            /*Intent intent = new Intent(ACTION_CONNECTION_RESPONSE);
            intent.putExtra("tipo", "array");
            intent.putExtra("pacote", response.toString());
            contexto.sendBroadcast(intent);*/
            Toast.makeText(getBaseContext(), "Envio OK...", Toast.LENGTH_SHORT).show();
        }
    };

    private Response.ErrorListener listenerError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            /*Intent intent = new Intent(ACTION_CONNECTION_RESPONSE);
            intent.putExtra("tipo", "error");
            intent.putExtra("pacote", error.toString());
            contexto.sendBroadcast(intent);*/
            Toast.makeText(getBaseContext(), "Erro ao receber...", Toast.LENGTH_SHORT).show();
        }
    };

    private MapaService.Callback callbackAtualizarPontos = new MapaService.Callback() {
        @Override
        public void executar(Map<String, Object> args0) {
            try {
                List<LocalizacaoVeiculo> lista = jsonConversor.convertToLocalizacaoArray(new JSONArray(args0.get("pontos").toString()));
                for(LocalizacaoVeiculo item : lista) mapaLocalizacao.put(item.getVeiculo().getId(), item);

                Set<Long> iterator = mapaLocalizacao.keySet();
                for(Long v : iterator)
                    if (mapaMarcador.get(v) != null){
                        MarkerAnimation.animateMarkerToICS(mapaMarcador.get(v), mapaLocalizacao.get(v).getPonto(), new LatLngInterpolator.Linear());
                    } else {
                        mapaVeiculo.put(v, veiculoController.buscar("id=?", Arrays.asList((Object) v)).get(0));
                        mapaMarcador.put(v, mapa.addMarker(new MarkerOptions()
                                .position(mapaLocalizacao.get(v).getPonto())
                                .title(mapaVeiculo.get(v).getCod())
                                .snippet(mapaLocalizacao.get(v).getDatahora().toString())
                                .icon(BitmapDescriptorFactory.fromBitmap(marcador_on))));
                    }

            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    };

}
