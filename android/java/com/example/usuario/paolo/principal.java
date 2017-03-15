package com.example.usuario.paolo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;

import java.net.URISyntaxException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class principal extends ActionBarActivity {
    AdaptadorTitulares adap;
    AdaptadorProd adap1;
    AdaptadorCliente adap2;
    AdaptadorAnterior adap3;
    ArrayList<Titular> adaptador = new ArrayList();
    ArrayList<TitularAnterior> adaptanterior = new ArrayList();
    ArrayList<TitularCliente> adaptcliente = new ArrayList();
    ArrayList<TitularProd> adaptprod = new ArrayList();
    TextView clien;
    SQLiteDatabase db;
    String id;
    ImageView imag;
    RelativeLayout lay;
    ListView listad;
    ListView listado;
    ListView listanterior;
    ListView listprod;
    TextView obser;
    EditText producto;
    SlidingDrawer sl;
    TextView sum;
    TextView text;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://ferreboom.sytes.net:3500");
        } catch (URISyntaxException e) {}
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.principal);
        mSocket.connect();
        text = ((TextView)findViewById(R.id.text2));
        sum = ((TextView)findViewById(R.id.suma));
        clien = ((TextView)findViewById(R.id.cliente));
        obser = ((TextView)findViewById(R.id.observacion));
        listprod = ((ListView)findViewById(R.id.listaprod));
        listad = ((ListView)findViewById(R.id.listacliente));
        listanterior = ((ListView)findViewById(R.id.listanterior));
        sl = ((SlidingDrawer)findViewById(R.id.bottom));
        adap3 = new AdaptadorAnterior(this, adaptanterior, listanterior);
        adap1 = new AdaptadorProd(this, adaptprod, listprod, sum, clien, adap3, listanterior, adaptanterior, sl);
        ImageButton localImageButton = (ImageButton)findViewById(R.id.busc);
        Bundle localBundle = getIntent().getExtras();
        String str = localBundle.getString("user");
        id = localBundle.getString("id");
        BdHelper localBdHelper = new BdHelper(this, "DBTotalVent", null, 1);
        db = localBdHelper.getWritableDatabase();
        if (!id.equals("0")) {
            listprod.setVisibility(View.VISIBLE);
            Cursor localCursor = db.rawQuery("SELECT * FROM Venta WHERE idventa=" + this.id, null);
            if ((localCursor != null) && (localCursor.getCount() != 0) && (localCursor.moveToFirst()))
                do
                    adaptprod.add(new TitularProd(localCursor.getString(localCursor.getColumnIndex("producto")), localCursor.getString(localCursor.getColumnIndex("cantidad")), localCursor.getString(localCursor.getColumnIndex("unitario")), localCursor.getString(localCursor.getColumnIndex("importe")), localCursor.getString(localCursor.getColumnIndex("idprod")), localCursor.getString(localCursor.getColumnIndex("sto")),localCursor.getString(localCursor.getColumnIndex("unit"))));
                while (localCursor.moveToNext());
            clien.setText(localBundle.getString("cliente"));
            sum.setText(localBundle.getString("total"));
            listprod.setAdapter(adap1);
        }
        text.setText(str);
        text.setVisibility(View.GONE);
        ActionBar localActionBar = getSupportActionBar();
        localActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2d4b7c")));
        localActionBar.setCustomView(R.layout.actionbar);
        localActionBar.setDisplayShowCustomEnabled(true);
        localActionBar.setDisplayShowHomeEnabled(true);
        lay = ((RelativeLayout)findViewById(R.id.lay));
        producto = ((EditText)findViewById(R.id.search));
        imag = ((ImageView)findViewById(R.id.imagg));
        listado = ((ListView)findViewById(R.id.lista));
        imag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView)
            {
                if (lay.getVisibility() == View.INVISIBLE) {
                    lay.animate().translationY(principal.this.lay.getHeight()).alpha(1.0F).setListener(new AnimatorListenerAdapter() {
                        public void onAnimationStart(Animator paramAnonymous2Animator) {
                            super.onAnimationStart(paramAnonymous2Animator);
                            lay.setVisibility(View.VISIBLE);
                        }
                    });
                    clien.requestFocus();
                }else {
                    lay.animate().translationY(0.0F).alpha(0.0F).setListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator paramAnonymous2Animator) {
                            super.onAnimationEnd(paramAnonymous2Animator);
                            principal.this.lay.setVisibility(View.INVISIBLE);
                        }
                    });
                    producto.requestFocus();
                }
            }
        });
        localImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                Buscar();
            }
        });
    }
    public void Buscar() {
        final ProgressDialog localProgressDialog = new ProgressDialog(this);
        adap2 = new AdaptadorCliente(this, adaptcliente, listad, clien);
        AsyncHttpClient localAsyncHttpClient = new AsyncHttpClient();
        String str = Global.ddns + "cliente.php";
        RequestParams localRequestParams = new RequestParams();
        localRequestParams.add("cliente", clien.getText().toString());
        localAsyncHttpClient.post(str, localRequestParams, new AsyncHttpResponseHandler() {

            public void onStart() {
                localProgressDialog.setMessage("Buscando Clientes...");
                localProgressDialog.show();
            }

            public void onSuccess(int paramAnonymousInt, Header[] paramAnonymousArrayOfHeader, byte[] paramAnonymousArrayOfByte) {
                if (paramAnonymousInt == 200)
                    try {
                        localProgressDialog.dismiss();
                        adap2.clear();
                        JSONArray localJSONArray = new JSONArray(new String(paramAnonymousArrayOfByte));
                        for (int i = 0; i < localJSONArray.length(); i++) {
                            String str1 = localJSONArray.getJSONObject(i).getString("cliente");
                            String str2 = localJSONArray.getJSONObject(i).getString("direccion");
                            adap2.add(new TitularCliente(str1, str2));
                        }
                        if (localJSONArray.length() > 0) {
                            listad.setAdapter(adap2);
                            listad.setVisibility(View.VISIBLE);
                        }else {
                            Toast localToast2 = Toast.makeText(principal.this.getApplicationContext(), "No se encontraron resultados", Toast.LENGTH_SHORT);
                            localToast2.getView().setBackgroundResource(R.drawable.toast_drawable);
                            localToast2.show();
                        }
                    }
                    catch (Exception localException) {
                        localProgressDialog.dismiss();
                        Toast localToast1 = Toast.makeText(principal.this.getApplicationContext(), "Error JSON", Toast.LENGTH_SHORT);
                        localToast1.getView().setBackgroundResource(R.drawable.toast_drawable);
                        localToast1.show();
                    }
            }

            public void onFailure(int paramAnonymousInt, Header[] paramAnonymousArrayOfHeader, byte[] paramAnonymousArrayOfByte, Throwable paramAnonymousThrowable) {
                localProgressDialog.dismiss();
                Toast localToast = Toast.makeText(principal.this.getApplicationContext(), "No se encuentra la Base de Datos", Toast.LENGTH_SHORT);
                localToast.getView().setBackgroundResource(R.drawable.toast_drawable);
                localToast.show();
            }
        });
    }
    public void Enviar() {
        if (!clien.getText().toString().equals("")) {
            final ProgressDialog localProgressDialog = new ProgressDialog(this);
            AsyncHttpClient localAsyncHttpClient = new AsyncHttpClient();
            String str = Global.ddns + "procesarventa.php";
            RequestParams localRequestParams = new RequestParams();
            localRequestParams.add("cliente", clien.getText().toString());
            localRequestParams.add("observacion", obser.getText().toString());
            localRequestParams.add("vendedor", text.getText().toString());
            localRequestParams.add("total", sum.getText().toString());
            localRequestParams.add("size", Integer.toString(adaptprod.size()));
            for (int i = 0; i < adaptprod.size(); i++) {
                localRequestParams.add("stock" + i, adaptprod.get(i).getStock());
                localRequestParams.add("unit" + i, adaptprod.get(i).getUnitario());
                localRequestParams.add("impor" + i, adaptprod.get(i).getImporte());
                localRequestParams.add("id" + i, adaptprod.get(i).getId());
            }
            localAsyncHttpClient.post(str, localRequestParams, new AsyncHttpResponseHandler() {

                public void onStart() {
                    localProgressDialog.setMessage("Enviando Pedido...");
                    localProgressDialog.show();
                }

                public void onSuccess(int paramAnonymousInt, Header[] paramAnonymousArrayOfHeader, byte[] paramAnonymousArrayOfByte) {
                    if (paramAnonymousInt == 200) {
                        try {
                            localProgressDialog.dismiss();
                            mSocket.emit("notificacion","");
                            Toast localToast2 = Toast.makeText(getApplicationContext(), "Pedido Enviado Correctamente \n Se procesara en breves momentos", Toast.LENGTH_LONG);
                            localToast2.getView().setBackgroundResource(R.drawable.toast_correct);
                            localToast2.show();
                            if (!id.equals("0")) {
                                db.execSQL("UPDATE TotalVenta SET entregado='SI' WHERE idventa='" + id + "'");
                            }
                            finish();
                        } catch (Exception localException) {
                            localProgressDialog.dismiss();
                            Toast localToast1 = Toast.makeText(getApplicationContext(), "Error JSON", Toast.LENGTH_SHORT);
                            localToast1.getView().setBackgroundResource(R.drawable.toast_drawable);
                            localToast1.show();
                        }
                    }
                }

                public void onFailure(int paramAnonymousInt, Header[] paramAnonymousArrayOfHeader, byte[] paramAnonymousArrayOfByte, Throwable paramAnonymousThrowable) {
                    localProgressDialog.dismiss();
                    Toast localToast = Toast.makeText(getApplicationContext(), "No se encuentra la Base de Datos", Toast.LENGTH_SHORT);
                    localToast.getView().setBackgroundResource(R.drawable.toast_drawable);
                    localToast.show();
                }
            });
        }else {
            if (lay.getVisibility() == View.INVISIBLE) {
                lay.animate().translationY(this.lay.getHeight()).alpha(1.0F).setListener(new AnimatorListenerAdapter() {
                    public void onAnimationStart(Animator paramAnonymousAnimator) {
                        super.onAnimationStart(paramAnonymousAnimator);
                        lay.setVisibility(View.VISIBLE);
                    }
                });
                this.clien.requestFocus();
            }
            Toast localToast = Toast.makeText(getApplicationContext(), "Escoger un Cliente", Toast.LENGTH_SHORT);
            localToast.getView().setBackgroundResource(R.drawable.toast_drawable);
            localToast.show();
        }
    }

    public void Guardar() {
        if (!clien.getText().toString().equals("")) {
            if (!id.equals("0")) {
                db.execSQL("UPDATE TotalVenta SET entregado='ANULADO' WHERE idventa='" + id + "'");
            }
            SharedPreferences localSharedPreferences = getSharedPreferences("data", 0);
            int i = localSharedPreferences.getInt("idventa", 1);
            if (db != null) {
                db.execSQL("INSERT INTO TotalVenta(_id,idventa,cliente,total,entregado,fecha) VALUES (null,'" + i + "','" + clien.getText().toString() + "','" + sum.getText().toString() + "','NO',date())");
                for (int k = 0; k < adaptprod.size(); k++) {
                    db.execSQL("INSERT INTO Venta(_id,idventa,cantidad,producto,unitario,importe,idprod,sto,unit) VALUES (null,'" + i + "','" + adaptprod.get(k).getStock() + "','" + adaptprod.get(k).getTitulo() + "','" + adaptprod.get(k).getUnitario() + "','" + adaptprod.get(k).getImporte() + "','" + adaptprod.get(k).getId() + "','" + adaptprod.get(k).getSto() + "','" + adaptprod.get(k).getUnit() + "')");
                    db.execSQL("UPDATE Precios SET stock=(stock-"+adaptprod.get(k).getStock()+") WHERE idprod='"+adaptprod.get(k).getId()+"'");
                }
            }
            int j = i + 1;
            SharedPreferences.Editor localEditor = localSharedPreferences.edit();
            localEditor.putInt("idventa", j);
            localEditor.apply();
            Toast localToast2 = Toast.makeText(getApplicationContext(), "Pedido Guardado Correctamente", Toast.LENGTH_LONG);
            localToast2.getView().setBackgroundResource(R.drawable.toast_correct);
            localToast2.show();
            finish();
        } else {
            if (lay.getVisibility() == View.INVISIBLE) {
                lay.animate().translationY(lay.getHeight()).alpha(1.0F).setListener(new AnimatorListenerAdapter() {
                    public void onAnimationStart(Animator paramAnonymousAnimator) {
                        super.onAnimationStart(paramAnonymousAnimator);
                        lay.setVisibility(View.VISIBLE);
                    }
                });
                clien.requestFocus();
            }
            Toast localToast1 = Toast.makeText(getApplicationContext(), "Escoger un Cliente", Toast.LENGTH_SHORT);
            localToast1.getView().setBackgroundResource(R.drawable.toast_drawable);
            localToast1.show();
        }
    }
    public void Iniciar() {
        if(producto.getText().toString().trim().length() != 0) {
            producto.clearFocus();
            adap = new AdaptadorTitulares(this, adaptador, adaptprod, listado, listprod, adap1, sum, producto);
            adaptador.clear();
            StringBuilder localStringBuilder = new StringBuilder();
            for (String str3 : producto.getText().toString().split(" "))
                localStringBuilder.append("producto LIKE '%").append(str3).append("%' AND ");
            String str1 = localStringBuilder.toString();
            String str2 = str1.substring(0, str1.length() - 4);
            Cursor localCursor = db.rawQuery("SELECT * FROM Precios WHERE " + str2 + " LIMIT 10", null);
            if ((localCursor != null) && (localCursor.getCount() != 0)) {
                if (localCursor.moveToFirst())
                    do
                        adaptador.add(new Titular(localCursor.getString(localCursor.getColumnIndex("producto")), localCursor.getString(localCursor.getColumnIndex("stock")), localCursor.getString(localCursor.getColumnIndex("unitario")), localCursor.getString(localCursor.getColumnIndex("especial")), localCursor.getString(localCursor.getColumnIndex("idprod")), getResources().getIdentifier("a" + localCursor.getString(localCursor.getColumnIndex("idprod")), "drawable", getPackageName())));
                    while (localCursor.moveToNext());
                listado.setAdapter(adap);
                listado.setVisibility(View.VISIBLE);
            }else{
                listado.setVisibility(View.GONE);
                Toast localToast1 = Toast.makeText(getApplicationContext(), "No se encontraron resultados ", Toast.LENGTH_SHORT);
                localToast1.getView().setBackgroundResource(R.drawable.toast_drawable);
                localToast1.show();
            }
            /*final ProgressDialog localProgressDialog = new ProgressDialog(this);
            AsyncHttpClient localAsyncHttpClient = new AsyncHttpClient();
            String str = Global.ddns + "producto.php";
            RequestParams localRequestParams = new RequestParams();
            localRequestParams.add("producto", producto.getText().toString());
            localAsyncHttpClient.post(str, localRequestParams, new AsyncHttpResponseHandler() {

                public void onStart() {
                    localProgressDialog.setMessage("Buscando Productos...");
                    localProgressDialog.show();
                }

                public void onSuccess(int paramAnonymousInt, Header[] paramAnonymousArrayOfHeader, byte[] paramAnonymousArrayOfByte) {
                    if (paramAnonymousInt == 200) {
                        try {
                            localProgressDialog.dismiss();
                            adaptador.clear();
                            JSONArray localJSONArray = new JSONArray(new String(paramAnonymousArrayOfByte));
                            for (int i = 0; i < localJSONArray.length(); i++) {
                                String str1 = localJSONArray.getJSONObject(i).getString("producto")+" "+localJSONArray.getJSONObject(i).getString("marca");
                                String str2 = localJSONArray.getJSONObject(i).getString("p_promotor");
                                String str3 = localJSONArray.getJSONObject(i).getString("p_especial");
                                String str4 = localJSONArray.getJSONObject(i).getString("stock_real");
                                String str5 = localJSONArray.getJSONObject(i).getString("id");
                                adaptador.add(new Titular(str1, str4, str2, str3, str5, getResources().getIdentifier("a" + str5, "drawable", getPackageName())));
                            }
                            if (localJSONArray.length() > 0) {
                                listado.setAdapter(adap);
                                listado.setVisibility(View.VISIBLE);
                            }else {
                                listado.setVisibility(View.GONE);
                                Toast localToast2 = Toast.makeText(getApplicationContext(), "No se encontraron resultados", Toast.LENGTH_SHORT);
                                localToast2.getView().setBackgroundResource(R.drawable.toast_drawable);
                                localToast2.show();
                            }
                        } catch (Exception localException) {
                            localProgressDialog.dismiss();
                            Toast localToast1 = Toast.makeText(getApplicationContext(), "Error JSON", Toast.LENGTH_SHORT);
                            localToast1.getView().setBackgroundResource(R.drawable.toast_drawable);
                            localToast1.show();
                        }
                    }
                }

                public void onFailure(int paramAnonymousInt, Header[] paramAnonymousArrayOfHeader, byte[] paramAnonymousArrayOfByte, Throwable paramAnonymousThrowable) {
                    localProgressDialog.dismiss();
                    Toast localToast = Toast.makeText(getApplicationContext(), "No se encuentra la Base de Datos", Toast.LENGTH_SHORT);
                    localToast.getView().setBackgroundResource(R.drawable.toast_drawable);
                    localToast.show();
                }
            });*/
        }else{
            listado.setVisibility(View.GONE);
        }
    }
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("Se Perdera la informacion si continua")
            .setMessage("Desea Continuar de todos Modos")
            .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                    principal.this.finish();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                    paramAnonymousDialogInterface.cancel();
                }
            }).show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.buscar){
            Iniciar();
        }
        else if (id == R.id.guardar){
            Guardar();
        }
        else{
            Enviar();
        }
        return super.onOptionsItemSelected(item);
    }
}
