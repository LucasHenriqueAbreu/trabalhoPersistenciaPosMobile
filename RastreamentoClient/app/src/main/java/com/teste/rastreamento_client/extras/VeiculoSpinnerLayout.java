package com.teste.rastreamento_client.extras;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityManagerCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.teste.rastreamento_client.R;
import com.teste.rastreamento_client.model.Veiculo;

import java.util.List;

/**
 * Created by Paulo on 25/11/2017.
 */

public class VeiculoSpinnerLayout extends BaseAdapter implements SpinnerAdapter {

    private final Context context;
    private final List<Veiculo> lista;

    public VeiculoSpinnerLayout(Context context, List<Veiculo> lista){
        this.context = context;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int i) {
        return lista.get(i);
    }

    @Override
    public long getItemId(int i) {
        return lista.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView txt = new TextView(context);
        txt.setText(lista.get(i).getCod());

        txt.setGravity(Gravity.LEFT);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(16);
        //txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down, 0);
        txt.setTextColor(Color.parseColor("#000000"));
        return txt;
    }
}
