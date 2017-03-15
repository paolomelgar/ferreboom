package com.example.usuario.paolo;

public class TitularAdelanto {
    private String total;
    private String fecha;
    private String encargado;

    public TitularAdelanto(String paramString1, String paramString2, String paramString3)
    {
        this.fecha = paramString1;
        this.total = paramString2;
        this.encargado = paramString3;
    }

    public String getTotal()
    {
        return this.total;
    }

    public String getFecha()
    {
        return this.fecha;
    }

    public String getEncargado() { return this.encargado; }
}
