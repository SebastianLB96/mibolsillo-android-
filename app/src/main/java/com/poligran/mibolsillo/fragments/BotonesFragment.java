package com.poligran.mibolsillo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.poligran.mibolsillo.R;

/**
 * RF5 a RF9 / CU-05 a CU-09. Panel de operaciones.
 * Los cuatro botones ya estan en pantalla; sus eventos se programan en la Entrega 2.
 */
public class BotonesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup contenedor,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_botones, contenedor, false);
    }
}
