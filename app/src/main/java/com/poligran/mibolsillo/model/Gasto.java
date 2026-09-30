package com.poligran.mibolsillo.model;

import com.poligran.mibolsillo.data.RepositorioGastos;

/**
 * Modelo de un movimiento de gasto.
 *
 * El valor se guarda como numero (long) y no como texto, porque sobre el
 * se calculan el total gastado, el saldo disponible y los porcentajes.
 */
public class Gasto {

    private final int numeroMiniatura;
    private final String descripcion;
    private final String fecha;
    private final String categoria;
    private final long valor;
    private final String medioPago;
    private final String comprobante;

    public Gasto(int numeroMiniatura, String descripcion, String fecha, String categoria,
                 long valor, String medioPago, String comprobante) {
        this.numeroMiniatura = numeroMiniatura;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.categoria = categoria;
        this.valor = valor;
        this.medioPago = medioPago;
        this.comprobante = comprobante;
    }

    public int getNumeroMiniatura() { return numeroMiniatura; }
    public String getDescripcion()  { return descripcion; }
    public String getFecha()        { return fecha; }
    public String getCategoria()    { return categoria; }
    public long getValor()          { return valor; }
    public String getMedioPago()    { return medioPago; }
    public String getComprobante()  { return comprobante; }

    /** Valor con formato de pesos colombianos. */
    public String getValorFormateado() {
        return RepositorioGastos.formatoMoneda(valor);
    }

    /** Linea secundaria de la fila: fecha y categoria. */
    public String getResumen() {
        return fecha + " · " + categoria;
    }
}
