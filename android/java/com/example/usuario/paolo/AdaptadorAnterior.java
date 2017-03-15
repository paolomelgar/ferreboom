package com.example.usuario.paolo;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorAnterior extends ArrayAdapter {
    Activity context;
    ArrayList<TitularAnterior> datos;
    ListView list;

    public AdaptadorAnterior(Activity paramActivity, ArrayList<TitularAnterior> paramArrayList, ListView paramListView) {
        super(paramActivity, R.layout.adaptador_anterior, paramArrayList);
        datos = paramArrayList;
        context = paramActivity;
        list = paramListView;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        View localView = this.context.getLayoutInflater().inflate(R.layout.adaptador_anterior, null);
        ((TextView)localView.findViewById(R.id.fecha)).setText(datos.get(paramInt).getFecha());
        ((TextView)localView.findViewById(R.id.canti)).setText(datos.get(paramInt).getCantidad());
        ((TextView)localView.findViewById(R.id.produ)).setText(datos.get(paramInt).getProducto());
        ((TextView)localView.findViewById(R.id.precio)).setText(datos.get(paramInt).getPrecio());
        return localView;
    }
}