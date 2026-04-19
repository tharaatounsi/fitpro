package com.fitpro.myapplication2;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SeanceFragment extends Fragment {

    private SeanceViewModel seanceViewModel;
    private SeanceAdapter adapter;
    private CountDownTimer countDownTimer;
    private boolean timerEnCours = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seance, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        seanceViewModel = new ViewModelProvider(requireActivity()).get(SeanceViewModel.class);

        RecyclerView recycler   = view.findViewById(R.id.recyclerSeance);
        View layoutVide         = view.findViewById(R.id.layoutVide);
        Button btnDemarrer      = view.findViewById(R.id.btnDemarrer);
        TextView tvTotalCal     = view.findViewById(R.id.tvTotalCal);
        TextView tvTimer        = view.findViewById(R.id.tvTimer);
        Button btnTimer         = view.findViewById(R.id.btnTimer);

        // Initialise le RecyclerView
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new SeanceAdapter(new ArrayList<>(), exercice ->
                seanceViewModel.supprimerExercice(exercice)
        );
        recycler.setAdapter(adapter);

        // Observer la liste des exercices
        seanceViewModel.getExercices().observe(getViewLifecycleOwner(), liste -> {
            if (liste == null || liste.isEmpty()) {
                recycler.setVisibility(View.GONE);
                layoutVide.setVisibility(View.VISIBLE);
                btnDemarrer.setEnabled(false);
                tvTotalCal.setText("🔥 0 kcal");
                tvTimer.setVisibility(View.GONE);
                btnTimer.setVisibility(View.GONE);
            } else {
                recycler.setVisibility(View.VISIBLE);
                layoutVide.setVisibility(View.GONE);
                btnDemarrer.setEnabled(true);
                adapter.mettreAJour(liste);
                tvTotalCal.setText("🔥 " + seanceViewModel.totalCalories() + " kcal");
            }
        });

        // Bouton démarrer la séance
        btnDemarrer.setOnClickListener(v -> {
            Toast.makeText(requireContext(),
                    "💪 Séance démarrée ! Bonne chance !",
                    Toast.LENGTH_SHORT).show();

            // Afficher le timer
            tvTimer.setVisibility(View.VISIBLE);
            btnTimer.setVisibility(View.VISIBLE);
            btnTimer.setText("⏱ Démarrer repos (60s)");
        });

        // Bouton timer repos
        btnTimer.setOnClickListener(v -> {
            if (timerEnCours) {
                // Arrêter le timer
                if (countDownTimer != null) countDownTimer.cancel();
                timerEnCours = false;
                tvTimer.setText("⏱ 60s");
                btnTimer.setText("⏱ Démarrer repos (60s)");
                tvTimer.setTextColor(android.graphics.Color.parseColor("#06D6A0"));
            } else {
                // Démarrer le timer
                demarrerTimer(60, tvTimer, btnTimer);
            }
        });
    }

    private void demarrerTimer(int secondes, TextView tvTimer, Button btnTimer) {
        timerEnCours = true;
        btnTimer.setText("⏹ Arrêter");

        countDownTimer = new CountDownTimer(secondes * 1000L, 1000) {
            @Override
            public void onTick(long millisRestants) {
                long secsRestantes = millisRestants / 1000;
                tvTimer.setText("⏱ " + secsRestantes + "s");

                // Couleur selon le temps restant
                if (secsRestantes <= 10) {
                    tvTimer.setTextColor(android.graphics.Color.parseColor("#FF4D6D"));
                } else if (secsRestantes <= 30) {
                    tvTimer.setTextColor(android.graphics.Color.parseColor("#FFB703"));
                } else {
                    tvTimer.setTextColor(android.graphics.Color.parseColor("#06D6A0"));
                }
            }

            @Override
            public void onFinish() {
                timerEnCours = false;
                tvTimer.setText("✅ Repos terminé !");
                tvTimer.setTextColor(android.graphics.Color.parseColor("#06D6A0"));
                btnTimer.setText("⏱ Démarrer repos (60s)");
                Toast.makeText(requireContext(),
                        "✅ Repos terminé ! Reprends l'exercice !",
                        Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}