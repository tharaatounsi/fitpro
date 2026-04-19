package com.fitpro.myapplication2;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SeanceAdapter extends RecyclerView.Adapter<SeanceAdapter.ViewHolder> {

    private List<ExerciceModel> liste;
    private final OnSupprimerListener onSupprimer;

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
        public final TextView btnMenu;
        public final LinearLayout layoutCercles;
        public final ProgressBar progressSets;

        public ViewHolder(View view) {
            super(view);
            tvEmoji       = view.findViewById(R.id.tvSeanceEmoji);
            tvNom         = view.findViewById(R.id.tvSeanceNom);
            tvSetsReps    = view.findViewById(R.id.tvSeanceSetsReps);
            tvCalories    = view.findViewById(R.id.tvSeanceCalories);
            btnMenu       = view.findViewById(R.id.btnMenu);
            layoutCercles = view.findViewById(R.id.layoutCercles);
            progressSets  = view.findViewById(R.id.progressSets);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_seance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExerciceModel ex = liste.get(position);
        Context context  = holder.itemView.getContext();

        // Remplir les données
        holder.tvEmoji.setText(ex.getEmoji());
        holder.tvNom.setText(ex.getNom());
        holder.tvSetsReps.setText(ex.getSets() + " sets × " + ex.getReps());
        holder.tvCalories.setText(String.valueOf(ex.getCalories()));

        // Créer les cercles sets
        int totalSets = ex.getSets();
        boolean[] setsCompletes = new boolean[totalSets];
        holder.layoutCercles.removeAllViews();

        for (int i = 0; i < totalSets; i++) {
            final int index = i;
            TextView cercle = new TextView(context);
            cercle.setWidth(44);
            cercle.setHeight(44);
            cercle.setGravity(android.view.Gravity.CENTER);
            cercle.setText(String.valueOf(i + 1));
            cercle.setTextSize(12f);
            cercle.setTextColor(Color.WHITE);
            cercle.setBackgroundResource(R.drawable.bg_niveau);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80, 80);
            params.setMargins(8, 0, 8, 0);
            cercle.setLayoutParams(params);

            // Clic sur cercle
            cercle.setOnClickListener(v -> {
                setsCompletes[index] = !setsCompletes[index];

                if (setsCompletes[index]) {
                    // Set complété → vert
                    cercle.setBackgroundColor(Color.parseColor("#06D6A0"));
                    cercle.setText("✓");

                    // Mettre à jour la barre de progression
                    int completes = compterCompletes(setsCompletes);
                    holder.progressSets.setProgress((completes * 100) / totalSets);

                    // Afficher timer de repos
                    afficherTimerRepos(context);

                } else {
                    // Set annulé → gris
                    cercle.setBackgroundColor(Color.parseColor("#2A2A3A"));
                    cercle.setText(String.valueOf(index + 1));
                    cercle.setTextColor(Color.WHITE);

                    int completes = compterCompletes(setsCompletes);
                    holder.progressSets.setProgress((completes * 100) / totalSets);
                }
            });

            holder.layoutCercles.addView(cercle);
        }

        // Progression initiale
        holder.progressSets.setProgress(0);

        // Menu ⋮ → Supprimer
        holder.btnMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, holder.btnMenu);
            popup.getMenu().add("🗑 Supprimer");
            popup.setOnMenuItemClickListener(item -> {
                if (onSupprimer != null) onSupprimer.onSupprimer(ex);
                return true;
            });
            popup.show();
        });
    }

    private int compterCompletes(boolean[] sets) {
        int count = 0;
        for (boolean s : sets) if (s) count++;
        return count;
    }

    private void afficherTimerRepos(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("⏱ Temps de repos");
        builder.setCancelable(false);

        TextView tvTimer = new TextView(context);
        tvTimer.setTextSize(48f);
        tvTimer.setGravity(android.view.Gravity.CENTER);
        tvTimer.setTextColor(Color.parseColor("#06D6A0"));
        tvTimer.setPadding(0, 32, 0, 32);
        tvTimer.setText("60s");
        builder.setView(tvTimer);

        builder.setNegativeButton("Passer", null);

        AlertDialog dialog = builder.create();
        dialog.show();

        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisRestants) {
                long secs = millisRestants / 1000;
                tvTimer.setText(secs + "s");

                if (secs <= 10) {
                    tvTimer.setTextColor(Color.parseColor("#FF4D6D"));
                } else if (secs <= 30) {
                    tvTimer.setTextColor(Color.parseColor("#FFB703"));
                } else {
                    tvTimer.setTextColor(Color.parseColor("#06D6A0"));
                }
            }

            @Override
            public void onFinish() {
                tvTimer.setText("✅ Go !");
                tvTimer.setTextColor(Color.parseColor("#06D6A0"));
                if (dialog.isShowing()) dialog.dismiss();
            }
        }.start();
    }

    @Override
    public int getItemCount() {
        return liste != null ? liste.size() : 0;
    }

    public void mettreAJour(List<ExerciceModel> nouvelleListe) {
        this.liste = nouvelleListe;
        notifyDataSetChanged();
    }
}