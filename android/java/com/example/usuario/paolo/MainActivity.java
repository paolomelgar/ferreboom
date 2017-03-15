package com.example.usuario.paolo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends ActionBarActivity {
    int number;
    EditText pass;
    SharedPreferences sharedPref;
    EditText user;
    Button but;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = ((EditText)findViewById(R.id.user));
        pass = ((EditText)findViewById(R.id.pass));
        but = (Button)findViewById(R.id.button);
        getSupportActionBar().hide();
        sharedPref = getSharedPreferences("data", 0);
        number = sharedPref.getInt("isLogged", 0);
        if (number == 1) {
            Intent localIntent = new Intent(this, menuprincipal.class);
            localIntent.putExtra("user", sharedPref.getString("user", ""));
            startActivity(localIntent);
        }
    }

    public void ClickIniciar(View paramView) {
        final ProgressDialog localProgressDialog = new ProgressDialog(this);
        AsyncHttpClient localAsyncHttpClient = new AsyncHttpClient();
        String str = Global.ddns + "login.php";
        RequestParams localRequestParams = new RequestParams();
        localRequestParams.add("user", user.getText().toString());
        localRequestParams.add("pass", pass.getText().toString());
        localAsyncHttpClient.post(str, localRequestParams, new AsyncHttpResponseHandler() {
            String usuario = null;

            @Override
            public void onStart() {
                localProgressDialog.setMessage("Autenticando...");
                localProgressDialog.show();
            }

            @Override
            public void onSuccess(int paramAnonymousInt, Header[] headers, byte[] paramAnonymousArrayOfByte) {
                if (paramAnonymousInt == 200) {
                    try {
                        localProgressDialog.dismiss();
                        usuario = new JSONObject(new String(paramAnonymousArrayOfByte)).getString("login");
                        if (!TextUtils.isEmpty(usuario)) {
                            SharedPreferences.Editor localEditor = sharedPref.edit();
                            localEditor.putInt("isLogged", 1);
                            localEditor.putString("user", usuario);
                            localEditor.commit();
                            Toast localToast3 = Toast.makeText(getApplicationContext(), "Bienvenido " +usuario, Toast.LENGTH_LONG);
                            localToast3.getView().setBackgroundResource(R.drawable.toast_correct);
                            localToast3.show();
                            Intent localIntent = new Intent(MainActivity.this, menuprincipal.class);
                            localIntent.putExtra("user", usuario);
                            startActivity(localIntent);
                        }else {
                            Toast localToast2 = Toast.makeText(getApplicationContext(), "Usuario Incorrecto", Toast.LENGTH_SHORT);
                            localToast2.getView().setBackgroundResource(R.drawable.toast_drawable);
                            localToast2.show();
                        }
                    } catch (Exception localJSONException) {
                        localProgressDialog.dismiss();
                        Toast localToast1 = Toast.makeText(getApplicationContext(), "Error JSON", Toast.LENGTH_SHORT);
                        localToast1.getView().setBackgroundResource(R.drawable.toast_drawable);
                        localToast1.show();
                    }
                }
            }

            @Override
            public void onFailure(int paramAnonymousInt, Header[] headers, byte[] paramAnonymousArrayOfByte, Throwable paramAnonymousThrowable) {
                localProgressDialog.dismiss();
                Toast localToast = Toast.makeText(getApplicationContext(), "No se encuentra la Base de Datos", Toast.LENGTH_SHORT);
                localToast.getView().setBackgroundResource(R.drawable.toast_drawable);
                localToast.show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.buscar) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
