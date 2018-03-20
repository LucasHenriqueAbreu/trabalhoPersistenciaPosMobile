package com.teste.rastreamento_client.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.teste.rastreamento_client.model.Veiculo;
import com.teste.rastreamento_client.repository.BancoHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Paulo on 25/11/2017.
 */

public class VeiculoController extends BancoHelper {
    private SQLiteDatabase banco;
    private BancoHelper tabela;
    private static final String tablename = "VEICULO";
    private static final Map<String, String> columns = new HashMap<String, String>();
    static {
        columns.put("ID", "LONG PRIMARY KEY NOT NULL");
        columns.put("COD", "VARCHAR(20)");
    };

    public VeiculoController(Context context){
        super(context);
    }

    public Veiculo inserir(Map<String, Object> valores){
        if(super.inserir(this.tablename, valores) != -1)
            return new Veiculo(
                    new Long((Integer)valores.get("ID")),
                    (String) valores.get("COD")
            );
        else return null;
    }

    public Veiculo inserir(Veiculo vei){
        if(super.inserir(this.tablename, convertToMap(vei)) != -1) return vei;
        else return null;
    }

    public List<Veiculo> buscar(String col, List<Object> filtro){

        List<Map<String, Object>> result;

        if(col!=null) result = super.buscar(this.tablename, col.replace("?", filtro.toString().replaceAll("(\\[|\\])","")));
        else result = super.buscar(this.tablename, null);

        if(result != null){
            List<Veiculo> veiList = new ArrayList<Veiculo>();
            for(Map<String, Object> m : result){
                veiList.add(convertToVeiculo(m));
            }
            return veiList;
        } else return null;
    }

    private Veiculo convertToVeiculo(Map<String, Object> valores){
        Set<String> keys = valores.keySet();
        Veiculo vei = new Veiculo();

        for(String s : keys){
            switch (s){
                case "ID":
                    vei.setId((Long)valores.get(s));
                    break;
                case "COD":
                    vei.setCod((String)valores.get(s));
                    break;
                default:
                    Log.d("InsVei.ColUnknown", s);
            }
        }

        return vei;
    }

    private Map<String, Object> convertToMap(Veiculo vei){
        Map<String, Object> map = new HashMap<>();
        map.put("ID", vei.getId());
        map.put("COD", vei.getCod());
        return map;
    }

    public boolean atualizar(Veiculo vei, Map<String, Object> valores){
        return (super.atualizar(this.tablename, vei.getId(), valores) == 1);
    }

    public boolean deletar(Veiculo vei){
        return (super.deletar(this.tablename, "ID", (new String[]{String.valueOf(vei.getId())})) == 1);
    }

}
