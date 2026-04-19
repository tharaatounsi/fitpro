package com.fitpro.myapplication2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Clique sur une catégorie → va vers Exercices
        int[] cardIds = {
                R.id.cardPectoraux,
                R.id.cardDos,
                R.id.cardJambes,
                R.id.cardEpaules,
                R.id.cardBras,
                R.id.cardCardio
        };

        for (int id : cardIds) {
            CardView card = view.findViewById(id);
            card.setOnClickListener(v ->
                    Navigation.findNavController(view).navigate(R.id.exercicesFragment)
            );
        }
    }
}