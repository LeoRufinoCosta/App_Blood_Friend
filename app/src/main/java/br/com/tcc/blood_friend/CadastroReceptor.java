package br.com.tcc.blood_friend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;



public class CadastroReceptor extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText edit_user, edit_idade, edit_email, edit_bio, edit_senha, edit_confirmar_senha;
    private Spinner edit_tipo_sanguineo, edit_sexo;
    private Button bt_cadastrado;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;
    String usuarioID;
    int idade_int;
    String[] msgs = {"Preencha todos os campos!", "As senhas não conferem!", "Cadastro realizado com sucesso.",
            "Selecione ou preencha todos os campos!.", "Deve ter entre 16 e 60 anos."};


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.voltar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_voltar:
                Intent intent = new Intent(CadastroReceptor.this, CadastroEtapa01.class);
                startActivity(intent);

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_receptor);

        //getSupportActionBar().hide();
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


        bt_cadastrado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String nome = edit_user.getText().toString();
                String idade = edit_idade.getText().toString();
                String email = edit_email.getText().toString();
                String senha = edit_senha.getText().toString();
                String confirmar_senha = edit_confirmar_senha.getText().toString();
                if(idade.isEmpty()){
                    idade_int = 0;
                }else{ idade_int = Integer.parseInt(idade, 10);}


                if (nome.isEmpty()  || idade.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmar_senha.isEmpty()){
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
                        //if(idade_int>60 && idade_int<16){
                        if(gen.equals("GÊNERO") || sangue.equals("TIPO SANGUE")){
                            Snackbar snackbar = Snackbar.make(v, msgs[3], Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(Color.WHITE);
                            snackbar.setTextColor(Color.BLACK);
                            snackbar.show();
                        }
                        else{
                            if(senha.equals(confirmar_senha)){

                                CadastrarUsuario(v);

                                progressbar.setVisibility(View.VISIBLE);
                                bt_cadastrado.setVisibility(View.INVISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        FirebaseAuth.getInstance().signOut();
                                        Intent intent = new Intent(CadastroReceptor.this, Inicial.class);
                                        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                },3000);

                            }
                            else{
                                Snackbar snackbar = Snackbar.make(v, msgs[1], Snackbar.LENGTH_SHORT);
                                snackbar.setBackgroundTint(Color.WHITE);
                                snackbar.setTextColor(Color.BLACK);
                                snackbar.show();
                            }
                        }
                    }
                }

            }
        });

    }



    private void CadastrarUsuario(View v){

        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    //SalvarDadosUsuario("Usuarios");
                    SalvarDadosUsuario();

                }else{
                    String erro;
                    try {
                        throw task.getException();

                    }catch (FirebaseAuthWeakPasswordException e){
                        erro = "Digite uma senha com mínimo 6 caracteres.";

                    }catch (FirebaseAuthUserCollisionException e){
                        erro = "Este e-mail já foi cadastrado!";

                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erro = "Digite um e-mail válido!";

                    }catch(Exception e){
                        erro = "Erro ao cadastrar usuário.";

                    }
                    Snackbar snackbar = Snackbar.make(v, erro, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });
    }



    private void SalvarDadosUsuario(){

        String nome = edit_user.getText().toString();
        String idade = edit_idade.getText().toString();
        String email = edit_email.getText().toString();
        String bio = edit_bio.getText().toString();
        //String tipo_sanguineo = String.valueOf(edit_tipo_sanguineo.getSelectedItemPosition());
        String tipo_sanguineo = edit_tipo_sanguineo.getSelectedItem().toString();
        String tipo_sexo = edit_sexo.getSelectedItem().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String,Object> usuarios = new HashMap<>();
        usuarios.put("ID", usuarioID);
        usuarios.put("Nome", nome);
        usuarios.put("Idade", idade);
        usuarios.put("Email", email);
        usuarios.put("Bio", bio);
        usuarios.put("TipoSanguineo", tipo_sanguineo);
        usuarios.put("Genero", tipo_sexo);




        DocumentReference documentReference = db.collection("Usuario").document(usuarioID);
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("db","Sucesso ao salvar os dados");

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("db_error", "Erro ao salvar os dados"+ e.toString());

                    }
                });

        DocumentReference documentReference1 = db.collection("Receptor").document(usuarioID);
        documentReference1.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("db","Sucesso ao salvar os dados");

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("db_error", "Erro ao salvar os dados"+ e.toString());

                    }
                });
    }


    private void IniciarComponenetes(){
        edit_user = findViewById(R.id.edit_user);
        edit_idade = findViewById(R.id.edit_idade);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        edit_sexo = findViewById(R.id.edit_sexo);
        edit_bio = findViewById(R.id.edit_bio);
        edit_tipo_sanguineo = findViewById(R.id.edit_tipo_sangue);
        edit_confirmar_senha = findViewById(R.id.edit_confirmar_senha);
        bt_cadastrado = findViewById(R.id.bt_cadastrado);
        progressbar = findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String text = adapterView.getItemAtPosition(0).toString();
        String text2 = adapterView.getSelectedItem().toString();
        //Toast.makeText(adapterView.getContext(), text , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}