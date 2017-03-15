package com.example.usuario.paolo;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MapsActivity extends ActionBarActivity{
    EditText clien;
    private GoogleMap mMap;
    ListView listadop;
    AdaptadorCliente adap2;
    ArrayList<TitularCliente> adaptcliente = new ArrayList<TitularCliente>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        listadop = ((ListView)findViewById(R.id.view));
        setUpMapIfNeeded();
        android.support.v7.app.ActionBar localActionBar = getSupportActionBar();
        localActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2d4b7c")));
        localActionBar.setCustomView(R.layout.actionbar);
        localActionBar.setDisplayShowCustomEnabled(true);
        localActionBar.setDisplayShowHomeEnabled(true);
        clien = ((EditText)findViewById(R.id.search));
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(-12.0696667,-75.2066667) , 13.0f) );
    }

    public void Ver() {
        AsyncHttpClient localAsyncHttpClient = new AsyncHttpClient();
        String str = Global.ddns + "maps.php";
        RequestParams localRequestParams = new RequestParams();
        localRequestParams.add("cliente", " ");
        localAsyncHttpClient.post(str, localRequestParams, new AsyncHttpResponseHandler() {
            public void onFailure(int paramAnonymousInt, Header[] paramAnonymousArrayOfHeader, byte[] paramAnonymousArrayOfByte, Throwable paramAnonymousThrowable) {
                Toast localToast = Toast.makeText(getApplicationContext(), "No se encuentra la Base de Datos", Toast.LENGTH_SHORT);
                localToast.getView().setBackgroundResource(R.drawable.toast_drawable);
                localToast.show();
            }

            public void onSuccess(int paramAnonymousInt, Header[] paramAnonymousArrayOfHeader, byte[] paramAnonymousArrayOfByte) {
                if (paramAnonymousInt == 200) {
                    try {
                        JSONArray localJSONArray = new JSONArray(new String(paramAnonymousArrayOfByte));
                        for (int i = 0; i < localJSONArray.length(); i++) {
                            String str1 = localJSONArray.getJSONObject(i).getString("cliente");
                            String str2 = localJSONArray.getJSONObject(i).getString("latitud");
                            String str3 = localJSONArray.getJSONObject(i).getString("longitud");
                            double d1 = Double.parseDouble(str2);
                            double d2 = Double.parseDouble(str3);
                            GoogleMap localGoogleMap = mMap;
                            MarkerOptions localMarkerOptions = new MarkerOptions();
                            LatLng localLatLng = new LatLng(d1, d2);
                            Marker localMarker = localGoogleMap.addMarker(localMarkerOptions.position(localLatLng).title(str1));
                            if (str1.equalsIgnoreCase(clien.getText().toString())) {
                                localMarker.showInfoWindow();
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(d1, d2), 14.0f));
                            }
                        }
                    } catch (JSONException localJSONException) {
                        Toast localToast = Toast.makeText(getApplicationContext(), "Error JSON", Toast.LENGTH_SHORT);
                        localToast.getView().setBackgroundResource(R.drawable.toast_drawable);
                        localToast.show();
                    }
                }
            }
        });
    }
    private void setUpMap() {
        Ver();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            if (mMap != null)
                setUpMap();
        }
    }

    public void Buscar() {
        final ProgressDialog localProgressDialog = new ProgressDialog(this);
        adap2 = new AdaptadorCliente(this, adaptcliente, listadop, clien);
        AsyncHttpClient localAsyncHttpClient = new AsyncHttpClient();
        String str = Global.ddns + "cliente.php";
        RequestParams localRequestParams = new RequestParams();
        localRequestParams.add("cliente", clien.getText().toString());
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
                        return;
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menupendiente, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.buscar1){
            Buscar();
        }
        else{
            Ver();
        }
        return super.onOptionsItemSelected(item);
    }
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }
}
