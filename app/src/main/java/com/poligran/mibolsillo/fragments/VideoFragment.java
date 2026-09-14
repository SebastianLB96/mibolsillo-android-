package com.poligran.mibolsillo.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.poligran.mibolsillo.R;

/** RF10 / CU-10. Reproductor del video de educacion financiera. */
public class VideoFragment extends Fragment {

    private static final String VIDEO_RESPALDO =
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup contenedor,
                             @Nullable Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_video, contenedor, false);
        VideoView videoView = vista.findViewById(R.id.videoFinanzas);

        int idRecurso = getResources().getIdentifier(
                "video_finanzas", "raw", requireContext().getPackageName());

        if (idRecurso != 0) {
            videoView.setVideoURI(Uri.parse(
                    "android.resource://" + requireContext().getPackageName() + "/" + idRecurso));
        } else {
            videoView.setVideoURI(Uri.parse(VIDEO_RESPALDO));
        }

        MediaController controles = new MediaController(requireContext());
        controles.setAnchorView(videoView);
        videoView.setMediaController(controles);
        videoView.requestFocus();

        return vista;
    }
}
