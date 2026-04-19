package com.fitpro.myapplication2;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ExercicesFragment extends Fragment {

    private ExerciceAdapter adapter;
    private RecyclerView recycler;

    private final List<ExerciceModel> tousLesExercices = Arrays.asList(
            new ExerciceModel(1,  "Bench Press",    "Pectoraux", 4, "8-10",  "Intermédiaire", 180, "🏋️"),
            new ExerciceModel(2,  "Push-Up",        "Pectoraux", 3, "15-20", "Débutant",      90,  "💪"),
            new ExerciceModel(3,  "Incline Press",  "Pectoraux", 4, "10-12", "Avancé",        160, "🔥"),
            new ExerciceModel(4,  "Pull-Up",        "Dos",       4, "6-10",  "Intermédiaire", 150, "🦅"),
            new ExerciceModel(5,  "Deadlift",       "Dos",       3, "5-6",   "Avancé",        300, "💀"),
            new ExerciceModel(6,  "Lat Pulldown",   "Dos",       4, "10-12", "Débutant",      130, "🌊"),
            new ExerciceModel(7,  "Squat",          "Jambes",    4, "8-10",  "Intermédiaire", 250, "🦵"),
            new ExerciceModel(8,  "Leg Press",      "Jambes",    4, "10-12", "Débutant",      200, "🏔️"),
            new ExerciceModel(9,  "Overhead Press", "Épaules",   4, "8-10",  "Intermédiaire", 160, "🏆"),
            new ExerciceModel(10, "Lateral Raise",  "Épaules",   3, "12-15", "Débutant",      90,  "🦋"),
            new ExerciceModel(11, "Barbell Curl",   "Bras",      3, "10-12", "Débutant",      100, "💥"),
            new ExerciceModel(12, "Tricep Dip",     "Bras",      3, "12-15", "Intermédiaire", 110, "🔱"),
            new ExerciceModel(13, "Sprint HIIT",    "Cardio",    8, "30s",   "Avancé",        400, "⚡"),
            new ExerciceModel(14, "Burpees",        "Cardio",    4, "15",    "Intermédiaire", 280, "🌪️")
    );

    private final List<String> categories = Arrays.asList(
            "Tous", "Pectoraux", "Dos", "Jambes", "Épaules", "Bras", "Cardio"
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exercices, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recycler = view.findViewById(R.id.recyclerExercices);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new ExerciceAdapter(tousLesExercices, exercice -> {
            Bundle bundle = new Bundle();
            bundle.putInt("id", exercice.getId());
            bundle.putString("nom", exercice.getNom());
            bundle.putString("categorie", exercice.getCategorie());
            bundle.putInt("sets", exercice.getSets());
            bundle.putString("reps", exercice.getReps());
            bundle.putString("niveau", exercice.getNiveau());
            bundle.putInt("calories", exercice.getCalories());
            bundle.putString("emoji", exercice.getEmoji());
            Navigation.findNavController(view).navigate(R.id.detailFragment, bundle);
        });
        recycler.setAdapter(adapter);

        // Boutons de filtre
        ViewGroup layoutFiltres = view.findViewById(R.id.layoutFiltres);
        for (String cat : categories) {
            TextView btn = new TextView(requireContext());
            btn.setText(cat);
            btn.setTextSize(13f);
            btn.setTextColor(Color.WHITE);
            btn.setPadding(24, 16, 24, 16);
            btn.setBackgroundColor(Color.parseColor("#1A1A24"));

            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 8, 8, 8);
            btn.setLayoutParams(params);

            btn.setOnClickListener(v -> filtrerParCategorie(cat));
            layoutFiltres.addView(btn);
        }
    }

    private void filtrerParCategorie(String categorie) {
        List<ExerciceModel> listeFiltree;
        if (categorie.equals("Tous")) {
            listeFiltree = tousLesExercices;
        } else {
            listeFiltree = new ArrayList<>();
            for (ExerciceModel ex : tousLesExercices) {
                if (ex.getCategorie().equals(categorie)) {
                    listeFiltree.add(ex);
                }
            }
        }
        adapter.filtrer(listeFiltree);
    }
}