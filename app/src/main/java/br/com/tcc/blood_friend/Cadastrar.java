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



public class Cadastrar extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText edit_user, edit_idade, edit_email, edit_senha, edit_confirmar_senha;
    private Spinner edit_tipo_sanguineo, edit_sexo, edit_loc;
    private Button bt_cadastrado;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;
    String usuarioID;



    String[] msgs = {"Preencha todos os campos!", "As senhas não conferem!", "Cadastro realizado com sucesso.", "Selecione ou preencha todos os campos!."};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

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
                String email = edit_email.getText().toString();
                String senha = edit_senha.getText().toString();
                String confirmar_senha = edit_confirmar_senha.getText().toString();

                if (nome.isEmpty() || idade.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmar_senha.isEmpty()){
                    Snackbar snackbar = Snackbar.make(v, msgs[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
                else{
                    onItemSelected(spinner_sexo, v, 0, 0);
                    String gen = spinner_sexo.getSelectedItem().toString();
                    onItemSelected(spinner_sangue, v, 0, 0);
                    String sangue = spinner_sangue.getSelectedItem().toString();
                    onItemSelected(spinner_loc, v, 0, 0);
                    String loc = spinner_loc.getSelectedItem().toString();
                    if(gen.equals("GÊNERO") || sangue.equals("TIPO SANGUE") || loc.equals("LOCALIZAÇÃO")){
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
                                    Intent intent = new Intent(Cadastrar.this, Inicial.class);
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
                };
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
        //String tipo_sanguineo = String.valueOf(edit_tipo_sanguineo.getSelectedItemPosition());
        String tipo_sanguineo = edit_tipo_sanguineo.getSelectedItem().toString();
        String tipo_sexo = edit_sexo.getSelectedItem().toString();
        String localizacao = edit_loc.getSelectedItem().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String,Object> usuarios = new HashMap<>();
        usuarios.put("Nome", nome);
        usuarios.put("Idade", idade);
        usuarios.put("Email", email);
        usuarios.put("TipoSanguineo", tipo_sanguineo);
        usuarios.put("Genero", tipo_sexo);
        usuarios.put("Localizacao", localizacao);

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
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
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String text = adapterView.getItemAtPosition(0).toString();
        String text2 = adapterView.getSelectedItem().toString();
        //Toast.makeText(adapterView.getContext(), text , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}