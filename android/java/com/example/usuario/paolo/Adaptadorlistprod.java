package com.example.usuario.paolo;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Adaptadorlistprod extends ArrayAdapter {
    Activity context;
    ArrayList<Titularlistprod> datos;

    public Adaptadorlistprod(Activity paramActivity, ArrayList<Titularlistprod> datos)
    {
        super(paramActivity, R.layout.adaptadorproductos, datos);
        this.context = paramActivity;
        this.datos = datos;
    }

    public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup)
    {
        View localView = context.getLayoutInflater().inflate(R.layout.adaptadorproductos, null);
        ((TextView)localView.findViewById(R.id.producto)).setText(datos.get(paramInt).getProducto());
        ((TextView)localView.findViewById(R.id.cant)).setText(datos.get(paramInt).getCant());
        ((TextView)localView.findViewById(R.id.unit)).setText(datos.get(paramInt).getUnit());
        ((TextView)localView.findViewById(R.id.imp)).setText(datos.get(paramInt).getImp());
        if (paramInt % 2 == 0)
            localView.setBackgroundColor(Color.parseColor("#FFEDFAFF"));
        return localView;
    }
}
