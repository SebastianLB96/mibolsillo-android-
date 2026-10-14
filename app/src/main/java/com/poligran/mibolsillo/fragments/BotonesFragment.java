package com.poligran.mibolsillo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.poligran.mibolsillo.R;
import com.poligran.mibolsillo.data.RepositorioGastos;
import com.poligran.mibolsillo.model.Gasto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Panel de operaciones (RF5 a RF9 / CU-05 a CU-09).
 *
 * Aqui se concentran los cuatro eventos de la aplicacion:
 *   - Registrar nuevo gasto       -> RF5
 *   - Ver grafico mensual         -> RF9
 *   - Definir presupuesto         -> RF7
 *   - Escanear factura            -> RF6 (incluye al RF5)
 * La alerta de exceso (RF8) se dispara al terminar de registrar un gasto.
 */
public class BotonesFragment extends Fragment implements View.OnClickListener {

    // --- Variables vinculadas a las vistas del layout ---
    private EditText txtValorGasto;
    private Spinner spnCategoria;
    private EditText txtMedioPago;
    private EditText txtPresupuesto;
    private TextView txtResultadoAccion;
    private ProgressBar barraPresupuesto;
    private View barraAlimentacion, barraTransporte, barraHogar, barraOcio, barraSalud;
    private TextView pctAlimentacion, pctTransporte, pctHogar, pctOcio, pctSalud;

