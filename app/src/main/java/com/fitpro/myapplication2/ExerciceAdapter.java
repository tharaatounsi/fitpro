package com.fitpro.myapplication2;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExerciceAdapter extends RecyclerView.Adapter<ExerciceAdapter.ViewHolder> {

    public interface OnExerciceClickListener {
        void onClick(ExerciceModel exercice);
    }

    private List<ExerciceModel> liste;
    private final OnExerciceClickListener listener;

    public ExerciceAdapter(List<ExerciceModel> liste, OnExerciceClickListener listener) {
        this.liste = liste;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmoji, tvNom, tvNiveau, tvSetsReps, tvCalories;

        public ViewHolder(View view) {
            super(view);
            tvEmoji    = view.findViewById(R.id.tvEmoji);
            tvNom      = view.findViewById(R.id.tvNom);
            tvNiveau   = view.findViewById(R.id.tvNiveau);
            tvSetsReps = view.findViewById(R.id.tvSetsReps);
            tvCalories = view.findViewById(R.id.tvCalories);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExerciceModel ex = liste.get(position);

        holder.tvEmoji.setText(ex.getEmoji());
        holder.tvNom.setText(ex.getNom());
        holder.tvNiveau.setText(ex.getNiveau());
        holder.tvSetsReps.setText(ex.getSets() + " sets × " + ex.getReps());
        holder.tvCalories.setText(String.valueOf(ex.getCalories()));

        // Couleur selon niveau
        String couleur;
        switch (ex.getNiveau()) {
            case "Débutant":      couleur = "#06D6A0"; break;
            case "Intermédiaire": couleur = "#FFB703"; break;
            case "Avancé":        couleur = "#FF4D6D"; break;
            default:              couleur = "#FFFFFF";  break;
        }
        holder.tvNiveau.setTextColor(Color.parseColor(couleur));

        holder.itemView.setOnClickListener(v -> listener.onClick(ex));
    }

    @Override
    public int getItemCount() {
        return liste.size();
    }

    public void filtrer(List<ExerciceModel> nouvelleListe) {
        liste = nouvelleListe;
        notifyDataSetChanged();
    }
}