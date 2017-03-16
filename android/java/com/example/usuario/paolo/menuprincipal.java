package com.example.usuario.paolo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.emitter.Emitter;
import java.net.URISyntaxException;

import cz.msebera.android.httpclient.Header;

public class menuprincipal extends ActionBarActivity {
    SQLiteDatabase db;
    ListView lista,listv;
    DrawerLayout drawerlayout;
    TextView usuario;
    SharedPreferences sharedPref;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.0.117:3500");
        } catch (URISyntaxException e) {}
    }

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.menuprincipal);
        usuario = ((TextView)findViewById(R.id.userr));
        lista = ((ListView)findViewById(R.id.listpend));
        drawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listv = (ListView) findViewById(R.id.left_drawer);
        String[] opciones = { "Opción 1", "Opción 2", "Opción 3", "Opción 4" };

        listv.setAdapter(new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, android.R.id.text1,
                opciones));
        Bundle localBundle = getIntent().getExtras();
        ActionBar localActionBar = getSupportActionBar();
        String str = localBundle.getString("user");
        localActionBar.setTitle(str);
        localActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2d4b7c")));
        usuario.setText(str);
        db = new BdHelper(this, "DBTotalVent", null,1).getWritableDatabase();
        MyCursorAdapter localMyCursorAdapter = new MyCursorAdapter(menuprincipal.this, R.layout.adaptador_custom, cursor(), new String[] { "idventa", "cliente", "total", "fecha" }, new int[] { R.id.idventa, R.id.cliente, R.id.total, R.id.fecha });
        lista.setAdapter(localMyCursorAdapter);
        mSocket.connect();
        mSocket.emit("usuario", str);
        mSocket.on("inactivo", onNewMessage);
        sharedPref = getSharedPreferences("data", 0);
    }

    @Override
    public void onStart() {
        super.onStart();
        setContentView(R.layout.menuprincipal);
        usuario = ((TextView)findViewById(R.id.userr));
        lista = ((ListView)findViewById(R.id.listpend));
        Bundle localBundle = getIntent().getExtras();
        ActionBar localActionBar = getSupportActionBar();
        String str = localBundle.getString("user");
        localActionBar.setTitle(str);
        localActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2d4b7c")));
        usuario.setText(str);
        db = new BdHelper(this, "DBTotalVent", null, 1).getWritableDatabase();
        MyCursorAdapter localMyCursorAdapter = new MyCursorAdapter(menuprincipal.this, R.layout.adaptador_custom, cursor(), new String[] { "idventa", "cliente", "total", "fecha" }, new int[] { R.id.idventa, R.id.cliente, R.id.total, R.id.fecha });
        lista.setAdapter(localMyCursorAdapter);
        mSocket.on("inactivo", onNewMessage);
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences.Editor localEditor = sharedPref.edit();
                    localEditor.putInt("isLogged", 0);
                    localEditor.putString("user", "");
                    localEditor.commit();
                }
            });
        }
    };
    public void ver(View paramView) {
        Uri uri = Uri.parse(Global.ddns + "catalogo.php");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void Actualizar(){
        new AlertDialog.Builder(this).
        setTitle("Actualizar").
        setMessage("Desea Actualizar todos los productos?").
        setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                final ProgressDialog localProgressDialog = new ProgressDialog(menuprincipal.this);
                AsyncHttpClient localAsyncHttpClient = new AsyncHttpClient();
                String str = Global.ddns + "precios.php";
                RequestParams localRequestParams = new RequestParams();
                localRequestParams.add("cargo", "ADMIN");
                localRequestParams.add("vendedor", usuario.getText().toString());
                localAsyncHttpClient.post(str, localRequestParams, new AsyncHttpResponseHandler() {

                    public void onStart() {
                        localProgressDialog.setMessage("Actualizando Precios...");
                        localProgressDialog.show();
                    }

                    public void onSuccess(int paramAnonymous2Int, Header[] paramAnonymous2ArrayOfHeader, byte[] paramAnonymous2ArrayOfByte) {
                        if (paramAnonymous2Int == 200) {
                            try {
                                db.execSQL("DELETE FROM Precios WHERE idprod>'0'");
                                JSONArray localJSONArray = new JSONArray(new String(paramAnonymous2ArrayOfByte));
                                for (int i = 0; i < localJSONArray.length(); i++) {
                                    String str1 = localJSONArray.getJSONObject(i).getString("producto") + " " + localJSONArray.getJSONObject(i).getString("marca");
                                    String str2 = localJSONArray.getJSONObject(i).getString("stock_real");
                                    String str3 = localJSONArray.getJSONObject(i).getString("p_promotor");
                                    String str4 = localJSONArray.getJSONObject(i).getString("p_especial");
                                    String str5 = localJSONArray.getJSONObject(i).getString("id");
                                    db.execSQL("INSERT INTO Precios(_id,idprod,producto,stock,unitario,especial) VALUES (null,'" + str5 + "','" + str1 + "','" + str2 + "','" + str3 + "','" + str4 + "')");
                                }
                                localProgressDialog.dismiss();
                            } catch (Exception localException) {
                                localProgressDialog.dismiss();
                                Toast localToast = Toast.makeText(getApplicationContext(), "Error JSON", Toast.LENGTH_SHORT);
                                localToast.getView().setBackgroundResource(R.drawable.toast_drawable);
                                localToast.show();
                            }
                        }
                    }

                    public void onFailure(int paramAnonymous2Int, Header[] paramAnonymous2ArrayOfHeader, byte[] paramAnonymous2ArrayOfByte, Throwable paramAnonymous2Throwable) {
                        localProgressDialog.dismiss();
                        Toast localToast = Toast.makeText(getApplicationContext(), "No se encuentra la Base de Datos", Toast.LENGTH_SHORT);
                        localToast.getView().setBackgroundResource(R.drawable.toast_drawable);
                        localToast.show();
                    }
                });
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
            }
        }).show();
    }
    public Cursor cursor() {
        return db.rawQuery("SELECT * FROM TotalVenta WHERE entregado='NO'", null);
    }
    private class MyCursorAdapter extends SimpleCursorAdapter {

        public MyCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
            super(context, layout, c, from, to);
        }

        public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
            View localView = super.getView(paramInt, paramView, paramViewGroup);
            final TextView localTextView1 = (TextView)localView.findViewById(R.id.cliente);
            final TextView localTextView2 = (TextView)localView.findViewById(R.id.idventa);
            final TextView localTextView3 = (TextView)localView.findViewById(R.id.fecha);
            final TextView localTextView4 = (TextView)localView.findViewById(R.id.total);
            if (paramInt % 2 == 0) {
                localView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#DAFDFD")));
            }
            localView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView){
                    new AlertDialog.Builder(menuprincipal.this)
                            .setTitle(localTextView1.getText().toString())
                            .setMessage("Fecha: " + localTextView3.getText().toString() + "    S/. " + localTextView4.getText().toString())
                            .setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {
                                    Intent localIntent = new Intent(menuprincipal.this, principal.class);
                                    localIntent.putExtra("user", usuario.getText());
                                    localIntent.putExtra("id", localTextView2.getText());
                                    localIntent.putExtra("cliente", localTextView1.getText());
                                    localIntent.putExtra("total", localTextView4.getText());
                                    startActivity(localIntent);
                                }
                            }).setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {
                                    db.execSQL("UPDATE TotalVenta SET entregado='ANULADO' WHERE idventa='" + localTextView2.getText().toString() + "'");
                                    MyCursorAdapter localMyCursorAdapter = new MyCursorAdapter(menuprincipal.this, R.layout.adaptador_custom, cursor(), new String[] { "idventa", "cliente", "total", "fecha" }, new int[] { R.id.idventa, R.id.cliente, R.id.total,R.id.fecha });
                                    lista.setAdapter(localMyCursorAdapter);
                                }
                            }).show();
                }
            });
            return localView;
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuprincipal, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.pedido){
            Intent localIntent1 = new Intent(this, principal.class);
            localIntent1.putExtra("user", usuario.getText());
            localIntent1.putExtra("id", "0");
            startActivity(localIntent1);
        }else if (id == R.id.maps){
            Intent localIntent2 = new Intent(this, MapsActivity.class);
            localIntent2.putExtra("user", usuario.getText());
            startActivity(localIntent2);
        }else if (id == R.id.pendiente){
            Intent localIntent3 = new Intent(this, pendiente.class);
            localIntent3.putExtra("user", usuario.getText());
            startActivity(localIntent3);
        }else{
            Actualizar();
        }
        return super.onOptionsItemSelected(item);
    }
}
