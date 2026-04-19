package com.fitpro.myapplication2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UtilisateursFragment extends Fragment {

    private RecyclerView recycler;
    private UtilisateurAdapter adapter;
    private List<UtilisateurModel> liste = new ArrayList<>();
    private FirebaseFirestore db;
    private TextView tvNombreUsers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_utilisateurs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();

        tvNombreUsers = view.findViewById(R.id.tvNombreUsers);

        recycler = view.findViewById(R.id.recyclerUtilisateurs);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new UtilisateurAdapter(liste);
        recycler.setAdapter(adapter);

        chargerUtilisateurs();
    }

    private void chargerUtilisateurs() {
        db.collection("users")
                .whereEqualTo("role", "user")
                .addSnapshotListener((snapshot, e) -> {
                    if (snapshot == null) return;
                    liste.clear();
                    for (QueryDocumentSnapshot doc : snapshot) {
                        UtilisateurModel user = new UtilisateurModel(
                                doc.getId(),
                                doc.getString("nom"),
                                doc.getString("email"),
                                doc.getString("role"),
                                doc.getString("statutAbonnement"),
                                doc.getString("dateExpiration")
                        );
                        liste.add(user);
                    }
                    adapter.mettreAJour(liste);
                    tvNombreUsers.setText(liste.size() + " membres");
                });
    }
}