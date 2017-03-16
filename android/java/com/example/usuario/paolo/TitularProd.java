package com.example.usuario.paolo;

public class TitularProd
{
    private String id;
    private String importe;
    private String stock;
    private String titulo;
    private String unitario;
    private String sto;
    private String unit;

    public TitularProd(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7)
    {
        this.titulo = paramString1;
        this.stock = paramString2;
        this.unitario = paramString3;
        this.importe = paramString4;
        this.id = paramString5;
        this.sto = paramString6;
        this.unit = paramString7;
    }

    public String getId()
    {
        return this.id;
    }

    public String getImporte()
    {
        return this.importe;
    }

    public String getStock()
    {
        return this.stock;
    }

    public String getTitulo()
    {
        return this.titulo;
    }

    public String getUnitario()
    {
        return this.unitario;
    }

    public String getSto()
    {
        return this.sto;
    }

    public String getUnit()
    {
        return this.unit;
    }
}