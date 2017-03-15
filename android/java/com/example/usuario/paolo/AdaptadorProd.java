package com.example.usuario.paolo;

        import android.app.ActionBar;
        import android.app.Activity;
        import android.app.AlertDialog;
        import android.app.AlertDialog.Builder;
        import android.content.DialogInterface;
        import android.content.DialogInterface.OnClickListener;
        import android.content.res.Resources;
        import android.graphics.Color;
        import android.text.Editable;
        import android.text.TextWatcher;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.View.OnLongClickListener;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.RelativeLayout;
        import android.widget.SlidingDrawer;
        import android.widget.TextView;
        import android.widget.Toast;
        import com.loopj.android.http.AsyncHttpClient;
        import com.loopj.android.http.AsyncHttpResponseHandler;
        import com.loopj.android.http.RequestParams;
        import java.text.DecimalFormat;
        import java.util.ArrayList;
        import org.json.JSONArray;
        import org.json.JSONObject;

        import cz.msebera.android.httpclient.Header;

public class AdaptadorProd extends ArrayAdapter
{
    AdaptadorAnterior adap;
    TextView cliente;
    Activity context;
    ArrayList<TitularProd> datos;
    ArrayList<TitularAnterior> datosant;
    ListView list;
    ListView listant;
    SlidingDrawer sl;
    TextView sum;

    public AdaptadorProd(Activity paramActivity, ArrayList<TitularProd> paramArrayList, ListView paramListView1, TextView paramTextView1, TextView paramTextView2, AdaptadorAnterior paramAdaptadorAnterior, ListView paramListView2, ArrayList<TitularAnterior> paramArrayList1, SlidingDrawer paramSlidingDrawer)
    {
        super(paramActivity, R.layout.adaptador_personal_lista_2, paramArrayList);
        datos = paramArrayList;
        context = paramActivity;
        list = paramListView1;
        sum = paramTextView1;
        cliente = paramTextView2;
        adap = paramAdaptadorAnterior;
        listant = paramListView2;
        datosant = paramArrayList1;
        sl = paramSlidingDrawer;
    }

