package com.example.usuario.paolo;

public class TitularAnterior
{
    private String cantidad;
    private String fecha;
    private String precio;
    private String producto;

    public TitularAnterior(String paramString1, String paramString2, String paramString3, String paramString4)
    {
        this.cantidad = paramString2;
        this.producto = paramString3;
        this.precio = paramString4;
        this.fecha = paramString1;
    }

    public String getCantidad()
    {
        return this.cantidad;
    }

    public String getFecha()
    {
        return this.fecha;
    }

    public String getPrecio()
    {
        return this.precio;
    }

    public String getProducto()
    {
        return this.producto;
    }
}