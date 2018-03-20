package com.teste.rastreamento_client.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.teste.rastreamento_client.ConexaoInternet.JSONConversor;
import com.teste.rastreamento_client.model.Rota;
import com.teste.rastreamento_client.model.Veiculo;
import com.teste.rastreamento_client.repository.BancoHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Created by Paulo on 25/11/2017.
 */

public class RotaController extends BancoHelper {
    private SQLiteDatabase banco;
    private BancoHelper tabela;
    private static final String tablename = "ROTAVEICULO";
    private static final Map<String, String> columns = new HashMap<String, String>();
    static {
        columns.put("ID", "LONG PRIMARY KEY NOT NULL");
        columns.put("IDVEICULO", "LONG");
        columns.put("DESTINO", "VARCHAR(100)");
        columns.put("DESCRICAO", "VARCHAR(100)");
        columns.put("PONTOS", "BLOB");
    };

    public RotaController(Context context){
        super(context);
    }

    public Rota inserir(Map<String, Object> valores){

        if(super.inserir(this.tablename, valores) != -1)
            return new Rota(
                    Long.valueOf((Integer) valores.get("ID")),
                    Long.valueOf((Integer) valores.get("IDVEICULO")),
                    (String) valores.get("DESTINO"),
                    (String) valores.get("DESCRICAO"),
                    ((List<LatLng>) valores.get("PONTOS"))
            );
        else return null;
    }

    public List<Rota> buscar(String col, List<Object> filtro){

        List<Map<String, Object>> result;

        if(col!=null) result = super.buscar(this.tablename, col.replace("?", filtro.toString().replaceAll("(\\[|\\])","")));
        else result = super.buscar(this.tablename, null);

        if(result != null){
            List<Rota> rotaList = new ArrayList<Rota>();
            for(Map<String, Object> m : result){
                rotaList.add(convertToRota(m));
            }
            return rotaList;
        } else return null;
    }

    private Rota convertToRota(Map<String, Object> valores) {
        Rota rota = new Rota();

        if(valores.containsKey("IDVEICULO")) rota.setIdveiculo((Long)valores.get("IDVEICULO"));
        if(valores.containsKey("DESTINO")) rota.setDestino((String)valores.get("DESTINO"));
        if(valores.containsKey("DESCRICAO")) rota.setDescricao((String)valores.get("DESCRICAO"));
        if(valores.containsKey("ID")) rota.setId((Long)valores.get("ID"));
        try {
            if (valores.containsKey("PONTOS")) rota.setPontos(JSONConversor.convertToListaLatLng(new JSONArray((String) valores.get("PONTOS"))));
        }catch (Exception e) {return null;}
        return rota;
    }

    private Map<String, Object> convertToRotaMap(Rota rota){
        Map<String, Object> mapa = new HashMap<>();
        mapa.put("ID", rota.getId());
        mapa.put("DESTINO", rota.getDestino());
        mapa.put("DESCRICAO", rota.getDescricao());
        mapa.put("IDVEICULO", rota.getIdveiculo());
        mapa.put("PONTOS", rota.getPontos());
        return mapa;
    }

    public boolean atualizar(Rota rota){
        return (super.atualizar(this.tablename, rota.getId(), convertToRotaMap(rota)) == 1);
    }

    public boolean deletar(Rota rota){
        return (super.deletar(this.tablename, "ID", (new String[]{String.valueOf(rota.getId())})) == 1);
    }

}