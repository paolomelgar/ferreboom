package com.example.usuario.paolo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;

import java.text.DecimalFormat;
import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;

public class pendiente extends ActionBarActivity {
    Adaptador adap;
    AdaptadorCliente adap2;
    ArrayList<TitularPendiente> adaptador = new ArrayList<TitularPendiente>();
    ArrayList<TitularCliente> adaptcliente = new ArrayList<TitularCliente>();
    ArrayList<TitularAcuenta> adaptacuenta = new ArrayList<TitularAcuenta>();
    EditText clie;
    ListView listadop;
    ListView listprod,listacuenta;
    TextView text,count;
    String user;
    SQLiteDatabase db;
    Button button;
    DecimalFormat localDecimalFormat;
    MyCursorAdapter localMyCursorAdapter;
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.pendiente);
        text = (TextView) findViewById(R.id.textus);
        listprod = (ListView)findViewById(R.id.listac);
        listadop = ((ListView)findViewById(R.id.listacl));
        listacuenta = (ListView) findViewById(R.id.listacuenta);
        count=(TextView) findViewById(R.id.count);
        button = (Button) findViewById(R.id.buttonn);
        user = getIntent().getExtras().getString("user");
        text.setText(user);
        text.setVisibility(View.GONE);
        ActionBar localActionBar = getSupportActionBar();
        localActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2d4b7c")));
        localActionBar.setCustomView(R.layout.actionbar);
        localActionBar.setDisplayShowCustomEnabled(true);
        localActionBar.setDisplayShowHomeEnabled(true);
        clie = ((EditText)findViewById(R.id.search));
        db = new BdHelper(this, "DBTotalVent", null, 1).getWritableDatabase();
        Cursor localCursor = db.rawQuery("SELECT * FROM Acuenta", null);
        localMyCursorAdapter = new MyCursorAdapter(this,adaptacuenta);
        if ((localCursor != null) && (localCursor.getCount() != 0) && (localCursor.moveToFirst()))
            do
                localMyCursorAdapter.add(new TitularAcuenta(localCursor.getString(localCursor.getColumnIndex("serie")), localCursor.getString(localCursor.getColumnIndex("cliente")), localCursor.getString(localCursor.getColumnIndex("total")), localCursor.getString(localCursor.getColumnIndex("acuenta"))));
            while (localCursor.moveToNext());
        listacuenta.setAdapter(localMyCursorAdapter);
        localDecimalFormat = new DecimalFormat("##.00");
        Cursor c = sumFoodColumn();
        Double total = c.getDouble(c.getColumnIndex("mytotal"));
        count.setText("  S/. " + localDecimalFormat.format(total) + "  ");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder localBuilder = new AlertDialog.Builder(pendiente.this);
                localBuilder.setTitle("Enviar").
                        setMessage("Esta seguro de Enviar!!!")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {
                                Cursor localCursor = db.rawQuery("SELECT * FROM Acuenta", null);
                                int i=0;
                                final ProgressDialog localProgressDialog = new ProgressDialog(pendiente.this);
                                AsyncHttpClient localAsyncHttpClient = new AsyncHttpClient();
                                String str = Global.ddns + "guardaradelanto.php";
                                RequestParams localRequestParams = new RequestParams();
                                localRequestParams.add("vendedor", text.getText().toString());
                                int size = (int) DatabaseUtils.longForQuery(db, "SELECT count(*) FROM Acuenta", null);
                                localRequestParams.add("size", String.valueOf(size));
                                if ((localCursor != null) && (localCursor.getCount() != 0) && (localCursor.moveToFirst()))
                                    do{
                                        localRequestParams.add("serie" + i, localCursor.getString(localCursor.getColumnIndex("serie")));
                                        localRequestParams.add("cliente" + i, localCursor.getString(localCursor.getColumnIndex("cliente")));
                                        localRequestParams.add("acuenta" + i, localCursor.getString(localCursor.getColumnIndex("acuenta")));
                                        i++;
                                    }
                                    while (localCursor.moveToNext());
                                localAsyncHttpClient.post(str, localRequestParams, new AsyncHttpResponseHandler() {

                                    public void onStart() {
                                        localProgressDialog.setMessage("Enviando Cobro ...");
                                        localProgressDialog.show();
                                    }

                                    public void onSuccess(int paramAnonymousInt, Header[] paramAnonymousArrayOfHeader, byte[] paramAnonymousArrayOfByte) {
                                        if (paramAnonymousInt == 200) {
                                            try {
                                                localProgressDialog.dismiss();
                                                Toast localToast2 = Toast.makeText(getApplicationContext(), "Cobranza enviada Correctamente", Toast.LENGTH_LONG);
                                                localToast2.getView().setBackgroundResource(R.drawable.toast_correct);
                                                localToast2.show();
                                                db.execSQL("DELETE FROM Acuenta WHERE serie>'0'");
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
                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {
                        paramAnonymous2DialogInterface.cancel();
                    }
                }).show();
            }
        });
    }

    public Cursor sumFoodColumn(){
        Cursor c = db.rawQuery("SELECT Sum(acuenta) AS mytotal FROM Acuenta", null);
        if (c !=null) {
            c.moveToFirst();
        }
        return c;
    }

    public class MyCursorAdapter extends ArrayAdapter {
        ArrayList<TitularAcuenta> datos;
        public MyCursorAdapter(Activity context, ArrayList<TitularAcuenta> paramArrayList) {
            super(context, R.layout.adaptador_personal_lista_1, paramArrayList);
            datos=paramArrayList;
        }

        public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup) {
            final View localview = getLayoutInflater().inflate(R.layout.acuenta, null);
            ((TextView)localview.findViewById(R.id.serie)).setText(datos.get(paramInt).getSerie());
            ((TextView)localview.findViewById(R.id.cliente)).setText(datos.get(paramInt).getCliente());
            ((TextView)localview.findViewById(R.id.total)).setText(datos.get(paramInt).getTotal());
            ((TextView)localview.findViewById(R.id.adelanto)).setText(datos.get(paramInt).getAdelanto());
            localview.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    AlertDialog.Builder localBuilder = new AlertDialog.Builder(pendiente.this);
                    localBuilder.setTitle("Eliminar")
                            .setMessage(datos.get(paramInt).getCliente()+" adelanto: S/."+datos.get(paramInt).getAdelanto())
                            .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {
                                    db.execSQL("DELETE FROM Acuenta WHERE serie='" + datos.get(paramInt).getSerie() + "'");
                                    datos.remove(paramInt);
                                    notifyDataSetChanged();
                                    Cursor c = sumFoodColumn();
                                    Double total = c.getDouble(c.getColumnIndex("mytotal"));
                                    count.setText("  S/. " + localDecimalFormat.format(total) + "  ");
                                }
                            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {
                            paramAnonymous2DialogInterface.cancel();
                        }
                    }).show();
                }
            });
            return localview;
        }
    }

    public void Buscar() {
        final ProgressDialog localProgressDialog = new ProgressDialog(this);
        adap2 = new AdaptadorCliente(this, adaptcliente, listadop, clie);
        AsyncHttpClient localAsyncHttpClient = new AsyncHttpClient();
        String str = Global.ddns + "cliente.php";
        RequestParams localRequestParams = new RequestParams();
        localRequestParams.add("cliente", clie.getText().toString());
        localAsyncHttpClient.post(str, localRequestParams, new AsyncHttpResponseHandler() {

            public void onStart(){
                localProgressDialog.setMessage("Buscando Cliente...");
                localProgressDialog.show();
            }

            public void onSuccess(int paramAnonymousInt, Header[] paramAnonymousArrayOfHeader, byte[] paramAnonymousArrayOfByte) {
                if (paramAnonymousInt == 200) {
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
                            listadop.setAdapter(adap2);
                            listadop.setVisibility(View.VISIBLE);
                            return;
                        }
                        Toast localToast2 = Toast.makeText(getApplicationContext(), "No se encontraron resultados", Toast.LENGTH_SHORT);
                        localToast2.getView().setBackgroundResource(R.drawable.toast_drawable);
                        localToast2.show();
                    } catch (Exception localException) {
                        localProgressDialog.dismiss();
                        Toast localToast1 = Toast.makeText(getApplicationContext(), "Error JSON", Toast.LENGTH_SHORT);
                        localToast1.getView().setBackgroundResource(R.drawable.toast_drawable);
                        localToast1.show();
                    }
                }
            }

            public void onFailure(int paramAnonymousInt, Header[] paramAnonymousArrayOfHeader, byte[] paramAnonymousArrayOfByte, Throwable paramAnonymousThrowable){
                localProgressDialog.dismiss();
                Toast localToast = Toast.makeText(getApplicationContext(), "No se encuentra la Base de Datos", Toast.LENGTH_SHORT);
                localToast.getView().setBackgroundResource(R.drawable.toast_drawable);
                localToast.show();
            }
        });
    }

    public void Iniciar() {
        final ProgressDialog localProgressDialog = new ProgressDialog(this);
        listadop.setVisibility(View.GONE);
        adap = new Adaptador(this, adaptador, listprod, user, count, listacuenta,localMyCursorAdapter);
        AsyncHttpClient localAsyncHttpClient = new AsyncHttpClient();
        String str = Global.ddns + "pendiente.php";
        RequestParams localRequestParams = new RequestParams();
        localRequestParams.add("cliente", clie.getText().toString());
        localRequestParams.add("vendedor", text.getText().toString());
        localAsyncHttpClient.post(str, localRequestParams, new AsyncHttpResponseHandler() {

            public void onStart() {
                localProgressDialog.setMessage("Buscando Deudas Pendientes...");
                localProgressDialog.show();
            }

            public void onSuccess(int paramAnonymousInt, Header[] paramAnonymousArrayOfHeader, byte[] paramAnonymousArrayOfByte) {
                if (paramAnonymousInt == 200) {
                    try {
                        localProgressDialog.dismiss();
                        adaptador.clear();
                        JSONArray localJSONArray = new JSONArray(new String(paramAnonymousArrayOfByte));
                        for (int i = 0; i < localJSONArray.length(); i++) {
                            String str1 = localJSONArray.getJSONObject(i).getString("cliente");
                            String str2 = localJSONArray.getJSONObject(i).getString("fecha");
                            String str3 = localJSONArray.getJSONObject(i).getString("fechapago");
                            String str4 = localJSONArray.getJSONObject(i).getString("total");
                            String str5 = localJSONArray.getJSONObject(i).getString("pendiente");
                            String str6 = localJSONArray.getJSONObject(i).getString("acuenta");
                            String str7 = localJSONArray.getJSONObject(i).getString("serieventas");
                            adaptador.add(new TitularPendiente(str1, str2, str3, str4, str5, str6, str7));
                        }
                        if (localJSONArray.length() > 0) {
                            listprod.setAdapter(adap);
                            return;
                        }
                        Toast localToast2 = Toast.makeText(getApplicationContext(), "No se encontraron resultados", Toast.LENGTH_SHORT);
                        localToast2.getView().setBackgroundResource(R.drawable.toast_drawable);
                        localToast2.show();
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
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menupendiente, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.buscar1){
            Buscar();
        }else{
            Iniciar();
        }
        return super.onOptionsItemSelected(item);
    }
}
