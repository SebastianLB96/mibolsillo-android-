package com.poligran.mibolsillo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.poligran.mibolsillo.R;
import com.poligran.mibolsillo.adapters.GastoAdapter;
import com.poligran.mibolsillo.data.RepositorioGastos;
import com.poligran.mibolsillo.model.Gasto;

import java.util.List;

/**
 * Registro de gastos y detalle del movimiento (RF3 y RF4 / CU-03 y CU-04).
 *
 * La lista se llena desde el repositorio, por lo que incluye los gastos
 * registrados durante la sesion desde el panel de operaciones.
 */
public class FotosFragment extends Fragment {

    // --- Variables vinculadas a las vistas del layout ---
    private ListView listaGastos;
    private TextView txtDetalleTitulo;
    private TextView txtDetalleCategoria;
    private TextView txtDetalleMedio;
    private TextView txtDetalleComprobante;

    private GastoAdapter adaptador;
    private List<Gasto> datos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup contenedor,
                             @Nullable Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_fotos, contenedor, false);

        // --- Vinculo de las variables con los identificadores del layout ---
        listaGastos           = vista.findViewById(R.id.listaGastos);
        txtDetalleTitulo      = vista.findViewById(R.id.txtDetalleTitulo);
        txtDetalleCategoria   = vista.findViewById(R.id.txtDetalleCategoria);
        txtDetalleMedio       = vista.findViewById(R.id.txtDetalleMedio);
        txtDetalleComprobante = vista.findViewById(R.id.txtDetalleComprobante);

        datos = RepositorioGastos.getInstancia().getGastos();
        adaptador = new GastoAdapter(requireContext(), datos);
        listaGastos.setAdapter(adaptador);

        // --- Declaracion del evento de seleccion de la lista (RF4) ---
        listaGastos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> padre, View v, int posicion, long id) {
                mostrarDetalle(posicion);
            }
        });

        if (!datos.isEmpty()) {
            mostrarDetalle(0);
        }

        return vista;
    }

    /** Escribe en el panel inferior los datos del gasto seleccionado. */
    private void mostrarDetalle(int posicion) {
        adaptador.setSeleccionada(posicion);
        Gasto g = datos.get(posicion);

        txtDetalleTitulo.setText(getString(R.string.detalle_encabezado,
                g.getDescripcion(), g.getFecha()));
        txtDetalleCategoria.setText(getString(R.string.detalle_categoria,
                g.getCategoria(), g.getValorFormateado()));
        txtDetalleMedio.setText(getString(R.string.detalle_medio, g.getMedioPago()));
        txtDetalleComprobante.setText(getString(R.string.detalle_comprobante,
                g.getComprobante()));
    }
}
