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
import com.poligran.mibolsillo.model.Gasto;

import java.util.ArrayList;
import java.util.List;

/**
 * RF3 y RF4 / CU-03 y CU-04. Registro de gastos con su detalle.
 * Los datos son de ejemplo; en la Entrega 2 provienen del repositorio.
 */
public class FotosFragment extends Fragment {

    private GastoAdapter adaptador;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup contenedor,
                             @Nullable Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_fotos, contenedor, false);

        ListView lista = vista.findViewById(R.id.listaGastos);
        final TextView txtTitulo = vista.findViewById(R.id.txtDetalleTitulo);
        final TextView txtCategoria = vista.findViewById(R.id.txtDetalleCategoria);
        final TextView txtMedio = vista.findViewById(R.id.txtDetalleMedio);
        final TextView txtComprobante = vista.findViewById(R.id.txtDetalleComprobante);

        final List<Gasto> datos = datosDeEjemplo();
        adaptador = new GastoAdapter(requireContext(), datos);
        lista.setAdapter(adaptador);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> padre, View v, int posicion, long id) {
                adaptador.setSeleccionada(posicion);
                Gasto g = datos.get(posicion);
                txtTitulo.setText(g.getDescripcion() + " \u00b7 " + g.getFecha());
                txtCategoria.setText(getString(R.string.detalle_categoria,
                        g.getCategoria(), g.getValor()));
                txtMedio.setText(getString(R.string.detalle_medio, g.getMedioPago()));
                txtComprobante.setText(getString(R.string.detalle_comprobante, g.getComprobante()));
            }
        });

        return vista;
    }

    private List<Gasto> datosDeEjemplo() {
        List<Gasto> datos = new ArrayList<>();
        datos.add(new Gasto(R.drawable.bg_thumb_1, "Mercado", "12/03/2026",
                "Alimentacion", "$ 186.400", "Tarjeta debito", "Factura POS N.\u00ba 004512"));
        datos.add(new Gasto(R.drawable.bg_thumb_2, "Energia", "05/03/2026",
                "Hogar", "$ 142.900", "Debito automatico", "Recibo N.\u00ba 88231"));
        datos.add(new Gasto(R.drawable.bg_thumb_3, "Transporte", "03/03/2026",
                "Transporte", "$ 12.500", "Efectivo", "Tiquete N.\u00ba 1042"));
        datos.add(new Gasto(R.drawable.bg_thumb_4, "Arriendo", "01/03/2026",
                "Vivienda", "$ 950.000", "Transferencia", "Comprobante N.\u00ba 7781"));
        datos.add(new Gasto(R.drawable.bg_thumb_5, "Internet", "28/02/2026",
                "Servicios", "$ 89.900", "Tarjeta credito", "Factura N.\u00ba 55110"));
        datos.add(new Gasto(R.drawable.bg_thumb_6, "Medico", "20/02/2026",
                "Salud", "$ 64.000", "Efectivo", "Recibo N.\u00ba 2098"));
        return datos;
    }
}
