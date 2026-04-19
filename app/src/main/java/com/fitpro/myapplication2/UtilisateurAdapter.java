package com.fitpro.myapplication2;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UtilisateurAdapter extends RecyclerView.Adapter<UtilisateurAdapter.ViewHolder> {

    private List<UtilisateurModel> liste;

    public UtilisateurAdapter(List<UtilisateurModel> liste) {
        this.liste = liste;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNom, tvEmail, tvAbonnement, tvStatut;

        public ViewHolder(View view) {
            super(view);
            tvNom        = view.findViewById(R.id.tvNomUser);
            tvEmail      = view.findViewById(R.id.tvEmailUser);
            tvAbonnement = view.findViewById(R.id.tvAbonnementUser);
            tvStatut     = view.findViewById(R.id.tvStatutUser);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_utilisateur, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UtilisateurModel user = liste.get(position);

        holder.tvNom.setText(user.getNom());
        holder.tvEmail.setText(user.getEmail());
        holder.tvAbonnement.setText("Expire : " + user.getDateExpiration());

        String statut = user.getStatutAbonnement();
        if ("actif".equals(statut)) {
            holder.tvStatut.setText("Actif");
            holder.tvStatut.setTextColor(Color.parseColor("#06D6A0"));
        } else {
            holder.tvStatut.setText("Inactif");
            holder.tvStatut.setTextColor(Color.parseColor("#FF4D6D"));
        }
    }

    @Override
    public int getItemCount() { return liste.size(); }

    public void mettreAJour(List<UtilisateurModel> nouvelleListe) {
        this.liste = nouvelleListe;
        notifyDataSetChanged();
    }
}