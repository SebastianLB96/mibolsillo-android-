package com.poligran.mibolsillo.data;

import com.poligran.mibolsillo.R;
import com.poligran.mibolsillo.model.Gasto;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Fuente unica de datos de la aplicacion (RNF8).
 *
 * Concentra la lista de gastos, el presupuesto del mes y todos los calculos
 * financieros. Los fragmentos no guardan datos propios: leen y escriben aqui,
 * de modo que un gasto registrado desde BotonesFragment se refleja de inmediato
 * en PerfilFragment y en FotosFragment.
 *
 * Los datos viven en memoria durante la sesion (RNF6). El almacenamiento
 * permanente queda propuesto como trabajo futuro.
 */
public class RepositorioGastos {

    /** Categorias validas del sistema. Todo gasto pertenece a una y solo una. */
    public static final String[] CATEGORIAS =
            {"Alimentacion", "Transporte", "Hogar", "Ocio", "Salud"};

    private static RepositorioGastos instancia;

    private final List<Gasto> gastos = new ArrayList<>();
    private long presupuesto = 2000000L;

    private RepositorioGastos() {
        cargarDatosIniciales();
    }

    /** Devuelve la unica instancia del repositorio (patron Singleton). */
    public static synchronized RepositorioGastos getInstancia() {
        if (instancia == null) {
            instancia = new RepositorioGastos();
        }
        return instancia;
    }

    // ------------------------------------------------------------------
    //  Consultas
    // ------------------------------------------------------------------

    /** Lista de gastos ordenada del mas reciente al mas antiguo (RF3). */
    public List<Gasto> getGastos() {
        return gastos;
    }

    public int getCantidadGastos() {
        return gastos.size();
    }

    public long getPresupuesto() {
        return presupuesto;
    }

    /** Suma de los valores de todos los gastos registrados. */
    public long getTotalGastado() {
        long total = 0;
        for (Gasto g : gastos) {
            total += g.getValor();
        }
        return total;
    }

    /** Presupuesto menos total gastado. Puede ser negativo. */
    public long getSaldoDisponible() {
        return presupuesto - getTotalGastado();
    }

    /** Porcentaje del presupuesto ya consumido, redondeado al entero mas cercano. */
    public int getPorcentajeConsumido() {
        if (presupuesto <= 0) {
            return 0;
        }
        return (int) Math.round(getTotalGastado() * 100.0 / presupuesto);
    }

    /** Indica si el gasto acumulado supero el presupuesto definido (RF8). */
    public boolean hayExcesoDePresupuesto() {
        return getTotalGastado() > presupuesto;
    }

    /** Monto en que se supero el presupuesto. Cero si no hay exceso. */
    public long getMontoExcedido() {
        long exceso = getTotalGastado() - presupuesto;
        return exceso > 0 ? exceso : 0;
    }

    /**
     * Participacion porcentual de cada categoria sobre el total gastado (RF9).
     * El arreglo devuelto sigue el mismo orden de CATEGORIAS.
     */
    public int[] getPorcentajePorCategoria() {
        int[] porcentajes = new int[CATEGORIAS.length];
        long total = getTotalGastado();
        if (total == 0) {
            return porcentajes;
        }
        for (int i = 0; i < CATEGORIAS.length; i++) {
            long suma = 0;
            for (Gasto g : gastos) {
                if (CATEGORIAS[i].equals(g.getCategoria())) {
                    suma += g.getValor();
                }
            }
            porcentajes[i] = (int) Math.round(suma * 100.0 / total);
        }
        return porcentajes;
    }

    // ------------------------------------------------------------------
    //  Operaciones
    // ------------------------------------------------------------------

    /**
     * Registra un nuevo gasto al inicio de la lista (RF5).
     *
     * @return true si el gasto se registro; false si el valor era invalido.
     */
    public boolean registrarGasto(Gasto gasto) {
        if (gasto == null || gasto.getValor() <= 0) {
            return false;
        }
        gastos.add(0, gasto);
        return true;
    }

    /**
     * Actualiza el tope de gasto del mes (RF7).
     *
     * @return true si el presupuesto se actualizo; false si el valor era invalido.
     */
    public boolean definirPresupuesto(long nuevoPresupuesto) {
        if (nuevoPresupuesto <= 0) {
            return false;
        }
        this.presupuesto = nuevoPresupuesto;
        return true;
    }

    // ------------------------------------------------------------------
    //  Utilidades
    // ------------------------------------------------------------------

    /** Da formato de pesos colombianos sin decimales (RNF7). Ej.: $ 1.445.700 */
    public static String formatoMoneda(long valor) {
        NumberFormat formato = NumberFormat.getInstance(new Locale("es", "CO"));
        formato.setMaximumFractionDigits(0);
        return "$ " + formato.format(valor);
    }

    /** Datos de ejemplo con los que arranca la aplicacion. */
    private void cargarDatosIniciales() {
        gastos.add(new Gasto(R.drawable.comp_mercado, "Mercado", "12/03/2026", "Alimentacion",
                186400, "Tarjeta debito", "Factura POS N.\u00ba 004512"));
        gastos.add(new Gasto(R.drawable.comp_energia, "Energia", "05/03/2026", "Hogar",
                142900, "Debito automatico", "Recibo N.\u00ba 88231"));
        gastos.add(new Gasto(R.drawable.comp_transporte, "Transporte", "03/03/2026", "Transporte",
                12500, "Efectivo", "Tiquete N.\u00ba 1042"));
        gastos.add(new Gasto(R.drawable.comp_arriendo, "Arriendo", "01/03/2026", "Hogar",
                950000, "Transferencia", "Comprobante N.\u00ba 7781"));
        gastos.add(new Gasto(R.drawable.comp_internet, "Internet", "28/02/2026", "Ocio",
                89900, "Tarjeta credito", "Factura N.\u00ba 55110"));
        gastos.add(new Gasto(R.drawable.comp_salud, "Medico", "20/02/2026", "Salud",
                64000, "Efectivo", "Recibo N.\u00ba 2098"));
    }
}
