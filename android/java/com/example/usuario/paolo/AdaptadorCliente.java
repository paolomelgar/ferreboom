package com.example.usuario.paolo;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdaptadorCliente extends ArrayAdapter {
    TextView clien;
    Activity context;
    ArrayList<TitularCliente> datos;
    ListView list;

    public AdaptadorCliente(Activity paramActivity, ArrayList<TitularCliente> paramArrayList, ListView paramListView, TextView paramTextView) {
        super(paramActivity, R.layout.adaptador_cliente, paramArrayList);
        this.datos = paramArrayList;
        this.context = paramActivity;
        this.list = paramListView;
        this.clien = paramTextView;
    }

    public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup) {
        View localView = context.getLayoutInflater().inflate(R.layout.adaptador_cliente, null);
        ((TextView)localView.findViewById(R.id.cli)).setText(datos.get(paramInt).getCliente());
        ((TextView)localView.findViewById(R.id.dir)).setText(datos.get(paramInt).getDireccion());
        localView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                list.setVisibility(View.GONE);
                clien.setText(datos.get(paramInt).getCliente());
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
        return localView;
    }
}