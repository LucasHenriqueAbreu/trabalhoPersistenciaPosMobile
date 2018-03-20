package com.teste.rastreamento_client.ConexaoInternet;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Paulo on 17/11/2017.
 */

public class ConexaoInternet extends AsyncTask<String,Integer,String>  {
    private Context contexto;
    public static final String ACTION_CONNECTION_RESPONSE = "com.teste.rastreamento_client.MapaActivity.action.ACTION_CONNECTION_RESPONSE";
    public static final String ACTION_CONNECTION_ERROR = "com.teste.rastreamento_client.MapaActivity.action.ACTION_CONNECTION_ERROR";
    private Response.Listener listenerObject;
    private Response.Listener listenerArray;
    private Response.ErrorListener listenerError;

    @Override
    protected void onPreExecute(){
        /**
         * Preparação para começar a enviar...
         */
        String coisa = "";
    }
    @Override
    protected String doInBackground(String... params) {
        /**
         * Chamada para o envio/recebimento
         */

        Log.d("Pacote",params[0]);
        JsonRequest request;
        try {
            if(params[0] == "array"){
                request = new JsonArrayRequest(
                        Integer.parseInt(params[1]),    // Método
                        params[2],                      // URL
                        new JSONArray(params[3]),      // Objeto
                        this.listenerArray,                     // Requisição foi bem sucedida
                        this.listenerError                   // Erro na requisição
                );
            } else {
                request = new JsonObjectRequest(
                        Integer.parseInt(params[1]),    // Método
                        params[2],                      // URL
                        new JSONObject(params[3]),      // Objeto
                        this.listenerObject,                     // Requisição foi bem sucedida
                        this.listenerError                   // Erro na requisição
                );
            }
            MySingleton.getInstance(contexto).addToRequestQueue(request);
        } catch (Exception e) {
            Intent intent = new Intent(ACTION_CONNECTION_ERROR);
            intent.putExtra("tipo", "Exception");
            intent.putExtra("msg", e.getMessage());
            contexto.sendBroadcast(intent);
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String numero){
        /**
         * Após o envio...
         */
        String coisa = "";
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

    }

    public void enviarPacote(Context context, int metodo, String url, HashMap<String,Object> pacote, Response.Listener listener, Response.ErrorListener errorListener) {
        this.contexto = context;
        this.listenerObject = listener;
        this.listenerError = errorListener;
        this.execute(new String[]{"enviar",String.valueOf(metodo), url, toPacote(pacote)});
    }

    public void buscarPacote(Context context, int metodo, String url, Response.Listener listener, Response.ErrorListener errorListener) {
        this.contexto = context;
        this.listenerArray = listener;
        this.listenerError = errorListener;
        this.execute(new String[]{"array",String.valueOf(metodo), url, "[{}]"});
    }


    public String toPacote(HashMap<String,Object> pacote){
        String result = "{", st;

        Iterator<String> it = pacote.keySet().iterator();

        while(it.hasNext()){
            st = it.next();
            result += "\""+st+"\":"+ (pacote.get(st).getClass().isInstance(String.class) ? "\""+pacote.get(st)+"\"": pacote.get(st).toString());
            result += it.hasNext() ? "," : "";
        }

        result += "}";

        return result;
    }
}
