package com.teste.rastreamento_client.extras;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.teste.rastreamento_client.R;
import com.teste.rastreamento_client.model.Rota;
import com.teste.rastreamento_client.model.Veiculo;

import java.util.List;

/**
 * Created by Paulo on 25/11/2017.
 */

public class RotasSpinnerLayout extends BaseAdapter implements SpinnerAdapter {

    private final Context context;
    private final List<Rota> lista;
    private LayoutInflater inflter;

    public RotasSpinnerLayout(Context context, List<Rota> lista){
        this.context = context;
        this.lista = lista;
        inflter = (LayoutInflater.from(context));
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
        view = inflter.inflate(R.layout.layout_spinner_titulo_subtitulo, null);

        TextView titulo = view.findViewById(R.id.textTitulo);
        TextView subTitulo = view.findViewById(R.id.textSubTitulo);

        titulo.setText(lista.get(i).getDestino());
        subTitulo.setText(lista.get(i).getDescricao());

        view.setPadding(16, 20, 16, 20);

        return view;
    }


}
