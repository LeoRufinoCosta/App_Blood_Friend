package br.com.tcc.blood_friend.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import br.com.tcc.blood_friend.EditPerfil;
import br.com.tcc.blood_friend.Perfil;
import br.com.tcc.blood_friend.R;

public class ConfigFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_config, container, false);
        TextView nome = (TextView) v.findViewById(R.id.nome);
        TextView editPerfil = (TextView) v.findViewById(R.id.editPerfil);

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Usuario").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null){
                    nome.setText(documentSnapshot.getString("Nome"));
                }
            }
        });
        editPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Perfil.class);
                startActivity(intent);
            }
        });
        return v;
    }
}