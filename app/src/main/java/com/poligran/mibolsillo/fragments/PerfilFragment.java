package com.poligran.mibolsillo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.poligran.mibolsillo.R;
import com.poligran.mibolsillo.data.RepositorioGastos;

/**
 * Perfil del usuario e indicadores del mes (RF2 / CU-02).
 *
 * Lee del repositorio y calcula presupuesto, total gastado, saldo disponible
 * y porcentaje consumido cada vez que el fragmento se muestra, por lo que un
 * gasto registrado en BotonesFragment se refleja aqui sin refrescar nada.
 */
public class PerfilFragment extends Fragment {

    // --- Variables vinculadas a las vistas del layout ---
    private TextView txtPresupuesto;
    private TextView txtGastado;
    private TextView txtCantidadRegistros;
    private TextView txtSaldo;
    private TextView txtConsumido;
    private ProgressBar barraPresupuesto;

    private RepositorioGastos repositorio;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup contenedor,
                             @Nullable Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_perfil, contenedor, false);
        repositorio = RepositorioGastos.getInstancia();

        // --- Vinculo de las variables con los identificadores del layout ---
        txtPresupuesto       = vista.findViewById(R.id.txtPresupuesto);
        txtGastado           = vista.findViewById(R.id.txtGastado);
        txtCantidadRegistros = vista.findViewById(R.id.txtCantidadRegistros);
        txtSaldo             = vista.findViewById(R.id.txtSaldo);
        txtConsumido         = vista.findViewById(R.id.txtConsumido);
        barraPresupuesto     = vista.findViewById(R.id.barraPresupuestoPerfil);

        mostrarIndicadores();
        return vista;
    }

    /** Se ejecuta cada vez que el fragmento vuelve a quedar visible. */
    @Override
    public void onResume() {
        super.onResume();
        mostrarIndicadores();
    }

    /** Calcula y escribe los cuatro indicadores financieros del mes. */
    private void mostrarIndicadores() {
        long presupuesto = repositorio.getPresupuesto();
        long gastado     = repositorio.getTotalGastado();
        long saldo       = repositorio.getSaldoDisponible();
        int  porcentaje  = repositorio.getPorcentajeConsumido();

        txtPresupuesto.setText(RepositorioGastos.formatoMoneda(presupuesto));
        txtGastado.setText(RepositorioGastos.formatoMoneda(gastado));
        txtCantidadRegistros.setText(getString(R.string.ind_registros,
                repositorio.getCantidadGastos()));
        txtSaldo.setText(RepositorioGastos.formatoMoneda(saldo));
        txtConsumido.setText(getString(R.string.ind_consumido, porcentaje));

        barraPresupuesto.setProgress(Math.min(porcentaje, 100));

        // El saldo negativo y la barra se pintan en rojo cuando hay exceso (RF8)
        int color = repositorio.hayExcesoDePresupuesto()
                ? R.color.rojo : R.color.verde_oscuro;
        txtSaldo.setTextColor(ContextCompat.getColor(requireContext(), color));
        barraPresupuesto.setProgressTintList(
                ContextCompat.getColorStateList(requireContext(), color));
    }
}
