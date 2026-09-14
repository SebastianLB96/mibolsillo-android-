package com.poligran.mibolsillo.model;

/** Modelo de un movimiento de gasto. Por ahora solo alimenta la interfaz. */
public class Gasto {

    private final int colorMiniatura;
    private final String descripcion;
    private final String fecha;
    private final String categoria;
    private final String valor;
    private final String medioPago;
    private final String comprobante;

    public Gasto(int colorMiniatura, String descripcion, String fecha, String categoria,
                 String valor, String medioPago, String comprobante) {
        this.colorMiniatura = colorMiniatura;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.categoria = categoria;
        this.valor = valor;
        this.medioPago = medioPago;
        this.comprobante = comprobante;
    }

    public int getColorMiniatura() { return colorMiniatura; }
    public String getDescripcion() { return descripcion; }
    public String getFecha()       { return fecha; }
    public String getCategoria()   { return categoria; }
    public String getValor()       { return valor; }
    public String getMedioPago()   { return medioPago; }
    public String getComprobante() { return comprobante; }

    /** Linea secundaria de la fila: fecha y categoria. */
    public String getResumen() {
        return fecha + " \u00b7 " + categoria;
    }
}
