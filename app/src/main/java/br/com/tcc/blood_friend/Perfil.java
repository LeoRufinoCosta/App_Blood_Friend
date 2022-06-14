package br.com.tcc.blood_friend;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import br.com.tcc.blood_friend.Fragments.ConfigFragment;


public class Perfil extends AppCompatActivity {

    private TextView nomeUser, idade, tipo_sangue, sexo, email;
    private Button bt_edit;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.voltar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_voltar:
                Intent intent = new Intent(Perfil.this, Principal.class);
                startActivity(intent);

                break;

        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        //getSupportActionBar().hide();
        IniciarComponentes();
        VerificarAuth();

        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Perfil.this, EditPerfil.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //FirebaseAuth.getInstance().signOut();
                //VerificarAuth();
            }
        });

    }


    private void VerificarAuth(){
        if (FirebaseAuth.getInstance().getUid() == null){
            Intent intent = new Intent(Perfil.this, Inicial.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
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
                    tipo_sangue.setText(documentSnapshot.getString("TipoSanguineo"));


                }
            }
        });
    }




    private void IniciarComponentes(){
        nomeUser = findViewById(R.id.text_nome_user);
        idade = findViewById(R.id.text_idade);
        email = findViewById(R.id.text_email);
        sexo = findViewById(R.id.text_sexo);
        tipo_sangue = findViewById(R.id.text_sangue);
        bt_edit = findViewById(R.id.bt_edit);

    }
}