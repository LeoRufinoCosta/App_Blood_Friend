package br.com.tcc.blood_friend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import br.com.tcc.blood_friend.Cadastro.CadastroEtapa01;
import br.com.tcc.blood_friend.Cadastro.CadastroReceptor;

public class EditPerfil extends AppCompatActivity {

    EditText editNome, editIdade, editBio;
    Spinner editTipo_sangue, editSexo;
    Button bt_salvar;

    FirebaseFirestore db;
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
                Intent intent = new Intent(EditPerfil.this, Perfil.class);
                startActivity(intent);

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_perfil);

        editNome = findViewById(R.id.editNome);
        editIdade = findViewById(R.id.editIdade);
        editSexo = findViewById(R.id.editSexo);
        editBio = findViewById(R.id.editBio);
        editTipo_sangue = findViewById(R.id.editTipo_sangue);
        bt_salvar = findViewById(R.id.bt_salvar);

        bt_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atualizarPerfil();
                Intent intent = new Intent(EditPerfil.this, Perfil.class);
                startActivity(intent);
            }
        });


    }

    private void atualizarPerfil(){
        db = FirebaseFirestore.getInstance();
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String nome = editNome.getText().toString();
        String idade = editIdade.getText().toString();
        String bio = editBio.getText().toString();
        String tipo_sanguineo = editTipo_sangue.getSelectedItem().toString();
        String tipo_sexo = editSexo.getSelectedItem().toString();

        Map<String,Object> usuario = new HashMap<>();
        usuario.put("Nome", nome);
        usuario.put("Idade", idade);
        usuario.put("Bio", bio);
        usuario.put("TipoSanguineo", tipo_sanguineo);
        usuario.put("Genero", tipo_sexo);

        DocumentReference documentReference = db.collection("Usuario").document(usuarioID);
        documentReference.update(usuario);
        DocumentReference documentReference2 = db.collection("Doador").document(usuarioID);
        documentReference2.update(usuario);
        DocumentReference documentReference3 = db.collection("Receptor").document(usuarioID);
        documentReference3.update(usuario);
    }
}