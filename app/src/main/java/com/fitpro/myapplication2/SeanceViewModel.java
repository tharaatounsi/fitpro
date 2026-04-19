package com.fitpro.myapplication2;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SeanceViewModel extends ViewModel {

    // Liste des exercices ajoutés à la séance
    private final MutableLiveData<List<ExerciceModel>> exercices =
            new MutableLiveData<>(new ArrayList<>());

    public MutableLiveData<List<ExerciceModel>> getExercices() {
        return exercices;
    }

    // Ajouter un exercice à la séance
    public void ajouterExercice(ExerciceModel exercice) {
        List<ExerciceModel> liste = exercices.getValue();
        if (liste == null) liste = new ArrayList<>();

        // Vérifier que l'exercice n'est pas déjà dans la liste
        for (ExerciceModel e : liste) {
            if (e.getId() == exercice.getId()) return; // déjà présent
        }

        liste.add(exercice);
        exercices.setValue(liste);
    }

    // Supprimer un exercice de la séance
    public void supprimerExercice(ExerciceModel exercice) {
        List<ExerciceModel> liste = exercices.getValue();
        if (liste == null) return;

        liste.removeIf(e -> e.getId() == exercice.getId());
        exercices.setValue(liste);
    }

    // Calculer le total des calories
    public int totalCalories() {
        List<ExerciceModel> liste = exercices.getValue();
        if (liste == null) return 0;

        int total = 0;
        for (ExerciceModel e : liste) {
            total += e.getCalories();
        }
        return total;
    }
}