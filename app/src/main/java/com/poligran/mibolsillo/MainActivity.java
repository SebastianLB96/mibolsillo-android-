package com.poligran.mibolsillo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.poligran.mibolsillo.fragments.BotonesFragment;
import com.poligran.mibolsillo.fragments.FotosFragment;
import com.poligran.mibolsillo.fragments.MenuFragment;
import com.poligran.mibolsillo.fragments.PerfilFragment;
import com.poligran.mibolsillo.fragments.VideoFragment;
import com.poligran.mibolsillo.fragments.WebFragment;

/**
 * RF1 / CU-01. Actividad unica que aloja los dos fragmentos.
 * Izquierda: MenuFragment.  Derecha: contenido de la opcion activa.
 */
public class MainActivity extends AppCompatActivity
        implements MenuFragment.OnOpcionSeleccionadaListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedorMenu, new MenuFragment())
                    .replace(R.id.contenedorContenido, new PerfilFragment())
                    .commit();
        }
    }

    @Override
    public void onOpcionSeleccionada(int posicion) {
        Fragment destino;
        switch (posicion) {
            case 1:  destino = new FotosFragment();   break;
            case 2:  destino = new VideoFragment();   break;
            case 3:  destino = new WebFragment();     break;
            case 4:  destino = new BotonesFragment(); break;
            default: destino = new PerfilFragment();
        }

        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.contenedorContenido, destino)
                .commit();
    }
}
