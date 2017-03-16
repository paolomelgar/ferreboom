package com.example.usuario.paolo;

public class TitularCliente
{
    private String cliente;
    private String direccion;

    public TitularCliente(String paramString1, String paramString2)
    {
        this.cliente = paramString1;
        this.direccion = paramString2;
    }

    public String getCliente()
    {
        return this.cliente;
    }

    public String getDireccion()
    {
        return this.direccion;
    }
}