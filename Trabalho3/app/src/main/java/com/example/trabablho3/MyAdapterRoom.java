package com.example.trabablho3;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trabablho3.Room.Todo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paulo on 10/03/2018.
 */

public class MyAdapterRoom extends RecyclerView.Adapter<MyAdapterRoom.ViewHolder> {
    private List<Todo> mDataset;

    public MyAdapterRoom(){
        this.mDataset = new ArrayList<Todo>();
    }

    public void add(Todo resp){
        this.mDataset.add(resp);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtNome;
        public TextView txtDescricao;

        public ViewHolder(View v) {
            super(v);
            txtNome = v.findViewById(R.id.txtNome);
            txtDescricao = v.findViewById(R.id.txtDescricao);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapterRoom(List<Todo> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapterRoom.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.txtNome.setText(mDataset.get(position).nome);
        holder.txtDescricao.setText(mDataset.get(position).descricao);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}