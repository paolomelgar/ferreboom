package com.example.usuario.paolo;

/**
 * Created by USUARIO on 01/12/2015.
 */
public class TitularAcuenta {
    private String serie;
    private String cliente;
    private String total;
    private String adelanto;

    public TitularAcuenta(String paramString1, String paramString2, String paramString3,String paramString4)
    {
        this.serie = paramString1;
        this.cliente = paramString2;
        this.total = paramString3;
        this.adelanto = paramString4;
    }

    public String getSerie()
    {
        return this.serie;
    }

    public String getCliente()
    {
        return this.cliente;
    }

    public String getTotal() { return this.total; }

    public String getAdelanto() { return this.adelanto; }
}
