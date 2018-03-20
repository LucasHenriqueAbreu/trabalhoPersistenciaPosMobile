package com.teste.rastreamento_client.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.teste.rastreamento_client.R;
import com.teste.rastreamento_client.extras.ExpandableListAdapter;
import com.teste.rastreamento_client.model.LocalizacaoVeiculo;
import com.teste.rastreamento_client.repository.RotaController;
import com.teste.rastreamento_client.repository.VeiculoController;
import com.teste.rastreamento_client.extras.RotasSpinnerLayout;
import com.teste.rastreamento_client.extras.VeiculoSpinnerLayout;
import com.teste.rastreamento_client.model.Rota;
import com.teste.rastreamento_client.model.Veiculo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private VeiculoController veiController;
    private RotaController rotaController;
    private List<Veiculo> listaVei;
    private Spinner spinner;
    private Spinner spinner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        veiController = new VeiculoController(this.getBaseContext());
        rotaController = new RotaController(this.getBaseContext());

        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MapaActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        spinner = findViewById(R.id.spVeiculos);
        spinner2 = findViewById(R.id.spRotas);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(getBaseContext(),"ItemSelected", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            }
        );

        Button btnBuscar = findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                listaVei = veiController.buscar(null,null);

                VeiculoSpinnerLayout adapter = new VeiculoSpinnerLayout(getBaseContext(), listaVei);
                spinner.setAdapter(adapter);
            }
        });


        Button btnBuscarRotas = findViewById(R.id.btnBuscarRotas);
        btnBuscarRotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Rota> listaRota = rotaController.buscar(null, null);

                RotasSpinnerLayout adapter = new RotasSpinnerLayout(getBaseContext(), listaRota);
                spinner2.setAdapter(adapter);
            }
        });

        // -----------------------------------------
        initMenuLateral(getBaseContext());
    }

    private void initMenuLateral(Context context){
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
        RotasSpinnerLayout spRotas = new RotasSpinnerLayout(context, rotaController.buscar(null, null));
        mapaObjetos.put("Rotas/Bairros", spRotas);
        VeiculoSpinnerLayout spVeiculos = new VeiculoSpinnerLayout(context, veiController.buscar(null, null));
        mapaObjetos.put("Veículos", spVeiculos);

        /**
         * Mapa dos Menus
         */
        mapaMenus.get("Rotas").add("Rotas/Bairros");
        mapaMenus.get("Veículos").add("Veículos");


        ExpandableListView Menus = findViewById(R.id.elvMenuLateral);
        Menus.setAdapter(new ExpandableListAdapter(context, listaHeaders, mapaMenus, mapaObjetos));
        for(int s = 0; s < listaHeaders.size();s++) Menus.expandGroup(s);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
