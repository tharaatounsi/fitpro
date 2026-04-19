package com.fitpro.myapplication2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PublicationAdapter extends RecyclerView.Adapter<PublicationAdapter.ViewHolder> {

    private List<PublicationModel> liste;

    public PublicationAdapter(List<PublicationModel> liste) {
        this.liste = liste;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitre, tvContenu, tvDate;

        public ViewHolder(View view) {
            super(view);
            tvTitre   = view.findViewById(R.id.tvTitrePublication);
            tvContenu = view.findViewById(R.id.tvContenuPublication);
            tvDate    = view.findViewById(R.id.tvDatePublication);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_publication, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PublicationModel pub = liste.get(position);
        holder.tvTitre.setText(pub.getTitre());
        holder.tvContenu.setText(pub.getContenu());
        holder.tvDate.setText(pub.getDate());
    }

    @Override
    public int getItemCount() { return liste.size(); }

    public void mettreAJour(List<PublicationModel> nouvelleListe) {
        this.liste = nouvelleListe;
        notifyDataSetChanged();
    }
}