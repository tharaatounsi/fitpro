package com.fitpro.myapplication2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EvenementAdapter extends RecyclerView.Adapter<EvenementAdapter.ViewHolder> {

    private List<EvenementModel> liste;

    public EvenementAdapter(List<EvenementModel> liste) {
        this.liste = liste;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmoji, tvTitre, tvDescription, tvDate, tvLieu;

        public ViewHolder(View view) {
            super(view);
            tvEmoji       = view.findViewById(R.id.tvEmojiEvenement);
            tvTitre       = view.findViewById(R.id.tvTitreEvenement);
            tvDescription = view.findViewById(R.id.tvDescriptionEvenement);
            tvDate        = view.findViewById(R.id.tvDateEvenement);
            tvLieu        = view.findViewById(R.id.tvLieuEvenement);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_evenement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EvenementModel ev = liste.get(position);
        holder.tvEmoji.setText(ev.getEmoji());
        holder.tvTitre.setText(ev.getTitre());
        holder.tvDescription.setText(ev.getDescription());
        holder.tvDate.setText("📅 " + ev.getDate());
        holder.tvLieu.setText("📍 " + ev.getLieu());
    }

    @Override
    public int getItemCount() { return liste.size(); }

    public void mettreAJour(List<EvenementModel> nouvelleListe) {
        this.liste = nouvelleListe;
        notifyDataSetChanged();
    }
}