package com.fitpro.myapplication2;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SeanceAdapter extends RecyclerView.Adapter<SeanceAdapter.ViewHolder> {

    private List<ExerciceModel> liste;
    private final OnSupprimerListener onSupprimer;

    // Interface pour le callback de suppression (équivalent du lambda Kotlin)
    public interface OnSupprimerListener {
        void onSupprimer(ExerciceModel exercice);
    }

    public SeanceAdapter(List<ExerciceModel> liste, OnSupprimerListener onSupprimer) {
        this.liste = liste;
        this.onSupprimer = onSupprimer;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvEmoji;
        public final TextView tvNom;
        public final TextView tvSetsReps;
        public final TextView tvCalories;
        public final Button btnSupprimer;

        public ViewHolder(View view) {
            super(view);
            tvEmoji = view.findViewById(R.id.tvSeanceEmoji);
            tvNom = view.findViewById(R.id.tvSeanceNom);
            tvSetsReps = view.findViewById(R.id.tvSeanceSetsReps);
            tvCalories = view.findViewById(R.id.tvSeanceCalories);
            btnSupprimer = view.findViewById(R.id.btnSupprimer);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_seance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ExerciceModel ex = liste.get(position);

        // Remplir les données
        holder.tvEmoji.setText(ex.getEmoji());
        holder.tvNom.setText(ex.getNom());
        holder.tvSetsReps.setText(ex.getSets() + " sets × " + ex.getReps());
        holder.tvCalories.setText(String.valueOf(ex.getCalories()));

        // Bouton supprimer
        holder.btnSupprimer.setOnClickListener(v -> {
            if (onSupprimer != null) {
                onSupprimer.onSupprimer(ex);
            }
        });
    }

    @Override
    public int getItemCount() {
        return liste != null ? liste.size() : 0;
    }

    // Méthode pour mettre à jour la liste
    public void mettreAJour(List<ExerciceModel> nouvelleListe) {
        this.liste = nouvelleListe;
        notifyDataSetChanged();
    }
}