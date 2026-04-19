package com.fitpro.myapplication2;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PublicationsFragment extends Fragment {

    private RecyclerView recycler;
    private PublicationAdapter adapter;
    private List<PublicationModel> liste = new ArrayList<>();
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_publications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recycler = view.findViewById(R.id.recyclerPublications);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new PublicationAdapter(liste);
        recycler.setAdapter(adapter);

        // Bouton retour
        view.findViewById(R.id.btnRetourPublications).setOnClickListener(v ->
                Navigation.findNavController(view).popBackStack()
        );

        // Vérifier le rôle
        db.collection("users").document(uid).get()
                .addOnSuccessListener(doc -> {
                    String role = doc.getString("role");
                    if (!"admin".equals(role)) {
                        // User → cacher bouton ajouter
                        view.findViewById(R.id.btnAjouterPublication).setVisibility(View.GONE);
                    } else {
                        // Admin → cacher bouton retour
                        view.findViewById(R.id.btnRetourPublications).setVisibility(View.GONE);
                    }
                });

        // Charger les publications
        chargerPublications();

        // Bouton ajouter
        view.findViewById(R.id.btnAjouterPublication).setOnClickListener(v ->
                afficherDialogAjouter()
        );
    }

    private void chargerPublications() {
        db.collection("publications")
                .orderBy("date")
                .addSnapshotListener((snapshot, e) -> {
                    if (snapshot == null) return;
                    liste.clear();
                    for (QueryDocumentSnapshot doc : snapshot) {
                        PublicationModel pub = new PublicationModel(
                                doc.getId(),
                                doc.getString("titre"),
                                doc.getString("contenu"),
                                doc.getString("date")
                        );
                        liste.add(pub);
                    }
                    adapter.mettreAJour(liste);
                });
    }

    private void afficherDialogAjouter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Nouvelle publication");

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(48, 24, 48, 24);

        EditText etTitre = new EditText(requireContext());
        etTitre.setHint("Titre");
        layout.addView(etTitre);

        EditText etContenu = new EditText(requireContext());
        etContenu.setHint("Contenu");
        etContenu.setMinLines(3);
        layout.addView(etContenu);

        builder.setView(layout);

        builder.setPositiveButton("Publier", (dialog, which) -> {
            String titre   = etTitre.getText().toString().trim();
            String contenu = etContenu.getText().toString().trim();

            if (titre.isEmpty() || contenu.isEmpty()) {
                Toast.makeText(requireContext(), "Remplissez tous les champs",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            String date = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    .format(new Date());

            Map<String, Object> pub = new HashMap<>();
            pub.put("titre", titre);
            pub.put("contenu", contenu);
            pub.put("date", date);

            db.collection("publications").add(pub)
                    .addOnSuccessListener(ref ->
                            Toast.makeText(requireContext(), "✅ Publication ajoutée !",
                                    Toast.LENGTH_SHORT).show()
                    )
                    .addOnFailureListener(e ->
                            Toast.makeText(requireContext(), "❌ Erreur : " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show()
                    );
        });

        builder.setNegativeButton("Annuler", null);
        builder.show();
    }
}