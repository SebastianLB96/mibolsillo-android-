package com.poligran.mibolsillo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.poligran.mibolsillo.R;

/** Adaptador del menu lateral izquierdo. */
public class MenuAdapter extends BaseAdapter {

    private final Context contexto;
    private final String[] titulos;
    private final int[] iconos;
    private int seleccionada = 0;

    public MenuAdapter(Context contexto, String[] titulos, int[] iconos) {
        this.contexto = contexto;
        this.titulos = titulos;
        this.iconos = iconos;
    }

    public void setSeleccionada(int posicion) {
        this.seleccionada = posicion;
        notifyDataSetChanged();
    }

    @Override public int getCount()        { return titulos.length; }
    @Override public Object getItem(int i) { return titulos[i]; }
    @Override public long getItemId(int i) { return i; }

    @Override
    public View getView(int posicion, View convertView, ViewGroup padre) {
        View fila = convertView;
        if (fila == null) {
            fila = LayoutInflater.from(contexto).inflate(R.layout.item_menu, padre, false);
        }

        ImageView icono = fila.findViewById(R.id.imgIconoMenu);
        TextView texto  = fila.findViewById(R.id.txtTituloMenu);

        icono.setImageResource(iconos[posicion]);
        texto.setText(titulos[posicion]);

        boolean activa = posicion == seleccionada;
        fila.setBackgroundColor(ContextCompat.getColor(contexto,
                activa ? R.color.verde_primario : R.color.gris_menu));
        int color = ContextCompat.getColor(contexto,
                activa ? R.color.blanco : R.color.gris_texto);
        texto.setTextColor(color);
        icono.setColorFilter(color);

        return fila;
    }
}
