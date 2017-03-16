package com.example.usuario.paolo;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class AdaptadorTitulares extends ArrayAdapter
{
    AdaptadorProd adap1;
    Activity context;
    ArrayList<Titular> datos;
    ArrayList<TitularProd> datosprod;
    ListView list;
    ListView listprod;
    EditText prod;
    TextView sum,id;
    Integer m;

    public AdaptadorTitulares(Activity paramActivity, ArrayList<Titular> paramArrayList, ArrayList<TitularProd> paramArrayList1, ListView paramListView1, ListView paramListView2, AdaptadorProd paramAdaptadorProd, TextView paramTextView, EditText paramEditText)
    {
        super(paramActivity, R.layout.adaptador_personal_lista_1, paramArrayList);
        datos = paramArrayList;
        context = paramActivity;
        list = paramListView1;
        listprod = paramListView2;
        datosprod = paramArrayList1;
        adap1 = paramAdaptadorProd;
        sum = paramTextView;
        prod = paramEditText;
    }

    public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup)
    {
        final View localView = context.getLayoutInflater().inflate(R.layout.adaptador_personal_lista_1, null);
        ((TextView)localView.findViewById(R.id.titulo)).setText(datos.get(paramInt).getTitulo());
        ((TextView)localView.findViewById(R.id.s)).setText(datos.get(paramInt).getDescripcion());
        ((TextView)localView.findViewById(R.id.p)).setText(datos.get(paramInt).getPrecio());
        ((TextView)localView.findViewById(R.id.e)).setText(datos.get(paramInt).getEspecial());
        id= (TextView) localView.findViewById(R.id.id);
        id.setText(datos.get(paramInt).getId());
        ImageView localImageView = (ImageView)localView.findViewById(R.id.imageView);
        m=context.getResources().getIdentifier("a"+datos.get(paramInt).getId(), "drawable", context.getPackageName());
        if(m ==0) {
            localImageView.setImageResource(R.drawable.nodisponible);
        }else{
            localImageView.setImageResource(datos.get(paramInt).getImg());
        }
        if(Integer.valueOf(datos.get(paramInt).getDescripcion())<1){
            localView.setBackgroundColor(Color.parseColor("#FFFFABAF"));
        }
        localImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                Dialog localDialog = new Dialog(context);
                localDialog.setContentView(R.layout.custom);
                localDialog.setTitle(datos.get(paramInt).getTitulo());
                if (datos.get(paramInt).getImg()==0) {
                    ((ImageView) localDialog.findViewById(R.id.ima)).setImageResource(R.drawable.nodisponible);
                }else{
                    ((ImageView) localDialog.findViewById(R.id.ima)).setImageResource(datos.get(paramInt).getImg());
                }
                ((TextView) localDialog.findViewById(R.id.precio)).setText("STOCK: " + datos.get(paramInt).getDescripcion() + "   -   S/. " + datos.get(paramInt).getPrecio());
                localDialog.show();
            }
        });
        localView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                int i = 0;
                for (int j = 0; j < datosprod.size(); j++) {
                    if (datos.get(paramInt).getTitulo().equals(datosprod.get(j).getTitulo())) {
                        Toast localToast = Toast.makeText(context, "Este producto ya esta en la Lista", Toast.LENGTH_SHORT);
                        localToast.getView().setBackgroundResource(R.drawable.toast_drawable);
                        localToast.show();
                        i++;
                    }
                }
                if (i == 0) {
                    datosprod.add(0, new TitularProd(datos.get(paramInt).getTitulo(), "", datos.get(paramInt).getPrecio(), "0.00", datos.get(paramInt).getId(),datos.get(paramInt).getDescripcion(),datos.get(paramInt).getPrecio()));
                    listprod.setAdapter(adap1);
                    list.setVisibility(View.GONE);
                    listprod.setVisibility(View.VISIBLE);
                    sum.setVisibility(View.VISIBLE);
                    prod.setText(" ");
                }
            }
        });
        return localView;
    }
}