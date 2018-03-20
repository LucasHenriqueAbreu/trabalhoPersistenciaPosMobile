package com.teste.rastreamento_client.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.teste.rastreamento_client.ConexaoInternet.JSONConversor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Paulo on 23/11/2017.
 */

public class BancoHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "RASTREAMENTO.DB";
    private SQLiteDatabase banco;
    private Gson aux = new Gson();

    /**
     *  -> Veiculo
     */
    private static final String tablename_1 = "VEICULO";
    private static final Map<String, String> columns_1 = new HashMap<String, String>();
    static {
        columns_1.put("ID", "LONG PRIMARY KEY NOT NULL");
        columns_1.put("COD", "VARCHAR(20)");
    };

    /** --------------------------------------------------------------------- */

    /**
     *  -> Rota
     */
    private static final String tablename_2 = "ROTAVEICULO";
    private static final Map<String, String> columns_2 = new HashMap<String, String>();
    static {
        columns_2.put("ID", "LONG PRIMARY KEY NOT NULL");
        columns_2.put("IDVEICULO", "LONG");
        columns_2.put("DESTINO", "VARCHAR(100)");
        columns_2.put("DESCRICAO", "VARCHAR(100)");
        columns_2.put("PONTOS", "BLOB");
    };

    /** --------------------------------------------------------------------- */

    protected BancoHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql = "CREATE TABLE "+this.tablename_1+"(";
        Set<String> it = this.columns_1.keySet();
        int cont=0;
        for(String s : it) {
            cont++;
            sql += s + " " +this.columns_1.get(s) + ((it.size() > cont) ? ", " : ")" );
        }
        sqLiteDatabase.execSQL(sql);

        sql = "CREATE TABLE "+this.tablename_2+"(";
        it = this.columns_2.keySet();
        cont=0;
        for(String s : it) {
            cont++;
            sql += s + " " +this.columns_2.get(s) + ((it.size() > cont) ? ", " : ")" );
        }
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if(oldVersion != newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + this.tablename_1);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + this.tablename_2);
            onCreate(sqLiteDatabase);
        }
    }

    protected long inserir(String tablename, Map<String, Object> valores){
        ContentValues contentValues = new ContentValues();
        long resultado = -1;

        Set<String> it = valores.keySet();
        for(String s : it) {
            if(valores.get(s) instanceof String) contentValues.put(s, (String) valores.get(s));
            else if(valores.get(s) instanceof Integer) contentValues.put(s, (Integer) valores.get(s));
            else if(valores.get(s) instanceof Double) contentValues.put(s, (Double) valores.get(s));
            else if(valores.get(s) instanceof Long) contentValues.put(s, (Long) valores.get(s));
            else if(valores.get(s) instanceof ArrayList) contentValues.put(s, aux.toJson(valores.get(s)));
            else Log.d("InserirCriaTabela", s+ " : "+valores.get(s).getClass().toString());
        }

        try {
            this.banco = this.getWritableDatabase();
            resultado = this.banco.insert(tablename, null, contentValues);
        } catch (Exception e) {
            Log.d("Insert "+tablename, e.getMessage());
        } finally {
            this.banco.close();
        }

        return resultado;
    }

    private void atualizarBanco(){

    }

    protected List<Map<String, Object>> buscar(String tablename, String clause){

        Set<String> col;

        if(tablename == this.tablename_1) col = columns_1.keySet();
        else if(tablename == this.tablename_2) col = columns_2.keySet();
        else col = null;

        String[] colunas = col.toArray(new String[col.size()]);

        banco = this.getWritableDatabase();

        Cursor cursor = banco.query(tablename, colunas, clause, null, null, null, null);

        if(cursor!= null){
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            while(cursor.moveToNext()){
                Map<String, Object> temp = new HashMap<String, Object>();

                for(String s:col) {
                    switch (cursor.getType(cursor.getColumnIndex(s))){
                        case Cursor.FIELD_TYPE_INTEGER:
                            temp.put(s, cursor.getLong(cursor.getColumnIndex(s)));
                            break;
                        case Cursor.FIELD_TYPE_FLOAT:
                            temp.put(s, cursor.getDouble(cursor.getColumnIndex(s)));
                            break;
                        case Cursor.FIELD_TYPE_STRING:
                            temp.put(s, cursor.getString(cursor.getColumnIndex(s)));
                            break;
                        case Cursor.FIELD_TYPE_BLOB:
                            temp.put(s, cursor.getBlob(cursor.getColumnIndex(s)));
                            break;
                        case Cursor.FIELD_TYPE_NULL:
                            temp.put(s, null);
                            break;
                        default:
                            Log.d("CriarTabela-Buscar", "Tipo desconhecido...");
                    }
                }
                result.add(temp);
            }
            return result;
        } return null;
    }

    protected int deletar(String tablename, String col, String[] filtro){
        banco = this.getWritableDatabase();
        int result = banco.delete(tablename, col+" = ?", filtro);
        banco.close();
        return result;
    }

    protected int atualizar(String tablename, Long id, Map<String, Object> valores){
        ContentValues contentValues = new ContentValues();
        int resultado = -1;

        Set<String> it = valores.keySet();

        for(String s : it) {
            if(valores.get(s) instanceof String) contentValues.put(s, (String) valores.get(s));
            else if(valores.get(s) instanceof Integer) contentValues.put(s, (Integer) valores.get(s));
            else if(valores.get(s) instanceof Double) contentValues.put(s, (Double) valores.get(s));
            else if(valores.get(s) instanceof Long) contentValues.put(s, (Long) valores.get(s));
            else if(valores.get(s) instanceof ArrayList) contentValues.put(s, aux.toJson(valores.get(s)));
            else Log.d("InserirCriaTabela", s+ " : "+valores.get(s).getClass().toString());
        }
        try {
            this.banco = this.getWritableDatabase();
            resultado = this.banco.update(tablename, contentValues, "ID = ?", (new String[]{String.valueOf(id)}));
        } catch (Exception e) {
            Log.d("Insert "+tablename, e.getMessage());
        } finally {
            this.banco.close();
        }
        return (int)resultado;
    }
}
