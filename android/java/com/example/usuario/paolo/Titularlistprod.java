package com.example.usuario.paolo;

public class Titularlistprod {
    private String cant;
    private String producto;
    private String unit;
    private String imp;

    public Titularlistprod(String paramString1, String paramString2, String paramString3, String  paramString4)
    {
        this.producto = paramString1;
        this.cant = paramString2;
        this.unit = paramString3;
        this.imp = paramString4;
    }

    public String getProducto() { return this.producto; }

    public String getCant()
    {
        return this.cant;
    }

    public String getUnit() { return this.unit; }

    public String getImp() { return this.imp; }
}