    public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup)
    {
        ViewHolder localViewHolder;
        if (paramView == null) {
            paramView = context.getLayoutInflater().inflate(R.layout.adaptador_personal_lista_2, null);
            localViewHolder = new ViewHolder();
            localViewHolder.titulo = ((TextView)paramView.findViewById(R.id.titulo));
            localViewHolder.stock = ((EditText)paramView.findViewById(R.id.stock));
            localViewHolder.unitario = ((EditText)paramView.findViewById(R.id.unitario));
            localViewHolder.importe = ((EditText)paramView.findViewById(R.id.importe));
            localViewHolder.id = ((TextView)paramView.findViewById(R.id.idd));
            localViewHolder.sto = ((TextView)paramView.findViewById(R.id.sto));
            localViewHolder.unit = ((TextView)paramView.findViewById(R.id.unit));
            localViewHolder.mWatcher = new MutableWatcher(localViewHolder, null);
            localViewHolder.stock.addTextChangedListener(localViewHolder.mWatcher);
            localViewHolder.stock.setSelectAllOnFocus(true);
            localViewHolder.unitario.addTextChangedListener(localViewHolder.mWatcher);
            localViewHolder.unitario.setSelectAllOnFocus(true);
            paramView.setTag(localViewHolder);
            if (paramInt % 2 == 0)
                paramView.setBackgroundColor(Color.parseColor("#FFEDFAFF"));
            if (paramInt == 0) {
                localViewHolder.stock.requestFocus();
            }
        }else {
            localViewHolder = (ViewHolder)paramView.getTag();
        }
        localViewHolder.titulo.setText(datos.get(paramInt).getTitulo());
        localViewHolder.mWatcher.setActive(false);
        localViewHolder.stock.setText(datos.get(paramInt).getStock());
        localViewHolder.unitario.setText(datos.get(paramInt).getUnitario());
        localViewHolder.importe.setText(datos.get(paramInt).getImporte());
        localViewHolder.id.setText(datos.get(paramInt).getId());
        localViewHolder.sto.setText(datos.get(paramInt).getSto());
        localViewHolder.unit.setText(datos.get(paramInt).getUnit());
        localViewHolder.mWatcher.setPosition(paramInt);
        localViewHolder.mWatcher.setActive(true);

        localViewHolder.titulo.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View paramAnonymousView) {
                AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
                localBuilder.setTitle("Eliminar")
                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {
                            DecimalFormat localDecimalFormat = new DecimalFormat("##.00");
                            Toast localToast = Toast.makeText(context, "Borrado Correctamente", Toast.LENGTH_SHORT);
                            localToast.getView().setBackgroundResource(R.drawable.toast_correct);
                            localToast.show();
                            datos.remove(paramInt);
                            notifyDataSetChanged();
                            Double localDouble = 0.00;
                            for (int i = 0; i < datos.size(); i++)
                                localDouble = localDouble + Double.parseDouble(datos.get(i).getImporte().replace(",", "."));
                            sum.setText(localDecimalFormat.format(localDouble));
                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {
                            paramAnonymous2DialogInterface.cancel();
                        }
                    });
                AlertDialog localAlertDialog = localBuilder.create();
                View localView = context.getLayoutInflater().inflate(R.layout.custom, null);
                int m=context.getResources().getIdentifier("a" + datos.get(paramInt).getId(), "drawable", context.getPackageName());
                if (m==0) {
                    ((ImageView) localView.findViewById(R.id.ima)).setImageResource(R.drawable.nodisponible);
                }else{
                    ((ImageView) localView.findViewById(R.id.ima)).setImageResource(m);
                }
                ((TextView) localView.findViewById(R.id.precio)).setText("Esta seguro de Eliminar!!!");
                localAlertDialog.setView(localView);
                localAlertDialog.requestWindowFeature(1);
                localAlertDialog.show();
                return false;
            }
        });
        final ViewHolder finalLocalViewHolder1 = localViewHolder;
        localViewHolder.titulo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (!cliente.getText().toString().equals("")){
                    AsyncHttpClient localAsyncHttpClient = new AsyncHttpClient();
                    String str = Global.ddns + "anterior.php";
                    RequestParams localRequestParams = new RequestParams();
                    localRequestParams.add("cliente", cliente.getText().toString());
                    localRequestParams.add("producto", finalLocalViewHolder1.titulo.getText().toString());
                    localAsyncHttpClient.post(str, localRequestParams, new AsyncHttpResponseHandler() {

                        public void onStart() {
                            datosant.clear();
                            datosant.add(new TitularAnterior("", "...", "Buscando...", ""));
                            listant.setAdapter(adap);
                        }

                        public void onSuccess(int paramAnonymous2Int, Header[] paramAnonymous2ArrayOfHeader, byte[] paramAnonymous2ArrayOfByte) {
                            if (paramAnonymous2Int == 200)
                                try {
                                    datosant.clear();
                                    JSONArray localJSONArray = new JSONArray(new String(paramAnonymous2ArrayOfByte));
                                    for (int i = 0; i < localJSONArray.length(); i++) {
                                        String str1 = localJSONArray.getJSONObject(i).getString("fecha");
                                        String str2 = localJSONArray.getJSONObject(i).getString("cantidad");
                                        String str3 = localJSONArray.getJSONObject(i).getString("producto");
                                        String str4 = localJSONArray.getJSONObject(i).getString("unitario");
                                        datosant.add(new TitularAnterior(str1, str2, str3, str4));
                                    }
                                    if (localJSONArray.length() > 0) {
                                        listant.setAdapter(adap);
                                        return;
                                    }
                                    datosant.add(new TitularAnterior("              No", "hay", "   Resultados", ""));
                                    listant.setAdapter(adap);
                                }
                                catch (Exception localException) {
                                    Toast localToast = Toast.makeText(context, "Error JSON", Toast.LENGTH_SHORT);
                                    localToast.getView().setBackgroundResource(R.drawable.toast_drawable);
                                    localToast.show();
                                }
                        }

                        public void onFailure(int paramAnonymous2Int, Header[] paramAnonymous2ArrayOfHeader, byte[] paramAnonymous2ArrayOfByte, Throwable paramAnonymous2Throwable) {
                            Toast localToast = Toast.makeText(context, "No se encuentra la Base de Datos", Toast.LENGTH_SHORT);
                            localToast.getView().setBackgroundResource(R.drawable.toast_drawable);
                            localToast.show();
                        }
                    });
                    sl.animateOpen();
                }
            }
        });
    return paramView;
    }

    class MutableWatcher implements TextWatcher {
        private boolean mActive;
        private int mPosition;
        private ViewHolder view;

        private MutableWatcher(ViewHolder arg2, Object o)
        {
            view=arg2;
        }

        public void afterTextChanged(Editable paramEditable)
        {
            if ((mActive) && (!paramEditable.toString().trim().equals("")))
            {
                DecimalFormat localDecimalFormat = new DecimalFormat("##.00");
                Double localDouble1 = Double.parseDouble(view.stock.getText().toString());
                Double localDouble2 = Double.parseDouble(view.unitario.getText().toString());
                Double localDouble4 = 0.00;
                if (localDouble1 > Double.parseDouble(view.sto.getText().toString())) {
                    view.sto.setVisibility(View.VISIBLE);
                    view.stock.setTextColor(Color.parseColor("#FFFF0000"));
                }else{
                    view.sto.setVisibility(View.GONE);
                    view.stock.setTextColor(Color.parseColor("#FF000000"));
                }
                if (localDouble2 < Double.valueOf(view.unit.getText().toString())) {
                    view.unitario.setTextColor(Color.parseColor("#FFFF0000"));
                }else{
                    view.unitario.setTextColor(Color.parseColor("#FF000000"));
                }
                Double localDouble3 = localDouble1 * localDouble2;
                view.importe.setText(localDecimalFormat.format(localDouble3));
                datos.set(mPosition, new TitularProd(view.titulo.getText().toString(), view.stock.getText().toString(), view.unitario.getText().toString(), localDecimalFormat.format(localDouble3), view.id.getText().toString(), view.sto.getText().toString(), view.unit.getText().toString()));
                for (int i = 0; i < datos.size(); i++) {
                    localDouble4 = localDouble4 + Double.parseDouble((datos.get(i)).getImporte().replace(",", "."));
                }
                sum.setText(localDecimalFormat.format(localDouble4));
            }
        }

        public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
        }

        public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
        }

        void setActive(boolean paramBoolean)
        {
            this.mActive = paramBoolean;
        }

        void setPosition(int paramInt)
        {
            this.mPosition = paramInt;
        }
    }

    static class ViewHolder {
        public TextView id;
        public EditText importe;
        public MutableWatcher mWatcher;
        public EditText stock;
        public TextView titulo;
        public TextView unit;
        public TextView sto;
        public EditText unitario;
    }
}
