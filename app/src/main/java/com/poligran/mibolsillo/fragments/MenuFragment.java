package com.poligran.mibolsillo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.poligran.mibolsillo.R;
import com.poligran.mibolsillo.adapters.MenuAdapter;

/** Fragmento izquierdo: lista vertical con las cinco opciones. */
public class MenuFragment extends Fragment {

    public interface OnOpcionSeleccionadaListener {
        void onOpcionSeleccionada(int posicion);
    }

    private OnOpcionSeleccionadaListener listener;
    private MenuAdapter adaptador;

    @Override
    public void onAttach(@NonNull Context contexto) {
        super.onAttach(contexto);
        if (contexto instanceof OnOpcionSeleccionadaListener) {
            listener = (OnOpcionSeleccionadaListener) contexto;
        } else {
            throw new IllegalStateException(
                    "La actividad debe implementar OnOpcionSeleccionadaListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup contenedor,
                             @Nullable Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_menu, contenedor, false);
        ListView lista = vista.findViewById(R.id.listaMenu);

        String[] titulos = getResources().getStringArray(R.array.opciones_menu);
        int[] iconos = {
                R.drawable.ic_perfil,
                R.drawable.ic_fotos,
                R.drawable.ic_video,
                R.drawable.ic_web,
                R.drawable.ic_botones
        };

        adaptador = new MenuAdapter(requireContext(), titulos, iconos);
        lista.setAdapter(adaptador);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> padre, View v, int posicion, long id) {
                adaptador.setSeleccionada(posicion);
                if (listener != null) {
                    listener.onOpcionSeleccionada(posicion);
                }
            }
        });

        return vista;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
