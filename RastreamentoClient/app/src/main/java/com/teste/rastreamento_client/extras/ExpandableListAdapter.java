package com.teste.rastreamento_client.extras;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.teste.rastreamento_client.R;
import com.teste.rastreamento_client.model.Veiculo;
import com.teste.rastreamento_client.repository.RotaController;
import com.teste.rastreamento_client.repository.VeiculoController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Paulo on 05/12/2017.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter{
    private Context context;
    private List<String> listaDataHeader;
    private Map<String, List<String>> listaMap;
    private Map<String, Object> mapaItems;

    public ExpandableListAdapter(Context context, List<String> listaDataHeader, Map<String, List<String>> listaMap, Map<String, Object> mapaItems) {
        this.context = context;
        this.listaDataHeader = listaDataHeader;
        this.listaMap = listaMap;
        this.mapaItems = mapaItems;
    }

    @Override
    public int getGroupCount() {
        return listaMap.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listaMap.get(listaDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listaDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listaMap.get(listaDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_menu_headers, null);
        }
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.tvHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String child = (String) getChild(groupPosition, childPosition);
        final Object objeto = mapaItems.get(child);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(objeto instanceof Spinner){
                convertView = inflater.inflate(R.layout.layout_menu_items_spinner, null);

                TextView tvTitulo = convertView.findViewById(R.id.tvLabel);
                tvTitulo.setText(child);

                Spinner spItems = convertView.findViewById(R.id.spItems);
                spItems.setAdapter(((Spinner)objeto).getAdapter());
                spItems.setOnItemSelectedListener(((Spinner)objeto).getOnItemSelectedListener());
            }
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
