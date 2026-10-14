package com.poligran.mibolsillo.fragments;

import android.graphics.SurfaceTexture;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.poligran.mibolsillo.R;

/**
 * Reproductor del video de educacion financiera (RF10 / CU-10).
 *
 * El video se encuentra en res/raw/video_finanzas.mp4, por lo que la
 * reproduccion no depende de la conexion a internet.
 *
 * Se emplea TextureView y no VideoView porque este ultimo dibuja sobre una
 * superficie independiente de la jerarquia de vistas, y en varios emuladores
 * esa superficie no se compone: el video avanza pero la pantalla se ve negra.
 * TextureView se dibuja como cualquier otra vista y evita ese problema.
 */
public class VideoFragment extends Fragment
        implements TextureView.SurfaceTextureListener, MediaController.MediaPlayerControl {

    // --- Variables vinculadas a las vistas del layout ---
    private TextureView texturaVideo;

    private MediaPlayer reproductor;
    private MediaController controles;
    private boolean preparado = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup contenedor,
                             @Nullable Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_video, contenedor, false);

        // --- Vinculo de la variable con el identificador del layout ---
        texturaVideo = vista.findViewById(R.id.texturaVideo);
        texturaVideo.setSurfaceTextureListener(this);

        // Controles de reproduccion, pausa y avance
        controles = new MediaController(requireContext());
        controles.setAnchorView(texturaVideo);
        controles.setMediaPlayer(this);

        // Al tocar el video se muestran u ocultan los controles
        texturaVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controles.isShowing()) {
                    controles.hide();
                } else {
                    controles.show(3000);
                }
            }
        });

        return vista;
    }

    // ------------------------------------------------------------------
    //  Ciclo de vida de la superficie de dibujo
    // ------------------------------------------------------------------

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture textura, int ancho, int alto) {
        iniciarReproductor(new Surface(textura));
    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture textura) {
        liberarReproductor();
        return true;
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture t, int a, int h) { }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture t) { }

    // ------------------------------------------------------------------
    //  Reproductor
    // ------------------------------------------------------------------

    /** Prepara el reproductor con el video de los recursos de la aplicacion. */
    private void iniciarReproductor(Surface superficie) {
        Uri recurso = Uri.parse("android.resource://" + requireContext().getPackageName()
                + "/" + R.raw.video_finanzas);
        try {
            reproductor = new MediaPlayer();
            reproductor.setDataSource(requireContext(), recurso);
            reproductor.setSurface(superficie);
            reproductor.setAudioAttributes(new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                    .build());

            reproductor.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    preparado = true;
                    // Se adelanta un instante para que se vea el primer fotograma
                    mp.seekTo(1);
                    controles.setEnabled(true);
                    controles.show(3000);
                }
            });

            reproductor.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.seekTo(1);
                    controles.show(3000);
                }
            });

            reproductor.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int que, int extra) {
                    Toast.makeText(requireContext(), R.string.video_error,
                            Toast.LENGTH_LONG).show();
                    return true;
                }
            });

            reproductor.prepareAsync();

        } catch (Exception e) {
            Toast.makeText(requireContext(), R.string.video_error, Toast.LENGTH_LONG).show();
        }
    }

    /** Libera los recursos del reproductor. */
    private void liberarReproductor() {
        preparado = false;
        if (reproductor != null) {
            reproductor.release();
            reproductor = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (preparado && reproductor != null && reproductor.isPlaying()) {
            reproductor.pause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (controles != null) {
            controles.hide();
        }
        liberarReproductor();
    }

    // ------------------------------------------------------------------
    //  Contrato que exige MediaController para manejar la reproduccion
    // ------------------------------------------------------------------

    @Override
    public void start() {
        if (preparado && reproductor != null) {
            reproductor.start();
        }
    }

    @Override
    public void pause() {
        if (preparado && reproductor != null) {
            reproductor.pause();
        }
    }

    @Override
    public int getDuration() {
        return (preparado && reproductor != null) ? reproductor.getDuration() : 0;
    }

    @Override
    public int getCurrentPosition() {
        return (preparado && reproductor != null) ? reproductor.getCurrentPosition() : 0;
    }

    @Override
    public void seekTo(int posicion) {
        if (preparado && reproductor != null) {
            reproductor.seekTo(posicion);
        }
    }

    @Override
    public boolean isPlaying() {
        return preparado && reproductor != null && reproductor.isPlaying();
    }

    @Override public int getBufferPercentage()  { return 0; }
    @Override public boolean canPause()         { return true; }
    @Override public boolean canSeekBackward()  { return true; }
    @Override public boolean canSeekForward()   { return true; }
    @Override public int getAudioSessionId()    { return 0; }
}
