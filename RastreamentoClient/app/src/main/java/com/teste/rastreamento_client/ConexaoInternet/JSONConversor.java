package com.teste.rastreamento_client.ConexaoInternet;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.teste.rastreamento_client.model.LocalizacaoVeiculo;
import com.teste.rastreamento_client.model.Veiculo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Paulo on 18/11/2017.
 */

public class JSONConversor {
    /**
     * Faz a conversão de JSON para LocalizacaoVeiculo
     */
    public static List<LocalizacaoVeiculo>  convertToLocalizacaoArray(JSONArray in) throws JSONException {
        List<LocalizacaoVeiculo> lista = new ArrayList<LocalizacaoVeiculo>();
        for(int i=0;i<in.length();i++){
            lista.add(convertToLocalizacao(in.getJSONObject(i)));
        }
        return lista;
    }

    public static LocalizacaoVeiculo  convertToLocalizacao(JSONObject in) {
        LocalizacaoVeiculo loc = new LocalizacaoVeiculo();

        try {
            if (in.has("id")) loc.setId(in.getLong("id"));
            if (in.has("veiculo")) loc.setVeiculo(convertToVeiculo(in.getJSONObject("veiculo")));
            if (in.has("latitude")) loc.setLatitude(in.getDouble("latitude"));
            if (in.has("longitude")) loc.setLongitude(in.getDouble("longitude"));
            if (in.has("datahora")) loc.setDatahora(new Date(in.getLong("datahora")));

            return loc;
        } catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    public static Veiculo convertToVeiculo(JSONObject in) throws JSONException {
        Veiculo vei = new Veiculo();
        try {
            if (in.has("id")) vei.setId(in.getLong("id"));
            if (in.has("cod")) vei.setCod(in.getString("cod"));

            return vei;
        } catch(JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<LatLng> convertToListaLatLng(JSONArray in) throws  JSONException {
        List<LatLng> lista = new ArrayList<>();
        for(int i=0;i<in.length();i++){
            lista.add(convertToLatLng(in.getJSONObject(i)));
        }
        return lista;
    }

    public static LatLng convertToLatLng(JSONObject in) throws JSONException {
        LatLng ponto = new LatLng(in.getDouble("latitude"),in.optDouble("longitude"));
        return ponto;
    }
}
