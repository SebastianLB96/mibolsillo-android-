package com.poligran.mibolsillo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.poligran.mibolsillo.R;

/** RF11 / CU-11. Navegador interno. */
public class WebFragment extends Fragment {

    private EditText txtUrl;
    private WebView navegador;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup contenedor,
                             @Nullable Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_web, contenedor, false);

        txtUrl    = vista.findViewById(R.id.txtUrl);
        navegador = vista.findViewById(R.id.webNavegador);
        Button btnIr = vista.findViewById(R.id.btnIr);

        navegador.setWebViewClient(new WebViewClient());
        navegador.getSettings().setJavaScriptEnabled(true);
        navegador.getSettings().setDomStorageEnabled(true);

        btnIr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarPagina();
            }
        });

        navegador.loadUrl(txtUrl.getText().toString());
        return vista;
    }

    private void cargarPagina() {
        String url = txtUrl.getText().toString().trim();

        if (url.isEmpty()) {
            Toast.makeText(requireContext(), R.string.web_url_vacia, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
            txtUrl.setText(url);
        }
        navegador.loadUrl(url);
    }
}
