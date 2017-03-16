package com.example.usuario.paolo;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorAdelanto extends ArrayAdapter
{
    Activity context;
    ArrayList<TitularAdelanto> datos;

    public AdaptadorAdelanto(Activity paramActivity,ArrayList<TitularAdelanto> datos)
    {
        super(paramActivity, R.layout.adaptadelantos, datos);
        this.context = paramActivity;
        this.datos = datos;
    }

    public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup)
    {
        View localView = context.getLayoutInflater().inflate(R.layout.adaptadelantos, null);
        ((TextView)localView.findViewById(R.id.fecha)).setText(datos.get(paramInt).getFecha());
        ((TextView)localView.findViewById(R.id.total)).setText("S/. "+datos.get(paramInt).getTotal());
        ((TextView)localView.findViewById(R.id.encargado)).setText(datos.get(paramInt).getEncargado());
        if (paramInt % 2 == 0)
            localView.setBackgroundColor(Color.parseColor("#FFEDFAFF"));
        return localView;
    }
}