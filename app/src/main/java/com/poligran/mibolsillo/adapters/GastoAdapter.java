package com.poligran.mibolsillo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.poligran.mibolsillo.R;
import com.poligran.mibolsillo.model.Gasto;

import java.util.List;

/** Adaptador de la lista de gastos del fragmento Fotos (RF3). */
public class GastoAdapter extends BaseAdapter {

    private static final int[] MINIATURAS = {
            R.drawable.bg_thumb_1, R.drawable.bg_thumb_2, R.drawable.bg_thumb_3,
            R.drawable.bg_thumb_4, R.drawable.bg_thumb_5, R.drawable.bg_thumb_6
    };

    private final Context contexto;
    private final List<Gasto> datos;
    private int seleccionada = 0;

    public GastoAdapter(Context contexto, List<Gasto> datos) {
        this.contexto = contexto;
        this.datos = datos;
    }

    public void setSeleccionada(int posicion) {
        this.seleccionada = posicion;
        notifyDataSetChanged();
    }

    @Override public int getCount()        { return datos.size(); }
    @Override public Object getItem(int i) { return datos.get(i); }
    @Override public long getItemId(int i) { return i; }

    @Override
    public View getView(int posicion, View convertView, ViewGroup padre) {
        View fila = convertView;
        if (fila == null) {
            fila = LayoutInflater.from(contexto).inflate(R.layout.item_gasto, padre, false);
        }

        Gasto g = datos.get(posicion);

        View miniatura   = fila.findViewById(R.id.viewMiniatura);
        TextView desc    = fila.findViewById(R.id.txtDescripcionGasto);
        TextView resumen = fila.findViewById(R.id.txtResumenGasto);
        TextView valor   = fila.findViewById(R.id.txtValorGasto);

        int indice = Math.max(0, Math.min(g.getNumeroMiniatura() - 1, MINIATURAS.length - 1));
        miniatura.setBackgroundResource(MINIATURAS[indice]);
        desc.setText(g.getDescripcion());
        resumen.setText(g.getResumen());
        valor.setText(g.getValorFormateado());

        fila.setBackgroundResource(posicion == seleccionada
                ? R.drawable.bg_fila_seleccionada : R.drawable.bg_fila);

        return fila;
    }
}
