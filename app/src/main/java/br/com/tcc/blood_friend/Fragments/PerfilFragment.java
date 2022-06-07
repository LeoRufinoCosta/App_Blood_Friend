package br.com.tcc.blood_friend.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import br.com.tcc.blood_friend.R;


public class PerfilFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_perfil, container, false);
        TextView nomeUser = (TextView) v.findViewById(R.id.text_nome_user);
        TextView idade = (TextView) v.findViewById(R.id.text_idade);
        TextView email = (TextView) v.findViewById(R.id.text_email);
        TextView sexo = (TextView) v.findViewById(R.id.text_sexo);
        TextView loc = (TextView) v.findViewById(R.id.text_loc);
        TextView sangue = (TextView) v.findViewById(R.id.text_sangue);

        String emailUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuario").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null){
                    nomeUser.setText(documentSnapshot.getString("Nome"));
                    email.setText(emailUser);
                    idade.setText(documentSnapshot.getString("Idade"));
                    sexo.setText(documentSnapshot.getString("Genero"));
                    //loc.setText(documentSnapshot.getString("Localizacao"));
                    sangue.setText(documentSnapshot.getString("TipoSanguineo"));


                }
            }
        });
        return v;
    }

}