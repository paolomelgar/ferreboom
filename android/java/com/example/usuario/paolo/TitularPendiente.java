package com.example.usuario.paolo;

public class TitularPendiente
{
    private String acuenta;
    private String cliente;
    private String fechaingreso;
    private String fechapago;
    private String pendiente;
    private String serieventas;
    private String total;

    public TitularPendiente(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7)
    {
        this.cliente = paramString1;
        this.fechaingreso = paramString2;
        this.fechapago = paramString3;
        this.total = paramString4;
        this.pendiente = paramString5;
        this.acuenta = paramString6;
        this.serieventas = paramString7;
    }

    public String getAcuenta()
    {
        return this.acuenta;
    }

    public String getCliente()
    {
        return this.cliente;
    }

    public String getFechaingreso()
    {
        return this.fechaingreso;
    }

    public String getFechapago()
    {
        return this.fechapago;
    }

    public String getPendiente()
    {
        return this.pendiente;
    }

    public String getSerieventas()
    {
        return this.serieventas;
    }

    public String getTotal()
    {
        return this.total;
    }
}