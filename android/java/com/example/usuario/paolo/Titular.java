package com.example.usuario.paolo;

public class Titular
{
    private String descripcion;
    private String especial;
    private String id;
    private int img;
    private String precio;
    private String titulo;

    public Titular(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, int paramInt)
    {
        this.titulo = paramString1;
        this.descripcion = paramString2;
        this.precio = paramString3;
        this.especial = paramString4;
        this.id = paramString5;
        this.img = paramInt;
    }

    public String getDescripcion()
    {
        return this.descripcion;
    }

    public String getEspecial()
    {
        return this.especial;
    }

    public String getId()
    {
        return this.id;
    }

    public int getImg()
    {
        return this.img;
    }

    public String getPrecio()
    {
        return this.precio;
    }

    public String getTitulo()
    {
        return this.titulo;
    }
}