    private RepositorioGastos repositorio;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup contenedor,
                             @Nullable Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_botones, contenedor, false);
        repositorio = RepositorioGastos.getInstancia();

        // --- Vinculo de las variables con los identificadores del layout ---
        txtValorGasto      = vista.findViewById(R.id.txtValorGasto);
        spnCategoria       = vista.findViewById(R.id.spnCategoria);
        txtMedioPago       = vista.findViewById(R.id.txtMedioPago);
        txtPresupuesto     = vista.findViewById(R.id.txtPresupuesto);
        txtResultadoAccion = vista.findViewById(R.id.txtResultadoAccion);
        barraPresupuesto   = vista.findViewById(R.id.barraPresupuesto);

        barraAlimentacion = vista.findViewById(R.id.barraAlimentacion);
        barraTransporte   = vista.findViewById(R.id.barraTransporte);
        barraHogar        = vista.findViewById(R.id.barraHogar);
        barraOcio         = vista.findViewById(R.id.barraOcio);
        barraSalud        = vista.findViewById(R.id.barraSalud);

        pctAlimentacion = vista.findViewById(R.id.pctAlimentacion);
        pctTransporte   = vista.findViewById(R.id.pctTransporte);
        pctHogar        = vista.findViewById(R.id.pctHogar);
        pctOcio         = vista.findViewById(R.id.pctOcio);
        pctSalud        = vista.findViewById(R.id.pctSalud);

        Button btnRegistrarGasto     = vista.findViewById(R.id.btnRegistrarGasto);
        Button btnVerGrafico         = vista.findViewById(R.id.btnVerGrafico);
        Button btnDefinirPresupuesto = vista.findViewById(R.id.btnDefinirPresupuesto);
        Button btnEscanearFactura    = vista.findViewById(R.id.btnEscanearFactura);

        // --- Declaracion de los eventos ---
        btnRegistrarGasto.setOnClickListener(this);
        btnVerGrafico.setOnClickListener(this);
        btnDefinirPresupuesto.setOnClickListener(this);
        btnEscanearFactura.setOnClickListener(this);

        txtPresupuesto.setText(String.valueOf(repositorio.getPresupuesto()));
        actualizarBarraPresupuesto();
        actualizarGrafico();

        return vista;
    }

    /** Distribuye el evento segun el boton pulsado. */
    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnRegistrarGasto) {
            registrarGasto();
        } else if (id == R.id.btnVerGrafico) {
            verGraficoMensual();
        } else if (id == R.id.btnDefinirPresupuesto) {
            definirPresupuesto();
        } else if (id == R.id.btnEscanearFactura) {
            escanearFactura();
        }
    }

    // ------------------------------------------------------------------
    //  RF5 / CU-05: registrar un nuevo gasto
    // ------------------------------------------------------------------
    private void registrarGasto() {
        String textoValor = txtValorGasto.getText().toString().trim();

        // Flujo alterno: valor vacio, no numerico o menor o igual a cero
        if (textoValor.isEmpty()) {
            txtValorGasto.setError(getString(R.string.error_valor_vacio));
            return;
        }

        long valor;
        try {
            valor = Long.parseLong(textoValor);
        } catch (NumberFormatException e) {
            txtValorGasto.setError(getString(R.string.error_valor_invalido));
            return;
        }

        if (valor <= 0) {
            txtValorGasto.setError(getString(R.string.error_valor_invalido));
            return;
        }

        String categoria = spnCategoria.getSelectedItem().toString();
        String medioPago = txtMedioPago.getText().toString().trim();
        if (medioPago.isEmpty()) {
            medioPago = getString(R.string.medio_por_defecto);
        }

        String fecha = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "CO"))
                .format(new Date());

        Gasto gasto = new Gasto(imagenDeCategoria(categoria), categoria, fecha, categoria,
                valor, medioPago, getString(R.string.comprobante_manual));

        repositorio.registrarGasto(gasto);

        txtValorGasto.setText("");
        txtMedioPago.setText("");
        actualizarBarraPresupuesto();
        actualizarGrafico();

        String mensaje = getString(R.string.res_registrar,
                categoria,
                RepositorioGastos.formatoMoneda(valor),
                RepositorioGastos.formatoMoneda(repositorio.getSaldoDisponible()),
                repositorio.getPorcentajeConsumido());

        // RF8 / CU-08: la alerta extiende al registro de gasto
        if (repositorio.hayExcesoDePresupuesto()) {
            mensaje += "\n\n" + getString(R.string.res_exceso,
                    RepositorioGastos.formatoMoneda(repositorio.getMontoExcedido()));
        }

        mostrarResultado(mensaje);
    }

    // ------------------------------------------------------------------
    //  RF9 / CU-09: consultar el gasto por categoria
    // ------------------------------------------------------------------
    private void verGraficoMensual() {
        actualizarGrafico();

        int[] p = repositorio.getPorcentajePorCategoria();
        String mensaje = getString(R.string.res_grafico,
                RepositorioGastos.formatoMoneda(repositorio.getTotalGastado()),
                p[0], p[1], p[2], p[3], p[4]);

        mostrarResultado(mensaje);
    }

    // ------------------------------------------------------------------
    //  RF7 / CU-07: definir el presupuesto mensual
    // ------------------------------------------------------------------
    private void definirPresupuesto() {
        String texto = txtPresupuesto.getText().toString().trim();

        if (texto.isEmpty()) {
            txtPresupuesto.setError(getString(R.string.error_presupuesto_vacio));
            return;
        }

        long nuevo;
        try {
            nuevo = Long.parseLong(texto);
        } catch (NumberFormatException e) {
            txtPresupuesto.setError(getString(R.string.error_valor_invalido));
            return;
        }

        if (!repositorio.definirPresupuesto(nuevo)) {
            txtPresupuesto.setError(getString(R.string.error_valor_invalido));
            return;
        }

        actualizarBarraPresupuesto();

        mostrarResultado(getString(R.string.res_presupuesto,
                RepositorioGastos.formatoMoneda(nuevo),
                RepositorioGastos.formatoMoneda(repositorio.getSaldoDisponible()),
                repositorio.getPorcentajeConsumido()));
    }

    // ------------------------------------------------------------------
    //  RF6 / CU-06: registrar un gasto por escaneo de factura
    // ------------------------------------------------------------------
    private void escanearFactura() {
        // Se simula la lectura del comprobante proponiendo valor y categoria.
        Random azar = new Random();
        long valorLeido = (azar.nextInt(90) + 10) * 1000L;
        String categoriaLeida = RepositorioGastos.CATEGORIAS[
                azar.nextInt(RepositorioGastos.CATEGORIAS.length)];

        // El usuario confirma o corrige los datos antes de registrarlos.
        txtValorGasto.setText(String.valueOf(valorLeido));
        txtMedioPago.setText(getString(R.string.medio_escaneo));
        for (int i = 0; i < RepositorioGastos.CATEGORIAS.length; i++) {
            if (RepositorioGastos.CATEGORIAS[i].equals(categoriaLeida)) {
                spnCategoria.setSelection(i);
                break;
            }
        }

        mostrarResultado(getString(R.string.res_escanear,
                RepositorioGastos.formatoMoneda(valorLeido), categoriaLeida));
    }

    // ------------------------------------------------------------------
    //  Apoyo
    // ------------------------------------------------------------------

    /** Imagen de comprobante que corresponde a la categoria elegida. */
    private int imagenDeCategoria(String categoria) {
        if (getString(R.string.cat_alimentacion_larga).equals(categoria)) {
            return R.drawable.comp_mercado;
        } else if (getString(R.string.cat_transporte_larga).equals(categoria)) {
            return R.drawable.comp_transporte;
        } else if (getString(R.string.cat_hogar_larga).equals(categoria)) {
            return R.drawable.comp_energia;
        } else if (getString(R.string.cat_ocio_larga).equals(categoria)) {
            return R.drawable.comp_internet;
        } else {
            return R.drawable.comp_salud;
        }
    }

    /** Refleja el porcentaje consumido y cambia de color si hay exceso (RF8). */
    private void actualizarBarraPresupuesto() {
        int porcentaje = repositorio.getPorcentajeConsumido();
        barraPresupuesto.setProgress(Math.min(porcentaje, 100));

        int color = repositorio.hayExcesoDePresupuesto()
                ? R.color.rojo : R.color.verde_oscuro;
        barraPresupuesto.setProgressTintList(
                ContextCompat.getColorStateList(requireContext(), color));
    }

    /** Redibuja las cinco barras del grafico segun los gastos registrados. */
    private void actualizarGrafico() {
        int[] p = repositorio.getPorcentajePorCategoria();
        View[] barras = {barraAlimentacion, barraTransporte, barraHogar, barraOcio, barraSalud};
        TextView[] etiquetas = {pctAlimentacion, pctTransporte, pctHogar, pctOcio, pctSalud};

        final int ALTURA_MAXIMA_DP = 90;
        float densidad = getResources().getDisplayMetrics().density;

        for (int i = 0; i < barras.length; i++) {
            int alturaPx = (int) (Math.max(p[i], 2) * ALTURA_MAXIMA_DP / 100f * densidad);
            barras[i].getLayoutParams().height = alturaPx;
            barras[i].requestLayout();
            etiquetas[i].setText(getString(R.string.formato_porcentaje, p[i]));
        }
    }

    /** Escribe el resultado en pantalla y lo confirma con un Toast. */
    private void mostrarResultado(String mensaje) {
        txtResultadoAccion.setText(mensaje);
        String primeraLinea = mensaje.split("\n")[0];
        Toast.makeText(requireContext(), primeraLinea, Toast.LENGTH_SHORT).show();
    }
}
