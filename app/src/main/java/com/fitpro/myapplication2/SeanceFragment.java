package com.fitpro.myapplication2;

import android.os.Bundle;
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
import java.util.List;

public class SeanceFragment extends Fragment {

    private SeanceViewModel seanceViewModel;
    private SeanceAdapter adapter;

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

        // ViewModel partagé avec l'Activity
        seanceViewModel = new ViewModelProvider(requireActivity()).get(SeanceViewModel.class);

        RecyclerView recycler   = view.findViewById(R.id.recyclerSeance);
        View layoutVide         = view.findViewById(R.id.layoutVide);
        Button btnDemarrer      = view.findViewById(R.id.btnDemarrer);
        TextView tvTotalCal     = view.findViewById(R.id.tvTotalCal);

        // Initialise le RecyclerView
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new SeanceAdapter(new ArrayList<>(), exercice ->
                seanceViewModel.supprimerExercice(exercice)
        );
        recycler.setAdapter(adapter);

        // Observer la liste des exercices
        seanceViewModel.getExercices().observe(getViewLifecycleOwner(), liste -> {
            if (liste == null || liste.isEmpty()) {
                // Afficher écran vide
                recycler.setVisibility(View.GONE);
                layoutVide.setVisibility(View.VISIBLE);
                btnDemarrer.setEnabled(false);
                tvTotalCal.setText("🔥 0 kcal");
            } else {
                // Afficher la liste
                recycler.setVisibility(View.VISIBLE);
                layoutVide.setVisibility(View.GONE);
                btnDemarrer.setEnabled(true);
                adapter.mettreAJour(liste);
                tvTotalCal.setText("🔥 " + seanceViewModel.totalCalories() + " kcal");
            }
        });

        // Bouton démarrer la séance
        btnDemarrer.setOnClickListener(v ->
                Toast.makeText(
                        requireContext(),
                        "💪 Séance démarrée ! Bonne chance !",
                        Toast.LENGTH_SHORT
                ).show()
        );
    }
}