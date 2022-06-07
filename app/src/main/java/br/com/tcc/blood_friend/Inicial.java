package br.com.tcc.blood_friend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import br.com.tcc.blood_friend.Cadastro.CadastroEtapa01;
import br.com.tcc.blood_friend.Cadastro.CadastroGoogle;

public class Inicial extends AppCompatActivity {

    private Button bt_entrar, bt_cadastrar;
    private FirebaseAuth mAuth;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    Button bt_google;
    String usuarioID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);

        getSupportActionBar().hide();
        IniciarComponentes();

        mAuth = FirebaseAuth.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("707764864293-og1rgefeespf7j94cvnk4i85v9a4s6sj.apps.googleusercontent.com")
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        bt_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Inicial.this, Login.class);
                startActivity(intent);
            }
        });

        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Inicial.this, CadastroEtapa01.class);
                startActivity(intent);
            }
        });

        bt_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        if(usuarioAtual != null){
            Log.d("Usuario", usuarioAtual.getUid());
            Intent intent = new Intent(Inicial.this, Principal.class);
            startActivity(intent);
            finish();
        }
    }

    private void signIn(){
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent, 1);
    }

    private void login_google(String token){
        AuthCredential credencial = GoogleAuthProvider.getCredential(token, null);

        mAuth.signInWithCredential(credencial).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    SalvarDadosUsuario();
                } else{
                    Toast.makeText(getApplicationContext(), "Erro ao logar com Google!", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 1) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
            try{
                GoogleSignInAccount conta = task.getResult(ApiException.class);
                login_google(conta.getIdToken());
            } catch (ApiException exception){
                Log.d("Erro", exception.toString());
                Toast.makeText(getApplicationContext(), "Nenhum úsuario logado no aparelho!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void SalvarDadosUsuario(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usuarioGoogle = db.collection("Usuario");
        String emailUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String nulo = "";
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String,Object> usuarios = new HashMap<>();
        usuarios.put("Nome", "");
        usuarios.put("Idade", "");
        usuarios.put("Email", emailUser);
        usuarios.put("TipoSanguineo", "");
        usuarios.put("Genero", "");
        usuarios.put("Localizacao", "");

        db.collection("Usuario").document(usuarioID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                if(document.exists()){
                    usuarioGoogle.whereEqualTo("Nome", nulo).whereEqualTo("Email", emailUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    if(document.getId().equals(usuarioID)){
                                        Toast.makeText(getApplicationContext(), "Login com Google efetuado com sucesso!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Inicial.this, CadastroGoogle.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Login com Google efetuado com sucesso!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Inicial.this,Principal.class);
                                        startActivity(intent);
                                    }
                                    Log.d("Consulta", document.getId() + "-->" + document.getData());
                                }

                            }
                        }
                    });
                }else{
                    DocumentReference documentReference = db.collection("Usuario").document(usuarioID);
                    documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("db","Sucesso ao salvar os dados");
                            Toast.makeText(getApplicationContext(), "Login com Google efetuado com sucesso!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Inicial.this,CadastroGoogle.class);
                            startActivity(intent);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("db_error", "Erro ao salvar os dados"+ e.toString());
                        }
                    });
                }
            }
        });
    }

    private void IniciarComponentes(){
        bt_cadastrar = findViewById(R.id.bt_cadastrar);
        bt_entrar = findViewById(R.id.bt_entrar);
        bt_google = findViewById(R.id.bt_google);


    }
}