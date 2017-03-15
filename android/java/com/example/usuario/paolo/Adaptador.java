package com.example.usuario.paolo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class Adaptador extends ArrayAdapter {
    Activity context;
    ArrayList<TitularPendiente> datos;
    ListView list,listacuenta;
    String vendedor;
    TextView count;
    SQLiteDatabase db;
    ArrayList<TitularAdelanto> adaptadelanto = new ArrayList();
    ArrayList<Titularlistprod> adaptlist = new ArrayList();
    AdaptadorAdelanto adap;
    Adaptadorlistprod adap1;
    pendiente.MyCursorAdapter local;

    public Adaptador(Activity paramActivity, ArrayList<TitularPendiente> paramArrayList, ListView paramListView, String paramString, TextView count, ListView listacuenta,pendiente.MyCursorAdapter local) {
        super(paramActivity, R.layout.adaptadorpendiente, paramArrayList);
        this.datos = paramArrayList;
        this.context = paramActivity;
        this.list = paramListView;
        this.vendedor = paramString;
        this.count = count;
        this.listacuenta=listacuenta;
        this.local=local;
        adap = new AdaptadorAdelanto(context, adaptadelanto);
        adap1 = new Adaptadorlistprod(context, adaptlist);
        BdHelper localBdHelper = new BdHelper(context, "DBTotalVent", null, 1);
        db = localBdHelper.getWritableDatabase();
    }
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
        View localView = context.getLayoutInflater().inflate(R.layout.adaptadorpendiente, null);
        final TextView localTextView1 = (TextView)localView.findViewById(R.id.a);
        localTextView1.setText(datos.get(paramInt).getCliente());
        ((TextView)localView.findViewById(R.id.b)).setText(datos.get(paramInt).getFechaingreso());
        ((TextView)localView.findViewById(R.id.c)).setText(datos.get(paramInt).getSerieventas());
        final TextView total = (TextView)localView.findViewById(R.id.d);
        total.setText(datos.get(paramInt).getTotal());
        final TextView localTextView2 = (TextView)localView.findViewById(R.id.e);
        localTextView2.setText(datos.get(paramInt).getPendiente());
        TextView localTextView3 = (TextView)localView.findViewById(R.id.f);
        localTextView3.setText(datos.get(paramInt).getAcuenta());
        final TextView localTextView4 = (TextView)localView.findViewById(R.id.g);
        localTextView4.setText(datos.get(paramInt).getSerieventas());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        Date now= new Date();
        try {
            d = dateFormat.parse(datos.get(paramInt).getFechaingreso());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(d.getTime() < now.getTime()-2592000000L && d.getTime() >= now.getTime()-5184000000L){
            localView.setBackgroundColor(Color.parseColor("#FFFFFFB0"));
        }else if(d.getTime() < now.getTime()-5184000000L){
            localView.setBackgroundColor(Color.parseColor("#FFFDD8D3"));
        }else{
            localView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }
        localTextView3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                final ProgressDialog localProgressDialog = new ProgressDialog(context);
                AsyncHttpClient localAsyncHttpClient = new AsyncHttpClient();
                String str = Global.ddns + "adelanto.php";
                RequestParams localRequestParams = new RequestParams();
                localRequestParams.add("serie", localTextView4.getText().toString());
                localAsyncHttpClient.post(str, localRequestParams, new AsyncHttpResponseHandler() {

                    public void onStart() {
                        localProgressDialog.setMessage("Buscando Adelantos...");
                        localProgressDialog.show();
                    }

                    public void onSuccess(int paramAnonymous2Int, Header[] paramAnonymous2ArrayOfHeader, byte[] paramAnonymous2ArrayOfByte) {
                        if (paramAnonymous2Int == 200) {
                            try {
                                localProgressDialog.dismiss();
                                adap.clear();
                                JSONArray localJSONArray = new JSONArray(new String(paramAnonymous2ArrayOfByte));
                                for (int i = 0; i < localJSONArray.length(); i++) {
                                    String str1 = localJSONArray.getJSONObject(i).getString("fecha");
                                    String str2 = localJSONArray.getJSONObject(i).getString("adelanto");
                                    String str3 = localJSONArray.getJSONObject(i).getString("encargado");
                                    adap.add(new TitularAdelanto(str1,str2,str3));
                                }
                                Dialog localDialog = new Dialog(context);
                                localDialog.setContentView(R.layout.adelantos);
                                localDialog.setTitle(localTextView1.getText().toString());
                                ((ListView) localDialog.findViewById(R.id.listadelantos)).setAdapter(adap);
                                localDialog.show();
                            } catch (JSONException localJSONException) {
                                localProgressDialog.dismiss();
                                Toast localToast = Toast.makeText(context, "Error JSON", Toast.LENGTH_SHORT);
                                localToast.getView().setBackgroundResource(R.drawable.toast_drawable);
                                localToast.show();
                            }
                        }
                    }

                    public void onFailure(int paramAnonymous2Int, Header[] paramAnonymous2ArrayOfHeader, byte[] paramAnonymous2ArrayOfByte, Throwable paramAnonymous2Throwable) {
                        localProgressDialog.dismiss();
                        Toast localToast = Toast.makeText(context, "No se encuentra la Base de Datos", Toast.LENGTH_SHORT);
                        localToast.getView().setBackgroundResource(R.drawable.toast_drawable);
                        localToast.show();
                    }
                });
            }
        });

        localTextView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                final EditText localEditText = new EditText(context);
                new AlertDialog.Builder(context)
                    .setTitle(localTextView1.getText().toString()+"\nTOTAL:  S/. "+total.getText().toString())
                    .setMessage("Pendiente:  S/. "+localTextView2.getText().toString())
                    .setView(localEditText)
                    .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {
                            db.execSQL("INSERT INTO Acuenta(_id,cliente,serie,total,acuenta) VALUES (null,'" + localTextView1.getText().toString() + "','" + localTextView4.getText().toString() + "','" + localTextView2.getText().toString() + "','" + localEditText.getText().toString() + "')");
                            local.add(new TitularAcuenta(localTextView4.getText().toString(), localTextView1.getText().toString(), localTextView2.getText().toString(), localEditText.getText().toString()));
                            listacuenta.setAdapter(local);
                            Cursor c = db.rawQuery("SELECT Sum(acuenta) AS mytotal FROM Acuenta", null);
                            if (c !=null) {
                                c.moveToFirst();
                            }
                            DecimalFormat localDecimalFormat = new DecimalFormat("#0.00");
                            Double total = c.getDouble(c.getColumnIndex("mytotal"));
                            count.setText("  S/. " + localDecimalFormat.format(total) + "  ");
                            paramAnonymous2DialogInterface.cancel();
                        }
                    }).setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {
                            paramAnonymous2DialogInterface.cancel();
                        }
                    }).show();
            }
        });

        total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog localProgressDialog = new ProgressDialog(context);
                AsyncHttpClient localAsyncHttpClient = new AsyncHttpClient();
                String str = Global.ddns + "listaproductos.php";
                RequestParams localRequestParams = new RequestParams();
                localRequestParams.add("serie", localTextView4.getText().toString());
                localAsyncHttpClient.post(str, localRequestParams, new AsyncHttpResponseHandler() {

                    public void onStart() {
                        localProgressDialog.setMessage("Buscando Lista de Productos...");
                        localProgressDialog.show();
                    }

                    public void onSuccess(int paramAnonymous2Int, Header[] paramAnonymous2ArrayOfHeader, byte[] paramAnonymous2ArrayOfByte) {
                        if (paramAnonymous2Int == 200) {
                            try {
                                localProgressDialog.dismiss();
                                adap1.clear();
                                JSONArray localJSONArray = new JSONArray(new String(paramAnonymous2ArrayOfByte));
                                for (int i = 0; i < localJSONArray.length(); i++) {
                                    String str1 = localJSONArray.getJSONObject(i).getString("producto");
                                    String str2 = localJSONArray.getJSONObject(i).getString("cantidad");
                                    String str3 = localJSONArray.getJSONObject(i).getString("unitario");
                                    String str4 = localJSONArray.getJSONObject(i).getString("importe");
                                    adap1.add(new Titularlistprod(str1,str2,str3,str4));
                                }
                                Dialog localDialog = new Dialog(context);
                                localDialog.setContentView(R.layout.adelantos);
                                localDialog.setTitle(localTextView1.getText().toString());
                                ((ListView) localDialog.findViewById(R.id.listadelantos)).setAdapter(adap1);
                                localDialog.show();
                            } catch (JSONException localJSONException) {
                                localProgressDialog.dismiss();
                                Toast localToast = Toast.makeText(context, "Error JSON", Toast.LENGTH_SHORT);
                                localToast.getView().setBackgroundResource(R.drawable.toast_drawable);
                                localToast.show();
                            }
                        }
                    }

                    public void onFailure(int paramAnonymous2Int, Header[] paramAnonymous2ArrayOfHeader, byte[] paramAnonymous2ArrayOfByte, Throwable paramAnonymous2Throwable) {
                        localProgressDialog.dismiss();
                        Toast localToast = Toast.makeText(context, "No se encuentra la Base de Datos", Toast.LENGTH_SHORT);
                        localToast.getView().setBackgroundResource(R.drawable.toast_drawable);
                        localToast.show();
                    }
                });
            }
        });
        return localView;
    }
}