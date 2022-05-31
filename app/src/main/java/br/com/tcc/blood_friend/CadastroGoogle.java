package br.com.tcc.blood_friend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CadastroGoogle extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText edit_user, edit_idade, edit_email, edit_senha, edit_confirmar_senha;
    private Spinner edit_tipo_sanguineo, edit_sexo, edit_loc;
    private Button bt_cadastrado;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;
    String usuarioID;
    int idade_int;

    String[] msgs = {"Preencha todos os campos!", "As senhas não conferem!", "Cadastro realizado com sucesso.", "Selecione ou preencha todos os campos!.",
            "Deve ter entre 16 e 60 anos."};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_google);

        IniciarComponenetes();

        Spinner spinner_sexo = findViewById(R.id.edit_sexo);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.generos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sexo.setAdapter(adapter);
        spinner_sexo.setOnItemSelectedListener(this);

        Spinner spinner_sangue = findViewById(R.id.edit_tipo_sangue);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.tipo_sanguineo, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sangue.setAdapter(adapter2);
        spinner_sangue.setOnItemSelectedListener(this);

        Spinner spinner_loc = findViewById(R.id.edit_localizacao);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.localizacao, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_loc.setAdapter(adapter3);
        spinner_loc.setOnItemSelectedListener(this);



        bt_cadastrado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String nome = edit_user.getText().toString();
                String idade = edit_idade.getText().toString();
                if(idade.isEmpty()){
                    idade_int = 0;
                }else{ idade_int = Integer.parseInt(idade, 10);}

                if (nome.isEmpty()  || idade.isEmpty()){
                    Snackbar snackbar = Snackbar.make(v, msgs[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
                else{
                    if(idade_int<16 || idade_int>69){
                        Snackbar snackbar = Snackbar.make(v, msgs[4], Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();

                    }
                    else{onItemSelected(spinner_sexo, v, 0, 0);
                        String gen = spinner_sexo.getSelectedItem().toString();
                        onItemSelected(spinner_sangue, v, 0, 0);
                        String sangue = spinner_sangue.getSelectedItem().toString();
                        onItemSelected(spinner_loc, v, 0, 0);
                        String loc = spinner_loc.getSelectedItem().toString();
                        //if(idade_int>60 && idade_int<16){
                        if(gen.equals("GÊNERO") || sangue.equals("TIPO SANGUE") || loc.equals("LOCALIZAÇÃO")){
                            Snackbar snackbar = Snackbar.make(v, msgs[3], Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(Color.WHITE);
                            snackbar.setTextColor(Color.BLACK);
                            snackbar.show();
                        }
                        else{
                            AtualizarUsuario();

                            progressbar.setVisibility(View.VISIBLE);
                            bt_cadastrado.setVisibility(View.INVISIBLE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //FirebaseAuth.getInstance().signOut();
                                    Intent intent = new Intent(CadastroGoogle.this, Principal.class);
                                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            },3000);
                        }
                    }
                }

            }
        });

    }



    private void AtualizarUsuario(){

        String nome = edit_user.getText().toString();
        String idade = edit_idade.getText().toString();
        String sangue = edit_tipo_sanguineo.getSelectedItem().toString();
        String sexo = edit_sexo.getSelectedItem().toString();
        String loc = edit_loc.getSelectedItem().toString();

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usuarioGoogle = db.collection("Usuario");
        String emailUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String nulo = "";

        Map<String, Object> attUser = new HashMap<>();
        attUser.put("ID", usuarioID);
        attUser.put("Nome", nome);
        attUser.put("Idade", idade);
        attUser.put("TipoSanguineo", sangue);
        attUser.put("Genero", sexo);
        attUser.put("Localizacao", loc);

        usuarioGoogle.whereEqualTo("Nome", nulo).whereEqualTo("Email", emailUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        if(document.getId().equals(usuarioID)){
                            db.collection("Usuario").document(usuarioID).update(attUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Toast.makeText(CadastroGoogle.this, "Sucesso", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CadastroGoogle.this, "Erro", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Intent intent = new Intent(CadastroGoogle.this, Principal.class);
                            startActivity(intent);
                        }


                        Log.d("Consulta", document.getId() + "-->" + document.getData());
                    }

                }
            }
        });
    }


    private void IniciarComponenetes(){
        edit_user = findViewById(R.id.edit_user);
        edit_idade = findViewById(R.id.edit_idade);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        edit_sexo = findViewById(R.id.edit_sexo);
        edit_loc = findViewById(R.id.edit_localizacao);
        edit_tipo_sanguineo = findViewById(R.id.edit_tipo_sangue);
        edit_confirmar_senha = findViewById(R.id.edit_confirmar_senha);
        bt_cadastrado = findViewById(R.id.bt_cadastrado);
        progressbar = findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}